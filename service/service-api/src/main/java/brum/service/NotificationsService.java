package brum.service;

import brum.model.dto.notifications.NotificationContent;
import brum.model.dto.notifications.NotificationContentFilter;

public interface NotificationsService {
    NotificationContent getNotificationContent(String jobId, NotificationContentFilter filter);
}
