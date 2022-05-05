package brum.remote.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDetails {
    private String jobId;
    private String subject;
    private String smsContent;
    private String emailContent;
}
