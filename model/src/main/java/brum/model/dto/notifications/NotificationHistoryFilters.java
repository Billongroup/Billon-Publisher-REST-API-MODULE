package brum.model.dto.notifications;

import lombok.Data;

import java.util.List;

@Data
public class NotificationHistoryFilters {
    private String clientId;
    private String contactDetails;
    private List<NotificationStatusEnum> notificationStatusList;
}
