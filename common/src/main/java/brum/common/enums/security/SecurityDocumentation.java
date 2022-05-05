package brum.common.enums.security;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static brum.common.enums.security.EndpointConstants.*;
import static brum.common.enums.security.HttpMethodEnum.*;
import static brum.common.enums.security.PrivilegeEnum.*;
import static brum.common.enums.security.RoleEnum.*;

public class SecurityDocumentation {
    private SecurityDocumentation() {}

    /**
     * kolejność wpisów ma znaczenie, np:
     * users/{id} admin, jest zamienione na users/* tylko dla admina
     * jeśli po tym pojawi się users/setPassword permitAll, to taki wpis będzie zignorowany, ponieważ spełnia już matcher dla users/*
     * żeby to działało, najpierw musi być dodany bardziej szczegółowy wpis (users/setPassword) a dopiero po nim wpisy z wildcard w url (users/*)
     */

    public static List<Entry> getDocumentation() {
        return new ArrayList<>(Arrays.asList(
                // user endpoints
                new Entry(UserControllerConstants.RESET_PASSWORD_URI, UserControllerConstants.GET_RESET_PASSWORD_DESCRIPTION, GET),
                new Entry(UserControllerConstants.NOTIFICATION_URI, UserControllerConstants.GET_NOTIFICATION_DESCRIPTION,
                        GET, GET_NOTIFICATION, ADMIN),
                new Entry(UserControllerConstants.PASSWORD_URI, UserControllerConstants.PATCH_PASSWORD_DESCRIPTION,
                        PATCH, PATCH_PASSWORD),
                new Entry(UserControllerConstants.USER_URI, UserControllerConstants.GET_USERS_DESCRIPTION,
                        GET, GET_USERS, ADMIN),
                new Entry(UserControllerConstants.USER_WITH_ID, UserControllerConstants.GET_USER_DESCRIPTION,
                        GET, GET_USER, ADMIN),
                new Entry(UserControllerConstants.USER_URI, UserControllerConstants.POST_USER_DESCRIPTION,
                        POST, POST_USER, ADMIN),
                new Entry(UserControllerConstants.USER_WITH_ID, UserControllerConstants.PATCH_USER_DESCRIPTION,
                        PATCH, PATCH_USER, ADMIN),

                // utility endpoints
                new Entry(UtilControllerConstants.LOGIN_URI, UtilControllerConstants.LOGIN_DESCRIPTION, POST),
                new Entry(UtilControllerConstants.HELLO_URI, UtilControllerConstants.GET_HELLO_DESCRIPTION, GET),
                new Entry(UtilControllerConstants.SETTINGS_URI, UtilControllerConstants.GET_SETTINGS_DESCRIPTION,
                        GET, GET_SETTINGS),
                new Entry(UtilControllerConstants.SMS_CODE_URI, UtilControllerConstants.GET_SMS_CODE_DESCRIPTION,
                        GET, GET_SMS_CODE),
                new Entry(UtilControllerConstants.REFRESH_TOKEN_URI, UtilControllerConstants.GET_REFRESH_TOKEN_DESCRIPTION,
                        GET, GET_REFRESH_TOKEN),

                // category endpoints
                new Entry(CategoryControllerConstants.CATEGORY_WITH_ID_URI, CategoryControllerConstants.GET_CATEGORY_DESCRIPTION,
                        GET, GET_CATEGORY),
                new Entry(CategoryControllerConstants.CATEGORY_URI, CategoryControllerConstants.GET_CATEGORIES_DESCRIPTION,
                        GET, GET_CATEGORIES),
                new Entry(CategoryControllerConstants.CATEGORY_URI, CategoryControllerConstants.POST_CATEGORY_DESCRIPTION,
                        POST, POST_CATEGORY, ADMIN, AGENT),
                new Entry(CategoryControllerConstants.CATEGORY_WITH_ID_URI, CategoryControllerConstants.PATCH_CATEGORY_DESCRIPTION,
                        PATCH, PATCH_CATEGORY, ADMIN, AGENT),

                // identity endpoints
                new Entry(IdentityControllerConstants.IDENTITY_FILE_URI, IdentityControllerConstants.GET_IDENTITY_FILE_DESCRIPTION,
                        GET, GET_IDENTITY_FILE),
                new Entry(IdentityControllerConstants.IDENTITY_FILE_URI, IdentityControllerConstants.POST_IDENTITY_FILE_DESCRIPTION,
                        POST, POST_IDENTITY_FILE, ADMIN, AGENT),
                new Entry(IdentityControllerConstants.IDENTITY_URI, IdentityControllerConstants.GET_IDENTITIES_DESCRIPTION,
                        GET, GET_IDENTITIES),
                new Entry(IdentityControllerConstants.IDENTITY_WITH_ID_URI, IdentityControllerConstants.GET_IDENTITY_DESCRIPTION,
                        GET, GET_IDENTITY),
                new Entry(IdentityControllerConstants.IDENTITY_URI, IdentityControllerConstants.POST_IDENTITY_DESCRIPTION,
                        POST, POST_IDENTITY, ADMIN, AGENT),
                new Entry(IdentityControllerConstants.IDENTITY_WITH_ID_URI, IdentityControllerConstants.PATCH_IDENTITY_DESCRIPTION,
                        PATCH, PATCH_IDENTITY, ADMIN, AGENT, DPI),
                new Entry(IdentityControllerConstants.IDENTITY_WITH_ID_URI, IdentityControllerConstants.DELETE_IDENTITY_DESCRIPTION,
                        DELETE, DELETE_IDENTITY, ADMIN, AGENT, DPI),
                new Entry(IdentityControllerConstants.IDENTITY_GDPR_SUSPENDED_URI, IdentityControllerConstants.PATCH_IDENTITY_GDPR_SUSPENDED_DESCRIPTION,
                        PATCH, PATCH_IDENTITY_GDPR_SUSPENDED, DPI),

                // document endpoints
                new Entry(DocumentControllerConstants.DOCUMENT_URI, DocumentControllerConstants.GET_DOCUMENT_LIST_DESCRIPTION,
                        GET, GET_DOCUMENT_LIST),
                new Entry(DocumentControllerConstants.PUBLIC_DOCUMENT_URI, DocumentControllerConstants.POST_ADD_PUBLIC_DOCUMENT_DESCRIPTION,
                        POST, POST_PREPARE_PUBLIC_DOCUMENT, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.PRIVATE_DOCUMENT_URI, DocumentControllerConstants.POST_ADD_PRIVATE_DOCUMENT_DESCRIPTION,
                        POST, POST_PREPARE_PRIVATE_DOCUMENT, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.DOCUMENTS_REPORT_URI, DocumentControllerConstants.GET_DOWNLOAD_DOCUMENTS_REPORT_DESCRIPTION,
                        GET, GET_DOWNLOAD_DOCUMENTS_REPORT),
                new Entry(DocumentControllerConstants.DOCUMENT_WITH_ID_URI, DocumentControllerConstants.GET_DOCUMENT_DESCRIPTION,
                        GET, GET_DOCUMENT),
                new Entry(DocumentControllerConstants.PUBLISH_PUBLIC_DOCUMENT_URI, DocumentControllerConstants.POST_PUBLISH_PUBLIC_DOCUMENT_DESCRIPTION,
                        POST, POST_PUBLISH_PUBLIC_DOCUMENT, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.PUBLISH_PRIVATE_DOCUMENT_URI, DocumentControllerConstants.POST_PUBLISH_PRIVATE_DOCUMENT_DESCRIPTION,
                        POST, POST_PUBLISH_PRIVATE_DOCUMENT, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.DOCUMENT_FILE_URI, DocumentControllerConstants.GET_DOWNLOAD_DOCUMENT_DESCRIPTION,
                        GET, GET_DOWNLOAD_DOCUMENT),
                new Entry(DocumentControllerConstants.RECIPIENTS_FILE_URI, DocumentControllerConstants.GET_DOWNLOAD_RECIPIENTS_FILE_DESCRIPTION,
                        GET, GET_DOWNLOAD_RECIPIENTS_FILE),
                new Entry(DocumentControllerConstants.RECIPIENTS_FILE_URI, DocumentControllerConstants.PATCH_RECIPIENTS_FILE_DESCRIPTION,
                        PATCH, PATCH_RECIPIENTS_FILE, AGENT, ADMIN),
                new Entry(DocumentControllerConstants.DOCUMENT_WITH_ID_URI, DocumentControllerConstants.DELETE_DISCARD_DOCUMENT_DESCRIPTION,
                        DELETE, DELETE_DISCARD_DOCUMENT, ADMIN, AGENT, DPI),
                new Entry(DocumentControllerConstants.FORGET_DOCUMENT_URI, DocumentControllerConstants.DELETE_FORGET_DOCUMENT_DESCRIPTION,
                        DELETE, DELETE_FORGET_DOCUMENT, DPI),
                new Entry(DocumentControllerConstants.RESEND_AUTHORIZATION_CODES_URI, DocumentControllerConstants.GET_RESEND_AUTHORIZATION_CODES_DESCRIPTION,
                        GET, GET_RESEND_AUTHORIZATION_CODES, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.DOCUMENT_RECIPIENTS_URI, DocumentControllerConstants.GET_DOCUMENT_RECIPIENTS_DESCRIPTION,
                        GET, GET_DOCUMENT_RECIPIENTS, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.RESEND_NOTIFICATIONS_URI, DocumentControllerConstants.POST_RESEND_NOTIFICATIONS_DESCRIPTION,
                        POST, POST_RESEND_NOTIFICATIONS, ADMIN, AGENT),
                new Entry(DocumentControllerConstants.NOTIFICATIONS_HISTORY_URI, DocumentControllerConstants.GET_NOTIFICATIONS_HISTORY_DESCRIPTION,
                        GET, GET_NOTIFICATIONS_HISTORY),

                // report endpoints
                new Entry(ReportControllerConstants.YEARLY_REPORT_URI, ReportControllerConstants.GET_YEARLY_REPORT_DESCRIPTION,
                        GET, GET_YEARLY_REPORT),
                new Entry(ReportControllerConstants.FAIR_USAGE_REPORT_URI, ReportControllerConstants.GET_FAIR_USAGE_REPORT_DESCRIPTION,
                        GET, GET_FAIR_USAGE_REPORT),
                new Entry(ReportControllerConstants.CUSTOM_REPORT_URI, ReportControllerConstants.GET_CUSTOM_REPORT_DESCRIPTION,
                        GET, GET_CUSTOM_REPORT),
                new Entry(ReportControllerConstants.DOWNLOAD_STATISTICS_REPORT_URI, ReportControllerConstants.GET_DOWNLOAD_STATISTICS_REPORT_DESCRIPTION,
                        GET, GET_DOWNLOAD_STATISTICS_REPORT),

                // recipient endpoints
                new Entry(RecipientControllerConstants.FILE_VALIDITY_URI, RecipientControllerConstants.POST_FILE_VALIDITY_DESCRIPTION,
                        POST, POST_FILE_VALIDITY),

                // notification endpoints
                new Entry(NotificationControllerConstants.NOTIFICATION_CONTENT_URI, NotificationControllerConstants.GET_NOTIFICATION_CONTENT_DESCRIPTION,
                        GET, GET_NOTIFICATION_CONTENT, ADMIN, AGENT)
        ));
    }

    @Data
    public static class Entry {
        private final String endpoint;
        private final String description;
        private final HttpMethodEnum httpMethod;
        private final PrivilegeEnum privilege;
        private final RoleEnum[] roles;

        // add privilege for endpoint to passed roles
        private Entry(String endpoint, String description, HttpMethodEnum httpMethod, PrivilegeEnum privilege, RoleEnum... roles) {
            this.endpoint = endpoint;
            this.description = description;
            this.httpMethod = httpMethod;
            this.privilege = privilege;
            this.roles = roles;
        }

        // used for authenticated with any role
        private Entry(String endpoint, String description, HttpMethodEnum httpMethod, PrivilegeEnum privilege) {
            this.endpoint = endpoint;
            this.description = description;
            this.httpMethod = httpMethod;
            this.privilege = privilege;
            this.roles = RoleEnum.values();
        }

        // user for permitAll
        private Entry(String endpoint, String description, HttpMethodEnum httpMethod) {
            this.endpoint = endpoint;
            this.description = description;
            this.httpMethod = httpMethod;
            this.privilege = null;
            this.roles = new RoleEnum[0];
        }

    }

}
