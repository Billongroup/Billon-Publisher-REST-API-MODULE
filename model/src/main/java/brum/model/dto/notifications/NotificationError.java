package brum.model.dto.notifications;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationError {
    private String reason;
    private LocalDateTime sendingStartDate;
    private LocalDateTime errorDate;
}
