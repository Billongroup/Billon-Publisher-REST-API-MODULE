package brum.common.enums.security;

public enum PrivilegeEnum {
    // Util privileges
    GET_SMS_CODE,
    GET_REFRESH_TOKEN,
    GET_SETTINGS,

    // User privileges
    GET_USERS,
    GET_USER,
    POST_USER,
    PATCH_USER,
    PATCH_PASSWORD,
    GET_NOTIFICATION,

    // Category privileges
    GET_CATEGORY,
    GET_CATEGORIES,
    POST_CATEGORY,
    PATCH_CATEGORY,

    // Identity privileges
    GET_IDENTITIES,
    GET_IDENTITY,
    GET_IDENTITY_FILE,
    POST_IDENTITY,
    POST_IDENTITY_FILE,
    PATCH_IDENTITY,
    DELETE_IDENTITY,
    PATCH_IDENTITY_GDPR_SUSPENDED,

    // Document privileges
    GET_DOCUMENT_LIST,
    GET_DOCUMENT,
    POST_PREPARE_PUBLIC_DOCUMENT,
    POST_PUBLISH_PUBLIC_DOCUMENT,
    POST_PREPARE_PRIVATE_DOCUMENT,
    POST_PUBLISH_PRIVATE_DOCUMENT,
    GET_DOWNLOAD_DOCUMENT,
    GET_DOWNLOAD_RECIPIENTS_FILE,
    PATCH_RECIPIENTS_FILE,
    DELETE_DISCARD_DOCUMENT,
    DELETE_FORGET_DOCUMENT,
    GET_RESEND_AUTHORIZATION_CODES,
    POST_RESEND_NOTIFICATIONS,
    GET_DOCUMENT_RECIPIENTS,
    GET_DOWNLOAD_DOCUMENTS_REPORT,
    GET_NOTIFICATIONS_HISTORY,

    // Report privileges
    GET_YEARLY_REPORT,
    GET_FAIR_USAGE_REPORT,
    GET_CUSTOM_REPORT,
    GET_DOWNLOAD_STATISTICS_REPORT,

    // Recipient privileges
    POST_FILE_VALIDITY,

    // Notification privileges
    GET_NOTIFICATION_CONTENT
}
