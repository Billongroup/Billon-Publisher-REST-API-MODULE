package brum.model.dto.users;

import brum.model.dto.common.PaginationFilter;
import lombok.Data;

@Data
public class UserSearchCriteria {
    private PaginationFilter pagination;
    private SortUsers sort;
    private UserFilters filters;
}
