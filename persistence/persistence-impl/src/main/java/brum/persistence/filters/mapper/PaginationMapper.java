package brum.persistence.filters.mapper;

import brum.model.dto.common.PaginationFilter;
import org.springframework.data.domain.PageRequest;

public class PaginationMapper {
    private PaginationMapper() {
    }

    public static PageRequest map(PaginationFilter filter) {
        return PageRequest.of(
                filter == null || filter.getPage() == null || filter.getLimit() == null || filter.getLimit() == 0 ? 0 : filter.getPage(),
                filter == null || filter.getLimit() == null || filter.getLimit() == 0 ? Integer.MAX_VALUE : filter.getLimit()
        );
    }
}
