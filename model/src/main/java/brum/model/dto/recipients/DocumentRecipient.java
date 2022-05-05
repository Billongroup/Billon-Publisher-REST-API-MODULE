package brum.model.dto.recipients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DocumentRecipient {
    @JsonIgnore
    private Long contactDetailsId;
    private String externalId;
    private String contactDetails;
    private ContactDetailsType type;
    private DocumentRecipientStatus status;
}
