package brum.model.dto.recipients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDetails {
    @JsonIgnore
    private Long id;
    private String value;
    private ContactDetailsType type;
    private DocumentNotificationStatus status;

    public ContactDetails(String value, ContactDetailsType type) {
        this.value = value;
        this.type = type;
    }
}
