package brum.domain.impl.documents;

import brum.domain.documents.PrepareDocumentUC;
import brum.domain.file.resolvers.ContactDetailsFileResolver;
import brum.domain.recipients.AddRecipientUC;
import brum.domain.recipients.ValidateRecipientsUC;
import brum.model.dto.categories.Category;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.DataFileType;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentStatus;
import brum.model.dto.documents.DocumentType;
import brum.model.dto.documents.PublicationStatus;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentityStatus;
import brum.model.dto.recipients.DocumentNotificationStatus;
import brum.model.dto.recipients.Recipient;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.model.exception.validation.*;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.IdentityPersistenceGateway;
import brum.proxy.CategoryProxy;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrepareDocumentUCImpl implements PrepareDocumentUC {

    private final IdentityPersistenceGateway identityPersistenceGateway;
    private final DocumentPersistenceGateway documentPersistenceGateway;
    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private final DocumentProxy documentProxy;
    private final CategoryProxy categoryProxy;
    private final ValidateRecipientsUC validateRecipientsUC;
    private final AddRecipientUC addRecipientUC;

    public PrepareDocumentUCImpl(IdentityPersistenceGateway identityPersistenceGateway,
                                 DocumentPersistenceGateway documentPersistenceGateway,
                                 ParameterPersistenceGateway parameterPersistenceGateway,
                                 DocumentProxy documentProxy, CategoryProxy categoryProxy,
                                 ValidateRecipientsUC validateRecipientsUC,
                                 AddRecipientUC addRecipientUC) {
        this.identityPersistenceGateway = identityPersistenceGateway;
        this.documentPersistenceGateway = documentPersistenceGateway;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        this.documentProxy = documentProxy;
        this.categoryProxy = categoryProxy;
        this.validateRecipientsUC = validateRecipientsUC;
        this.addRecipientUC = addRecipientUC;
    }

    @Override
    public Document prepareDocument(DataFile document, DataFile contactDetails, Document documentInfo, String publisherUsername, DocumentType documentType) {
        if (!document.getFileType().equals(DataFileType.PDF)) {
            throw new BRUMGeneralException(ErrorStatusCode.FORMAT_NOT_SUPPORTED, LocalDateTime.now());
        }
        if (documentInfo == null) {
            throw new BRUMGeneralException(ErrorStatusCode.DOCUMENT_INFO_MISSING, LocalDateTime.now());
        }
        documentInfo.setDocumentType(documentType);
            documentInfo.setSource(document.getFile());
        documentInfo.setFileName(document.getFileName());
        documentInfo.setSignatoryId(publisherUsername);
        setCategoryPath(documentInfo);
        if (documentType.equals(DocumentType.PUBLIC)) {
            preparePublicDocument(contactDetails, documentInfo);
        } else if (documentType.equals(DocumentType.PRIVATE)) {
            preparePrivateDocument(documentInfo);
        }
        Boolean isOneStep = parameterPersistenceGateway.getParameterValue(ParameterKey.ONE_STEP_PUBLISH);
        BemResponse<Document> response;
        if (Boolean.TRUE.equals(documentInfo.getDraft())) {
            response = documentProxy.prepareDocument(documentInfo);
            documentInfo.setDocumentPublicationStatus(PublicationStatus.PREPARED_TO_SIGN);
            documentInfo.setStatus(DocumentStatus.PREPARED_TO_SIGN);
        } else {
            documentInfo.setPublishingPersonId(publisherUsername);
            response = documentProxy.publishDocument(documentInfo);
            documentInfo.setDocumentPublicationStatus(PublicationStatus.PUBLISHING_INITIATED);
            documentInfo.setStatus(DocumentStatus.UPLOADING);
        }
        if (!response.isSuccess()) {
            throw response.getException();
        }
        documentInfo.setJobId(response.getResponse().getJobId());
        documentPersistenceGateway.save(documentInfo);
        if (documentType.equals(DocumentType.PRIVATE)) {
            Identity identity = documentInfo.getIdentity();
            identity.setStatus(IdentityStatus.PUBLISHED);
            identityPersistenceGateway.saveIdentity(identity);
        }
        if (documentType.equals(DocumentType.PUBLIC) && !documentInfo.getNotificationReceivers().isEmpty()) {
            addRecipientUC.addRecipients(documentInfo, DocumentNotificationStatus.NOTIFICATION_SENT);
        }
        return response.getResponse();
    }

    private void preparePublicDocument(DataFile contactDetails, Document documentInfo) {
        if (contactDetails == null) {
            documentInfo.setNotificationReceivers(new ArrayList<>());
            return;
        }
        documentInfo.setNotificationReceivers(getNotificationReceivers(contactDetails));
    }

    private void preparePrivateDocument(Document documentInfo) {
        Identity identity = identityPersistenceGateway.getIdentityByExternalId(documentInfo.getIdentity().getExternalId());
        if (identity == null) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_NOT_FOUND, LocalDateTime.now());
        }
        if (Boolean.FALSE.equals(identity.getIsActive())) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_INACTIVE, LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(identity.getIsGdprSuspended())) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_SUSPENDED, LocalDateTime.now());
        }
        documentInfo.setIdentity(identity);
        Recipient recipient = new Recipient();
        recipient.setEmail(identity.getEmail());
        recipient.setPhoneNumber(identity.getPhoneNumber());
        //documentInfo.setNotificationReceivers(Collections.singletonList(recipient));
    }

    private List<Recipient> getNotificationReceivers(DataFile contactDetails) {
        List<Recipient> recipients = new ContactDetailsFileResolver(contactDetails.getFile(), contactDetails.getFileType()).resolve();
        List<ValidationException> validationResponse = validateRecipientsUC.validateRecipientList(recipients);
        List<Recipient> invalidRecipients = validationResponse.stream().map(v -> (Recipient) v.getInvalidData()).collect(Collectors.toList());
        recipients.removeAll(invalidRecipients);
        return recipients;
    }

    private void setCategoryPath(Document document) {
        if (document.getCategoryId() == null) {
            return;
        }
        BemResponse<Category> response = categoryProxy.getCategory(document.getCategoryId());
        if (!response.isSuccess()) {
            throw response.getException();
        }
        document.setCategory(response.getResponse().getFullPath());
    }
}