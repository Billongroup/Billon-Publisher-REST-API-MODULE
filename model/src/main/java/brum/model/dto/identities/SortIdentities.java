package brum.model.dto.identities;

import brum.model.dto.common.SortOrder;
import lombok.Data;

@Data
public class SortIdentities {
    private SortOrder order;
    private SortIdentitiesBy sortBy;
}
