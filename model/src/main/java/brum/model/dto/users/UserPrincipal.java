package brum.model.dto.users;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPrincipal {
    private UserStatus status;
    private LocalDateTime passwordUpdatedAt;
    private String username;
    private String password;
    private Role role;
    private Boolean isActive;
    private Integer failedLoginAttempts;
    private LocalDateTime blockedSince;
    private String department;
}
