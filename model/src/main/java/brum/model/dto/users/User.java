package brum.model.dto.users;

import brum.common.enums.security.RoleEnum;
import brum.common.views.DocumentViews;
import brum.common.views.IdentityViews;
import brum.common.views.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @JsonIgnore
    private Long id;
    @JsonProperty("id")
    @JsonView({UserViews.GetList.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String externalId;
    @JsonView(UserViews.GetList.class)
    private User createdBy;
    @JsonView(UserViews.GetList.class)
    private LocalDateTime createdAt;
    @JsonView(UserViews.Get.class)
    private LocalDateTime passwordUpdatedAt;
    @JsonView(UserViews.Get.class)
    private LocalDateTime lastNotificationAt;
    @JsonView(UserViews.GetList.class)
    private UserStatus status;
    @JsonView({UserViews.Login.class, UserViews.Add.class, UserViews.GetList.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String username;
    @JsonView({UserViews.Login.class, UserViews.Password.class})
    private String password;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class})
    private RoleEnum role;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String firstName;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class, DocumentViews.GetDocumentList.class, IdentityViews.GetIdentity.class})
    private String lastName;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class})
    private String phoneNumber;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class, UserViews.ResetPassword.class})
    private String email;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class})
    private String department;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class, UserViews.Modify.class})
    private Boolean isActive;
    @JsonView(UserViews.Password.class)
    private String smsCode;
    private LocalDateTime smsCodeGenerationTime;
    @JsonView(UserViews.Add.class)
    private Boolean notify;
    @JsonView(UserViews.Get.class)
    private List<UserHistory> history;
    private List<String> passwordHistory;
    @JsonView({UserViews.Add.class, UserViews.Get.class, UserViews.GetList.class})
    private Boolean isRobot;
    @JsonIgnore
    private Integer failedLoginAttempts;
    @JsonIgnore
    private LocalDateTime blockedSince;

}
