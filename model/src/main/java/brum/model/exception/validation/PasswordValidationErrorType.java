package brum.model.exception.validation;

public enum PasswordValidationErrorType {
    PASSWORD_CHANGED_TOO_OFTEN,
    PASSWORD_PREVIOUSLY_USED,
    PASSWORD_EMPTY,
    PASSWORD_TOO_SHORT,
    PASSWORD_NO_UNIQUE_CHARACTERS
}
