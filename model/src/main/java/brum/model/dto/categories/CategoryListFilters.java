package brum.model.dto.categories;

import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.SortOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryListFilters extends CategoryFilters {
    private PaginationFilter pagination;
    private SortOrder order;
    private String name;
    private Long parentId;
}
