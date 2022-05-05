package brum.persistence.filters.mapper;

import brum.model.dto.common.SortOrder;
import org.springframework.data.domain.Sort;

public enum SortByMapper {
    INSTANCE;

    public Sort map(String sortBy, SortOrder order) {
        Sort.Direction direction;
        if (order == null || order.equals(SortOrder.ASCENDING)) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        return Sort.by(direction, sortBy);
    }
}
