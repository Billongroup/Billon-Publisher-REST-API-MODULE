package brum.model.dto.identities;

import brum.model.dto.common.PaginationFilter;
import lombok.Data;

@Data
public class IdentitySearchCriteria {
    private PaginationFilter pagination;
    private SortIdentities sort;
    private IdentityFilters filters;
}
