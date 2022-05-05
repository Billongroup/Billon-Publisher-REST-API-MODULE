package brum.service.impl;

import brum.domain.notifications.GetNotificationsUC;
import brum.model.dto.notifications.NotificationContent;
import brum.model.dto.notifications.NotificationContentFilter;
import brum.service.NotificationsService;
import org.springframework.stereotype.Service;

@Service
public class NotificationsServiceImpl implements NotificationsService {
    private final GetNotificationsUC getNotificationsUC;

    public NotificationsServiceImpl(GetNotificationsUC getNotificationsUC) {
        this.getNotificationsUC = getNotificationsUC;
    }

    @Override
    public NotificationContent getNotificationContent(String jobId, NotificationContentFilter filter) {
        return getNotificationsUC.getNotificationContent(jobId, filter);
    }
}
