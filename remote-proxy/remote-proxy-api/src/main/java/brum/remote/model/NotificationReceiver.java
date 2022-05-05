package brum.remote.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationReceiver {
    private ReceiverType type;
    private String contactData;
}
