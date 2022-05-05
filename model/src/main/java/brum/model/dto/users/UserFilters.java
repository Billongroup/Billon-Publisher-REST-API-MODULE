package brum.model.dto.users;

import brum.common.enums.security.RoleEnum;
import brum.model.dto.common.DateRange;
import lombok.Data;

@Data
public class UserFilters {
    private String id;
    private String username;
    private RoleEnum role;
    private String createdBy;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private DateRange createdAt;
    private Boolean isRobot;
}
