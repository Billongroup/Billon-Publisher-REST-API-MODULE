package brum.domain.notifications;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.notifications.*;

public interface GetNotificationsUC {
    PaginatedResponse<NotificationStatus> getDocumentNotificationHistory(String blockchainAddress, NotificationHistorySearchCriteria filters);
    PaginatedResponse<NotificationStatus> getDocumentNotificationStatus(String blockchainAddress, NotificationStatusFilters filters);
    NotificationContent getNotificationContent(String jobId, NotificationContentFilter filter);
}
