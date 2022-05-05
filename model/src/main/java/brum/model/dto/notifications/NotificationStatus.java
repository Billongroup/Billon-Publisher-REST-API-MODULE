package brum.model.dto.notifications;

import brum.model.dto.recipients.ContactDetailsType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationStatus {
    private String jobId;
    private String clientId;
    private String contactDetails;
    private ContactDetailsType type;
    private NotificationStatusEnum status;
    private LocalDateTime sendingStartDate;
    private LocalDateTime deliveryDate;
    private List<NotificationError> errorsHistory;
}
