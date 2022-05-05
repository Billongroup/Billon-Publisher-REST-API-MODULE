package brum.model.dto.common;

import lombok.Getter;

import java.io.Serializable;

public enum ParameterKey {
    NOTIFICATION_SERVICE_URL(String.class),
    JWT_EXPIRATION_TIME(Long.class),
    JWT_REFRESH_EXPIRATION_TIME(Long.class),
    JWT_PATCH_PASSWORD_EXPIRATION_TIME(Long.class),
    ONE_STEP_PUBLISH(Boolean.class),
    PASSWORD_EXPIRATION_TIME(Long.class),
    FRONT_END_URL(String.class),
    SMS_CODE_EXPIRATION_TIME(Long.class),
    SMS_CODE_CONTENT(String.class),
    SET_PASSWORD_EMAIL_TITLE(String.class),
    SET_PASSWORD_EMAIL_CONTENT(String.class),
    SET_PASSWORD_URL_PREFIX(String.class),
    RESET_PASSWORD_NOTIFICATION_TITLE(String.class),
    RESET_PASSWORD_EMAIL_CONTENT(String.class),
    RESET_PASSWORD_URL_PREFIX(String.class),
    PASSWORD_EXPIRED_TITLE(String.class),
    PASSWORD_EXPIRED_EMAIL_CONTENT(String.class),
    DOCUMENT_PREVIEW_URL(String.class),
    PUBLISHER_NAME(String.class),
    ADMIN_PASSWORD_LENGTH(Long.class),
    ADMIN_UNIQUE_PASSWORDS(Long.class),
    USER_PASSWORD_LENGTH(Long.class),
    USER_UNIQUE_PASSWORDS(Long.class),
    HOURS_TO_CHANGE_PASSWORD(Long.class),
    ADMIN_PASSWORD_COMPLEXITY_GROUPS(Long.class),
    USER_PASSWORD_COMPLEXITY_GROUPS(Long.class),
    FAILED_LOGIN_BLOCK_TIME(Long.class),
    MAX_FAILED_LOGIN_ATTEMPTS(Long.class),
    RECIPIENT_SOURCE_SYSTEM_NAMES(String.class);

    @Getter
    private final Class<? extends Serializable> clazz;

    ParameterKey(Class<? extends Serializable> clazz) {
        this.clazz = clazz;
    }
}
