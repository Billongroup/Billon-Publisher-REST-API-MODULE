package brum.model.dto.notifications;

import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.SortOrder;
import lombok.Data;

import java.util.List;

@Data
public class NotificationStatusFilters {
    private PaginationFilter pagination;
    private SortOrder order;
    private List<String> clientIdList;
    private List<String> contactDetailsList;
    private List<NotificationStatusEnum> notificationStatusList;
}
