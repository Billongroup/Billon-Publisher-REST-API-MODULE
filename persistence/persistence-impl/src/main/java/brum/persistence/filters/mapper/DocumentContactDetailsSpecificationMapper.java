package brum.persistence.filters.mapper;

import brum.model.dto.recipients.*;
import brum.persistence.filters.specification.DocumentContactDetailsSpecification;
import org.springframework.util.StringUtils;

import java.util.*;

import static brum.persistence.filters.specification.Operation.*;

public class DocumentContactDetailsSpecificationMapper {
    private DocumentContactDetailsSpecificationMapper() {}

    private static final List<DocumentNotificationStatus> NEW_RECIPIENTS_STATUSES =
            Arrays.asList(DocumentNotificationStatus.NEW, DocumentNotificationStatus.UPDATED);

    public static List<DocumentContactDetailsSpecification> mapToSpecification(DocumentRecipientFilters filters, String blockchainAddress) {
        List<DocumentContactDetailsSpecification> specifications = new ArrayList<>();
        specifications.add(new DocumentContactDetailsSpecification("document.blockchainAddress", EQUAL, blockchainAddress));
        if (filters == null) {
            return specifications;
        }
        if (StringUtils.hasText(filters.getRecipientId())) {
            specifications.add(new DocumentContactDetailsSpecification("contactDetails.recipient.externalId", LIKE, filters.getRecipientId()));
        }
        if (StringUtils.hasText(filters.getContactDetails())) {
            specifications.add(new DocumentContactDetailsSpecification("contactDetails.value", LIKE, filters.getContactDetails()));
        }
        if (filters.getGroups() != null && !filters.getGroups().isEmpty()) {
            specifications.add(new DocumentContactDetailsSpecification("status", IN, mapGroupsToStatusList(filters.getGroups())));
        }
        return specifications;
    }

    public static List<DocumentContactDetailsSpecification> mapToSpecification(Map<String, List<ContactDetailsType>> idContactDetailsTypeMap, String blockchainAddress) {
        List<DocumentContactDetailsSpecification> specifications = new ArrayList<>();
        specifications.add(new DocumentContactDetailsSpecification("document.blockchainAddress", EQUAL, blockchainAddress));
        if (idContactDetailsTypeMap == null || idContactDetailsTypeMap.isEmpty()) {
            return specifications;
        }
        specifications.add(new DocumentContactDetailsSpecification("contactDetails.recipient.externalId", "contactDetails.type", MAP_IN, idContactDetailsTypeMap));
        return specifications;
    }

    private static List<DocumentNotificationStatus> mapGroupsToStatusList(List<RecipientGroup> groups) {
        if (groups.contains(RecipientGroup.NEW)) {
            return NEW_RECIPIENTS_STATUSES;
        } else {
            return Collections.singletonList(DocumentNotificationStatus.NOTIFICATION_SENT);
        }
    }
}
