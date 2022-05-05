package brum.domain.validator;

import brum.model.dto.users.User;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;

import java.util.EnumMap;
import java.util.Map;

import static brum.domain.validator.CommonValidator.*;
import static brum.model.exception.validation.InvalidField.*;
import static brum.model.exception.validation.ValidationErrorType.EMPTY;

public class UserValidator {
    private UserValidator() {}

    public static Map<InvalidField, ValidationErrorType> validateUser(User user) {
        Map<InvalidField, ValidationErrorType> errors = new EnumMap<>(InvalidField.class);
        if (user == null) {
            errors.put(USER, EMPTY);
            return errors;
        }
        if (emptyString(user.getUsername())) {
            errors.put(USERNAME, EMPTY);
        }
        if (user.getRole() == null) {
            errors.put(ROLE, EMPTY);
        }
        if (emptyString(user.getFirstName())) {
            errors.put(FIRST_NAME, EMPTY);
        }
        if (emptyString(user.getLastName())) {
            errors.put(LAST_NAME, EMPTY);
        }
        validateEmail(user.getEmail(), errors);
        validatePhoneNumber(user.getPhoneNumber(), errors);
        if (emptyString(user.getDepartment())) {
            errors.put(DEPARTMENT, EMPTY);
        }
        if (user.getIsActive() == null) {
            errors.put(IS_ACTIVE, EMPTY);
        }
        if (user.getNotify() == null) {
            errors.put(NOTIFY, EMPTY);
        }
        return errors;
    }
}
