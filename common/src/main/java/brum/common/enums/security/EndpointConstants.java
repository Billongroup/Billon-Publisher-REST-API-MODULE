package brum.common.enums.security;

public class EndpointConstants {

    private EndpointConstants() {}

    private static final String ID_PART = "/{id}";

    public static class UserControllerConstants {
        private UserControllerConstants() {}
        public static final String TAG = "Users";
        public static final String USER_URI = "/users";
        public static final String USER_WITH_ID = USER_URI + ID_PART;
        public static final String PASSWORD_URI = USER_URI + "/password";
        public static final String NOTIFICATION_URI = USER_URI + "/notification";
        public static final String RESET_PASSWORD_URI = USER_URI + "/resetPassword";

        public static final String GET_USERS_DESCRIPTION = "Get users list";
        public static final String GET_USER_DESCRIPTION = "Get user details";
        public static final String POST_USER_DESCRIPTION = "Register new user";
        public static final String PATCH_USER_DESCRIPTION = "Modify user's data";
        public static final String PATCH_PASSWORD_DESCRIPTION = "Modify password";
        public static final String GET_NOTIFICATION_DESCRIPTION = "Send notification and perform connected actions";
        public static final String GET_RESET_PASSWORD_DESCRIPTION = "Send notification and perform connected actions";
    }

    public static class UtilControllerConstants {
        private UtilControllerConstants() {}
        public static final String TAG = "Utility";
        public static final String UTIL_URI = "/utils";
        public static final String LOGIN_URI = UTIL_URI + "/login";
        public static final String HELLO_URI = UTIL_URI + "/hello";
        public static final String SETTINGS_URI = UTIL_URI + "/settings";
        public static final String SMS_CODE_URI = UTIL_URI + "/smsCode";
        public static final String REFRESH_TOKEN_URI = UTIL_URI + "/refreshToken";

        public static final String LOGIN_DESCRIPTION = "Login to acquire JWT token";
        public static final String GET_HELLO_DESCRIPTION = "Check connection";
        public static final String GET_SETTINGS_DESCRIPTION = "Get settings";
        public static final String GET_SMS_CODE_DESCRIPTION = "Get sms code for additional authentication";
        public static final String GET_REFRESH_TOKEN_DESCRIPTION = "Get new JWT token if not past refresh expiration time";
    }

    public static class CategoryControllerConstants {
        private CategoryControllerConstants() {}
        public static final String TAG = "Categories";
        public static final String CATEGORY_URI = "/categories";
        public static final String CATEGORY_WITH_ID_URI = CATEGORY_URI + ID_PART;

        public static final String GET_CATEGORY_DESCRIPTION = "Get category";
        public static final String GET_CATEGORIES_DESCRIPTION = "Get category list";
        public static final String POST_CATEGORY_DESCRIPTION = "Add category";
        public static final String PATCH_CATEGORY_DESCRIPTION = "Modify category activity";

    }

    public static class IdentityControllerConstants {
        private IdentityControllerConstants() {}
        public static final String TAG = "Identities";
        public static final String IDENTITY_URI = "/identities";
        public static final String IDENTITY_WITH_ID_URI = IDENTITY_URI + ID_PART;
        public static final String IDENTITY_FILE_URI = IDENTITY_URI + "/file";
        public static final String IDENTITY_GDPR_SUSPENDED_URI = IDENTITY_WITH_ID_URI + "/gdprSuspended";

        public static final String GET_IDENTITIES_DESCRIPTION = "Get identity list";
        public static final String GET_IDENTITY_DESCRIPTION = "Get identity";
        public static final String GET_IDENTITY_FILE_DESCRIPTION = "Generate identities report";
        public static final String POST_IDENTITY_DESCRIPTION = "Add identity";
        public static final String POST_IDENTITY_FILE_DESCRIPTION = "Add identities from file";
        public static final String PATCH_IDENTITY_DESCRIPTION = "Modify identity";
        public static final String DELETE_IDENTITY_DESCRIPTION = "Delete identity by id";
        public static final String PATCH_IDENTITY_GDPR_SUSPENDED_DESCRIPTION = "Suspend identity according to gdpr";
    }

    public static class DocumentControllerConstants {
        private DocumentControllerConstants() {}
        public static final String TAG = "Documents";
        public static final String DOCUMENT_URI = "/documents";
        public static final String DOCUMENT_WITH_ID_URI = DOCUMENT_URI + ID_PART;
        public static final String PUBLIC_DOCUMENT_URI_PART = "/public";
        public static final String PUBLIC_DOCUMENT_URI = DOCUMENT_URI + PUBLIC_DOCUMENT_URI_PART;
        public static final String PUBLISH_PUBLIC_DOCUMENT_URI = DOCUMENT_WITH_ID_URI + PUBLIC_DOCUMENT_URI_PART + "/publish";
        public static final String PRIVATE_DOCUMENT_URI_PART = "/private";
        public static final String PRIVATE_DOCUMENT_URI = DOCUMENT_URI + PRIVATE_DOCUMENT_URI_PART;
        public static final String PUBLISH_PRIVATE_DOCUMENT_URI = DOCUMENT_WITH_ID_URI + PRIVATE_DOCUMENT_URI_PART + "/publish";
        public static final String DOCUMENT_FILE_URI = DOCUMENT_WITH_ID_URI + "/file";
        public static final String RECIPIENTS_FILE_URI = DOCUMENT_WITH_ID_URI + "/recipientsFile";
        public static final String FORGET_DOCUMENT_URI = DOCUMENT_WITH_ID_URI + "/forget";
        public static final String RESEND_AUTHORIZATION_CODES_URI = DOCUMENT_WITH_ID_URI + "/reauthorize";
        public static final String RESEND_NOTIFICATIONS_URI = DOCUMENT_WITH_ID_URI + "/notification";
        public static final String DOCUMENT_RECIPIENTS_URI = DOCUMENT_WITH_ID_URI + "/recipients";
        public static final String DOCUMENTS_REPORT_URI = DOCUMENT_URI + "/documentsReportFile";
        public static final String NOTIFICATIONS_HISTORY_URI = DOCUMENT_WITH_ID_URI + "/notificationHistory";
        public static final String DOCUMENT_TREE_URI = DOCUMENT_WITH_ID_URI + "/tree";

        public static final String GET_DOCUMENT_LIST_DESCRIPTION = "Get filtered documents";
        public static final String GET_DOCUMENT_DESCRIPTION = "Get document details";
        public static final String PATCH_DOCUMENT_DESCRIPTION = "Update document data";
        public static final String TREE_DOCUMENT_DESCRIPTION = "Get tree for the document";
        public static final String POST_ADD_PUBLIC_DOCUMENT_DESCRIPTION = "Add new public document";
        public static final String POST_PUBLISH_PUBLIC_DOCUMENT_DESCRIPTION = "Publish public document";
        public static final String POST_ADD_PRIVATE_DOCUMENT_DESCRIPTION = "Add new private document";
        public static final String POST_PUBLISH_PRIVATE_DOCUMENT_DESCRIPTION = "Publish private document";
        public static final String GET_DOWNLOAD_DOCUMENT_DESCRIPTION = "Download document file";
        public static final String GET_DOWNLOAD_RECIPIENTS_FILE_DESCRIPTION = "Download public document's recipients file";
        public static final String PATCH_RECIPIENTS_FILE_DESCRIPTION = "Update document recipients from file";
        public static final String DELETE_DISCARD_DOCUMENT_DESCRIPTION = "Discard document by jobId";
        public static final String DELETE_FORGET_DOCUMENT_DESCRIPTION = "Forget document by blockchain address";
        public static final String GET_RESEND_AUTHORIZATION_CODES_DESCRIPTION = "Resend authorization codes by blockchain address";
        public static final String POST_RESEND_NOTIFICATIONS_DESCRIPTION = "Resend document notification by specific recipients or recipient group";
        public static final String GET_DOCUMENT_RECIPIENTS_DESCRIPTION = "Get filtered document recipient list";
        public static final String GET_DOWNLOAD_DOCUMENTS_REPORT_DESCRIPTION = "Download filtered documents report";
        public static final String GET_NOTIFICATIONS_HISTORY_DESCRIPTION = "Download notification history";
    }

    public static class ReportControllerConstants {
        private ReportControllerConstants() {}
        public static final String TAG = "Reports";
        public static final String REPORT_URI = "/reports";
        public static final String YEARLY_REPORT_URI = REPORT_URI + "/yearly";
        public static final String FAIR_USAGE_REPORT_URI = REPORT_URI + "/fairUsage";
        public static final String CUSTOM_REPORT_URI = REPORT_URI + "/custom";
        public static final String DOWNLOAD_STATISTICS_REPORT_URI = REPORT_URI + "/statisticsFile";

        public static final String GET_YEARLY_REPORT_DESCRIPTION = "Get yearly report";
        public static final String GET_FAIR_USAGE_REPORT_DESCRIPTION = "Get fair usage report";
        public static final String GET_CUSTOM_REPORT_DESCRIPTION = "Get custom report";
        public static final String GET_DOWNLOAD_STATISTICS_REPORT_DESCRIPTION = "Download reports as excel";
    }

    public static class RecipientControllerConstants {
        private RecipientControllerConstants() {}
        public static final String TAG = "Recipients";
        public static final String RECIPIENT_URI = "/recipients";
        public static final String FILE_VALIDITY_URI = RECIPIENT_URI + "/validity";

        public static final String POST_FILE_VALIDITY_DESCRIPTION = "Validate file with recipients";
    }

    public static class NotificationControllerConstants {
        private NotificationControllerConstants() {}
        public static final String TAG = "Notifications";
        public static final String NOTIFICATION_URI = "/notifications";
        public static final String NOTIFICATION_WITH_ID_URI = NOTIFICATION_URI + ID_PART;
        public static final String NOTIFICATION_CONTENT_URI = NOTIFICATION_WITH_ID_URI + "/content";

        public static final String GET_NOTIFICATION_CONTENT_DESCRIPTION = "Get notification content";
    }
}
