package brum.persistence.entity;

import brum.model.dto.users.UserStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordUpdatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastNotificationAt;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String username;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String department;
    private Boolean isActive;
    private String smsCode;
    private Boolean isRobot;
    private Integer failedLoginAttempts;
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockedSince;
    @Temporal(TemporalType.TIMESTAMP)
    private Date smsCodeGenerationTime;
    @OneToMany(mappedBy = "currentUser", fetch = FetchType.LAZY)
    private List<UserHistoryEntity> history;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PasswordHistoryEntity> passwordHistory;

}
