package brum.model.exception.validation;

public enum InvalidField {
    ID,

    // user
    USER,
    USERNAME,
    ROLE,
    DEPARTMENT,
    IS_ACTIVE,
    NOTIFY,
    CODE,

    // notification
    NOTIFICATION,
    NOTIFICATION_TYPE,

    // identity
    IDENTITY,
    FIRST_NAME,
    LAST_NAME,
    DOCUMENT_NUMBER,
    EMAIL,
    PHONE_NUMBER,
    IS_GDPR_SUSPENDED,

    // recipient
    RECIPIENT,
    SYSTEM_SOURCE,

    // document
    DOCUMENT,
    FAVOURITE,

    // report
    REPORT_TYPE
}
