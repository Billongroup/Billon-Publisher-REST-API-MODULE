package brum.domain.impl.recipients;

import brum.domain.file.resolvers.ContactDetailsFileResolver;
import brum.domain.recipients.ValidateRecipientsUC;
import brum.domain.validator.RecipientValidator;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.recipients.Recipient;
import brum.model.exception.validation.*;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.RecipientPersistenceGateway;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValidateRecipientsUCImpl implements ValidateRecipientsUC {

    private final RecipientPersistenceGateway recipientPersistenceGateway;
    private final ParameterPersistenceGateway parameterPersistenceGateway;

    public ValidateRecipientsUCImpl(RecipientPersistenceGateway recipientPersistenceGateway,
                                    ParameterPersistenceGateway parameterPersistenceGateway) {
        this.recipientPersistenceGateway = recipientPersistenceGateway;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
    }

    @Override
    public void getFileValidity(DataFile recipientFile) {
        List<Recipient> recipientList = new ContactDetailsFileResolver(recipientFile.getFile(), recipientFile.getFileType()).resolve();
        List<ValidationException> validationResult = validateRecipientList(recipientList);
        if (!validationResult.isEmpty()) {
            throw new ListValidationException(recipientList.size() - validationResult.size(), recipientList.size(), validationResult, LocalDateTime.now());
        }
    }

    @Override
    public List<ValidationException> validateRecipientList(List<Recipient> recipientList) {
        String recipientSourceSystems = parameterPersistenceGateway.getParameterValue(ParameterKey.RECIPIENT_SOURCE_SYSTEM_NAMES);
        List<String> sourceSystemList = Arrays.stream(recipientSourceSystems.split(",")).map(String::trim).collect(Collectors.toList());
        List<ValidationException> errors = new ArrayList<>();
        Set<String> extIdSet = recipientList.stream().map(Recipient::getExternalId).collect(Collectors.toSet());
        Map<String, Recipient> idRecipientMap = recipientPersistenceGateway.getIdRecipientMapByExtIdSet(extIdSet);
        Set<String> checkedIds = new HashSet<>();
        int counter = 0;
        for (Recipient recipient : recipientList) {
            counter++;
            Map<InvalidField, ValidationErrorType> errorDetails = RecipientValidator.validateRecipient(recipient, sourceSystemList);
            if (!errorDetails.isEmpty()) {
                if (idRecipientMap.containsKey(recipient.getExternalId()) &&
                        !StringUtils.equals(idRecipientMap.get(recipient.getExternalId()).getEmail(), recipient.getEmail())) {
                    errorDetails.put(InvalidField.EMAIL, ValidationErrorType.DIFFERENT);
                }
                errors.add(new ValidationException(recipient, counter, errorDetails));
                continue;
            }
            if (checkedIds.contains(recipient.getExternalId())) {
                errorDetails.put(InvalidField.SYSTEM_SOURCE, ValidationErrorType.NON_UNIQUE);
                errorDetails.put(InvalidField.ID, ValidationErrorType.NON_UNIQUE);
            }
            if (idRecipientMap.containsKey(recipient.getExternalId())) {
                compareContactDetails(idRecipientMap.get(recipient.getExternalId()), recipient, errorDetails);
            }
            checkedIds.add(recipient.getExternalId());
            if (!errorDetails.isEmpty()) {
                errors.add(new ValidationException(recipient, counter, errorDetails));
            }
        }
        return errors;
    }

    private void compareContactDetails(Recipient dbRecipient, Recipient toValidate, Map<InvalidField, ValidationErrorType> errorDetails) {
        if (!StringUtils.equals(dbRecipient.getEmail(), toValidate.getEmail())) {
            errorDetails.put(InvalidField.EMAIL, ValidationErrorType.DIFFERENT);
        }
        if (!errorDetails.containsKey(InvalidField.PHONE_NUMBER) &&
                !StringUtils.equals(dbRecipient.getPhoneNumber(), toValidate.getPhoneNumber())) {
            errorDetails.put(InvalidField.PHONE_NUMBER, ValidationErrorType.NEEDS_UPDATE);
        }
    }

}
