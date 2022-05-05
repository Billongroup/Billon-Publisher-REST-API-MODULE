package brum.domain.impl.documents;

import brum.domain.documents.ResendNotificationUC;
import brum.domain.recipients.GetDocumentRecipientsUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentType;
import brum.model.dto.identities.Identity;
import brum.model.dto.recipients.*;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.IdentityPersistenceGateway;
import brum.persistence.RecipientPersistenceGateway;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResendNotificationUCImpl implements ResendNotificationUC {

    private static final List<DocumentRecipientStatus> STATUSES_TO_SET_SENT = Arrays.asList(
            DocumentRecipientStatus.NEW, DocumentRecipientStatus.UPDATED);

    private final GetDocumentRecipientsUC getDocumentRecipientsUC;
    private final DocumentProxy documentProxy;
    private final IdentityPersistenceGateway identityPersistenceGateway;
    private final RecipientPersistenceGateway recipientPersistenceGateway;

    public ResendNotificationUCImpl(GetDocumentRecipientsUC getDocumentRecipientsUC, DocumentProxy documentProxy,
                                    IdentityPersistenceGateway identityPersistenceGateway,
                                    RecipientPersistenceGateway recipientPersistenceGateway) {
        this.getDocumentRecipientsUC = getDocumentRecipientsUC;
        this.documentProxy = documentProxy;
        this.identityPersistenceGateway = identityPersistenceGateway;
        this.recipientPersistenceGateway = recipientPersistenceGateway;
    }

    @Override
    public void resendNotifications(String blockchainAddress, RecipientsToNotify recipientsToNotify) {
        if (recipientsToNotify == null || (!recipientsToNotify.getIdContactTypeMap().isEmpty() && !recipientsToNotify.getGroups().isEmpty()) ||
                (recipientsToNotify.getIdContactTypeMap().isEmpty() && recipientsToNotify.getGroups().isEmpty())) {
            throw new BRUMGeneralException(ErrorStatusCode.GROUP_OR_MAP_REQUIRED, LocalDateTime.now());
        }
        List<DocumentRecipient> documentRecipients;
        if (!recipientsToNotify.getIdContactTypeMap().isEmpty()) {
            documentRecipients = getDocumentRecipientsByIdContactTypeMap(blockchainAddress, recipientsToNotify.getIdContactTypeMap());
        } else {
            documentRecipients = getDocumentRecipientsByGroups(blockchainAddress, recipientsToNotify.getGroups());
        }

        Map<String, Recipient> recipients = new HashMap<>();
        for (DocumentRecipient documentRecipient : documentRecipients) {
            if (!recipients.containsKey(documentRecipient.getExternalId())) {
                Recipient recipient = new Recipient();
                recipient.setExternalId(documentRecipient.getExternalId());
                recipients.put(documentRecipient.getExternalId(), recipient);
            }
            if (documentRecipient.getType().equals(ContactDetailsType.EMAIL)) {
                recipients.get(documentRecipient.getExternalId()).setEmail(documentRecipient.getContactDetails());
            } else {
                recipients.get(documentRecipient.getExternalId()).setPhoneNumber(documentRecipient.getContactDetails());
            }
        }
        documentProxy.resendDocumentNotifications(blockchainAddress, new ArrayList<>(recipients.values()));

        Set<Long> contactDetailsIdFirstSend = documentRecipients.stream().filter(r -> STATUSES_TO_SET_SENT.contains(r.getStatus()))
                .map(DocumentRecipient::getContactDetailsId).collect(Collectors.toSet());
        if (!contactDetailsIdFirstSend.isEmpty()) {
            recipientPersistenceGateway.setContactDetailsSentStatus(contactDetailsIdFirstSend, blockchainAddress);
        }
    }

    @Override
    public void resendAuthorizationCodes(String blockchainAddress) {
        BemResponse<Document> getDocumentResponse = documentProxy.getDocumentByBlockchainAddress(blockchainAddress, false);
        if (!getDocumentResponse.isSuccess()) {
            throw getDocumentResponse.getException();
        }
        Document document = getDocumentResponse.getResponse();
        if (document.getDocumentType().equals(DocumentType.PUBLIC)) {
            throw new BRUMGeneralException(ErrorStatusCode.DOCUMENT_IS_PUBLIC, LocalDateTime.now());
        }
        Identity identity = identityPersistenceGateway.getIdentityByDocumentNumber(document.getIdentity().getDocumentNumber());
        if (identity == null) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_NOT_FOUND, LocalDateTime.now());
        }
        document.setIdentity(identity);
        BemResponse<?> resendResponse = documentProxy.resendAuthorizationCodes(document);
        if (!resendResponse.isSuccess()) {
            throw resendResponse.getException();
        }
    }

    private List<DocumentRecipient> getDocumentRecipientsByIdContactTypeMap(String blockchainAddress, Map<String, List<ContactDetailsType>> idContactTypeMap) {
        return recipientPersistenceGateway.getDocumentRecipients(blockchainAddress, idContactTypeMap);
    }

    private List<DocumentRecipient> getDocumentRecipientsByGroups(String blockchainAddress, List<RecipientGroup> groups) {
        DocumentRecipientSearchCriteria searchCriteria = new DocumentRecipientSearchCriteria();
        searchCriteria.setFilters(new DocumentRecipientFilters());
        searchCriteria.getFilters().setGroups(groups);
        return getDocumentRecipientsUC.getDocumentRecipients(blockchainAddress, searchCriteria).getRows();
    }
}
