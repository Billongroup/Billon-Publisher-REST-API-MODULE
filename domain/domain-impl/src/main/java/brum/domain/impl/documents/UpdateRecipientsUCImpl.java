package brum.domain.impl.documents;

import brum.common.utils.ExternalId;
import brum.domain.documents.UpdateRecipientsUC;
import brum.domain.file.resolvers.ContactDetailsFileResolver;
import brum.domain.recipients.AddRecipientUC;
import brum.domain.recipients.ValidateRecipientsUC;
import brum.model.dto.common.DataFile;
import brum.model.dto.documents.Document;
import brum.model.dto.recipients.DocumentNotificationStatus;
import brum.model.dto.recipients.Recipient;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import brum.model.exception.validation.ValidationException;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.RecipientPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UpdateRecipientsUCImpl implements UpdateRecipientsUC {

    private final DocumentPersistenceGateway documentPersistenceGateway;
    private final RecipientPersistenceGateway recipientPersistenceGateway;
    private final ValidateRecipientsUC validateRecipientsUC;
    private final AddRecipientUC addRecipientUC;

    public UpdateRecipientsUCImpl(DocumentPersistenceGateway documentPersistenceGateway,
                                  RecipientPersistenceGateway recipientPersistenceGateway,
                                  ValidateRecipientsUC validateRecipientsUC,
                                  AddRecipientUC addRecipientUC) {
        this.documentPersistenceGateway = documentPersistenceGateway;
        this.recipientPersistenceGateway = recipientPersistenceGateway;
        this.validateRecipientsUC = validateRecipientsUC;
        this.addRecipientUC = addRecipientUC;
    }

    @Override
    public void updateRecipientsFile(String id, DataFile recipientsFile) {
        List<Recipient> recipients = recipientPersistenceGateway.getDocumentRecipients(id);
        if (recipients == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        List<Recipient> recipientList = new ContactDetailsFileResolver(recipientsFile.getFile(), recipientsFile.getFileType()).resolve();
        List<ValidationException> validationResponse = validateRecipientsUC.validateRecipientList(recipientList);
        validationResponse.removeAll(getRecipientsToIgnore(validationResponse));

        Map<String, Recipient> forUpdate = getRecipientsForUpdate(validationResponse);
        recipientPersistenceGateway.updateRecipients(forUpdate, id);

        Set<String> dbDocRecipientExtIds = recipients.stream().map(Recipient::getExternalId).collect(Collectors.toSet());
        List<Recipient> toAdd = recipientList.stream().filter(r -> !dbDocRecipientExtIds.contains(r.getExternalId())).collect(Collectors.toList());
        Document document = documentPersistenceGateway.getDocument(id);
        document.setNotificationReceivers(toAdd);
        addRecipientUC.addRecipients(document, DocumentNotificationStatus.NEW);
    }

    private List<ValidationException> getRecipientsToIgnore(List<ValidationException> validationResult) {
         return validationResult.stream()
                .filter(this::shouldBeIgnored)
                 .collect(Collectors.toList());
    }

    private boolean shouldBeIgnored(ValidationException validationEntry) {
        Map<InvalidField, ValidationErrorType> errorInfo = validationEntry.getErrorInfo();
        if (errorInfo.size() == 0) {
            return false;
        }
        if (errorInfo.size() > 1) {
            return true;
        }
        return errorInfo.containsKey(InvalidField.PHONE_NUMBER) &&
                !errorInfo.get(InvalidField.PHONE_NUMBER).equals(ValidationErrorType.NEEDS_UPDATE);
    }

    private Map<String, Recipient> getRecipientsForUpdate(List<ValidationException> validationResult) {
        return validationResult.stream()
                .filter(this::isForUpdate)
                .map(v -> (Recipient) v.getInvalidData())
                .collect(Collectors.toMap(Recipient::getExternalId, r -> r));
    }

    private boolean isForUpdate(ValidationException validationEntry) {
        return validationEntry.getErrorInfo().containsKey(InvalidField.PHONE_NUMBER) &&
                validationEntry.getErrorInfo().get(InvalidField.PHONE_NUMBER).equals(ValidationErrorType.NEEDS_UPDATE);
    }
}
