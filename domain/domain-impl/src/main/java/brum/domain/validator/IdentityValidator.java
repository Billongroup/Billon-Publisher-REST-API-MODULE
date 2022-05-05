package brum.domain.validator;

import brum.model.dto.identities.Identity;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;

import java.util.EnumMap;
import java.util.Map;

import static brum.domain.validator.CommonValidator.*;
import static brum.model.exception.validation.ValidationErrorType.*;
import static brum.model.exception.validation.InvalidField.*;

public class IdentityValidator {
    private IdentityValidator() {}

    public static Map<InvalidField, ValidationErrorType> validateIdentity(Identity identity) {
        Map<InvalidField, ValidationErrorType> errors = new EnumMap<>(InvalidField.class);
        if (identity == null) {
            errors.put(IDENTITY, EMPTY);
            return errors;
        }
        if (emptyString(identity.getFirstName())) {
            errors.put(FIRST_NAME, EMPTY);
        }
        if (emptyString(identity.getLastName())) {
            errors.put(LAST_NAME, EMPTY);
        }
        if (emptyString(identity.getDocumentNumber())) {
            errors.put(DOCUMENT_NUMBER, EMPTY);
        } else if (validatePesel(identity.getDocumentNumber())) {
            errors.put(DOCUMENT_NUMBER, INVALID);
        }
        validateEmail(identity.getEmail(), errors);
        validatePhoneNumber(identity.getPhoneNumber(), errors);
        return errors;
    }
}
