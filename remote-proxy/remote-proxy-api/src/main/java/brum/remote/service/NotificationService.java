package brum.remote.service;

import brum.remote.model.NotificationReceiver;
import brum.remote.model.NotificationDetails;

import java.util.List;

public interface NotificationService {
    String prepareNotification(List<NotificationReceiver> notificationReceivers);
    void sendNotification(NotificationDetails notificationDetails);
}
