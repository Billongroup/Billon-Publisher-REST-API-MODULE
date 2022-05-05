package brum.model.dto.recipients;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RecipientsToNotify {
    private Map<String, List<ContactDetailsType>> idContactTypeMap = new HashMap<>();
    private List<RecipientGroup> groups = new ArrayList<>();
}
