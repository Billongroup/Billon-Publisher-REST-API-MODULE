package brum.model.dto.recipients;

import lombok.Data;

import java.util.List;

@Data
public class DocumentRecipientFilters {
    private String recipientId;
    private String contactDetails;
    private List<RecipientGroup> groups;
}
