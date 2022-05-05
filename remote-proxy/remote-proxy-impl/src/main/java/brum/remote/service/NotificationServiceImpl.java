package brum.remote.service;

import brum.remote.mappers.NotificationProxyMapper;
import brum.remote.model.NotificationReceiver;
import brum.remote.model.NotificationDetails;
import brum.remote.proxy.NotificationRemoteProxy;
import https.notificationinterface_dm_billongroup.PrepareNotificationResponse;
import https.notificationinterface_dm_billongroup.PrepareNotificationStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRemoteProxy notificationProxy;

    public NotificationServiceImpl(NotificationRemoteProxy notificationProxy) {
        this.notificationProxy = notificationProxy;
    }

    @Override
    public String prepareNotification(List<NotificationReceiver> notificationReceivers) {
        PrepareNotificationResponse response = notificationProxy.prepareNotification(NotificationProxyMapper.map(notificationReceivers));
        if (response == null || !PrepareNotificationStatus.PREPARED_SUCCESSFULLY.equals(response.getStatus())) {
            return null;
        }
        return response.getJobId();
    }

    @Override
    public void sendNotification(NotificationDetails notificationDetails) {
        notificationProxy.sendNotification(NotificationProxyMapper.map(notificationDetails));
    }
}
