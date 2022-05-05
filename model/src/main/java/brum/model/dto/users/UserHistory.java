package brum.model.dto.users;

import brum.common.enums.security.RoleEnum;
import brum.common.views.UserViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserHistory {
    @JsonView(UserViews.Get.class)
    private User updatedBy;
    @JsonView(UserViews.Get.class)
    private LocalDateTime updatedAt;
    @JsonView(UserViews.Get.class)
    private UserStatus status;
    @JsonView(UserViews.Get.class)
    private RoleEnum role;
    @JsonView(UserViews.Get.class)
    private String firstName;
    @JsonView(UserViews.Get.class)
    private String lastName;
    @JsonView(UserViews.Get.class)
    private String phoneNumber;
    @JsonView(UserViews.Get.class)
    private String email;
    @JsonView(UserViews.Get.class)
    private String department;
    @JsonView(UserViews.Get.class)
    private Boolean isActive;
}
