package brum.domain.validator;

import brum.model.dto.recipients.Recipient;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import org.springframework.util.StringUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static brum.domain.validator.CommonValidator.*;
import static brum.model.exception.validation.InvalidField.*;
import static brum.model.exception.validation.ValidationErrorType.EMPTY;
import static brum.model.exception.validation.ValidationErrorType.INVALID;

public class RecipientValidator {
    private RecipientValidator() {}

    public static Map<InvalidField, ValidationErrorType> validateRecipient(Recipient recipient, List<String> sourceSystems) {
        Map<InvalidField, ValidationErrorType> errors = new EnumMap<>(InvalidField.class);
        if (recipient == null) {
            errors.put(RECIPIENT, EMPTY);
            return errors;
        }
        String[] splitId = recipient.getExternalId().split("_", 2);
        if (!StringUtils.hasText(splitId[0])) {
            errors.put(SYSTEM_SOURCE, EMPTY);
        } else if (!sourceSystems.contains(splitId[0])) {
            errors.put(SYSTEM_SOURCE, INVALID);
        }
        if (splitId.length == 1 || !StringUtils.hasText(splitId[1])) {
            errors.put(ID, EMPTY);
        }
        validateEmail(recipient.getEmail(), errors);
        if (StringUtils.hasText(recipient.getPhoneNumber())) {
            validatePhoneNumber(recipient.getPhoneNumber(), errors);
        }
        return errors;
    }
}
