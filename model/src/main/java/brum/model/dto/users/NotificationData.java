package brum.model.dto.users;

import lombok.Data;

@Data
public class NotificationData {
    private NotificationType type;
    private String userId;
}
