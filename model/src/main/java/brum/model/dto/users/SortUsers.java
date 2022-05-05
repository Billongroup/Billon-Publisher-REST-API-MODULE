package brum.model.dto.users;

import brum.model.dto.common.SortOrder;
import lombok.Data;

@Data
public class SortUsers {
    private SortOrder order;
    private SortUsersBy sortBy;
}
