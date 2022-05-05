package brum.remote.proxy;

import brum.model.dto.common.ParameterKey;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.ParameterPersistenceGateway;
import https.notificationinterface_dm_billongroup.PrepareNotificationRequest;
import https.notificationinterface_dm_billongroup.PrepareNotificationResponse;
import https.notificationinterface_dm_billongroup.SendNotificationRequest;
import https.notificationinterface_dm_billongroup.SendNotificationResponse;
import https.notificationinterface_dm_billongroup_com.NotificationInterfaceServicePortType;
import https.notificationinterface_dm_billongroup_com.NotificationInterfaceServicePortTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;
import java.time.LocalDateTime;

@Service
public class NotificationRemoteProxy extends NotificationInterfaceServicePortTypeService {

    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private NotificationInterfaceServicePortType notificationService;

    public NotificationRemoteProxy(ParameterPersistenceGateway parameterPersistenceGateway) {
        this.parameterPersistenceGateway = parameterPersistenceGateway;
    }

    @PostConstruct
    protected void init() {
        try {
            notificationService = getNotificationInterfaceServicePortTypeSoap11();
            ((BindingProvider) notificationService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNotificationServiceUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrepareNotificationResponse prepareNotification(PrepareNotificationRequest in) {
        try {
            return notificationService.prepareNotification(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BRUMGeneralException(ErrorStatusCode.NOTIFICATION_SENDING_ERROR, LocalDateTime.now());
        }
    }

    public SendNotificationResponse sendNotification(SendNotificationRequest in) {
        try {
            return notificationService.sendNotification(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BRUMGeneralException(ErrorStatusCode.NOTIFICATION_SENDING_ERROR, LocalDateTime.now());
        }
    }

    private String getNotificationServiceUrl() {
        return parameterPersistenceGateway.getParameterValue(ParameterKey.NOTIFICATION_SERVICE_URL);
    }
}
