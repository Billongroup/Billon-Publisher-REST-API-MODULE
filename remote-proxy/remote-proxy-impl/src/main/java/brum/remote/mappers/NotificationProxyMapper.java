package brum.remote.mappers;

import brum.remote.model.NotificationDetails;
import brum.remote.model.NotificationReceiver;
import https.notificationinterface_dm_billongroup.Client;
import https.notificationinterface_dm_billongroup.NotificationType;
import https.notificationinterface_dm_billongroup.PrepareNotificationRequest;
import https.notificationinterface_dm_billongroup.SendNotificationRequest;

import java.util.List;

public class NotificationProxyMapper {
    private NotificationProxyMapper() {}

    public static PrepareNotificationRequest map(List<NotificationReceiver> notificationReceivers) {
        PrepareNotificationRequest result = new PrepareNotificationRequest();
        for (NotificationReceiver receiver : notificationReceivers) {
            Client client = new Client();
            client.setNotificationType(NotificationType.valueOf(receiver.getType().name()));
            client.setClientContactData(receiver.getContactData());
            result.getClient().add(client);
        }
        return result;
    }

    public static SendNotificationRequest map(NotificationDetails notification) {
        SendNotificationRequest result = new SendNotificationRequest();
        result.setJobId(notification.getJobId());
        result.setSubject(notification.getSubject());
        result.setSmsContent(notification.getSmsContent());
        result.setEmailContent(notification.getEmailContent());
        return result;
    }
}
