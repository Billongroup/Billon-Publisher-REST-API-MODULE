package brum.model.dto.notifications;

import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.Sort;
import lombok.Data;

import java.util.List;

@Data
public class NotificationHistorySearchCriteria {
    private PaginationFilter pagination;
    private Sort sort;
    private NotificationHistoryFilters filters;
}
