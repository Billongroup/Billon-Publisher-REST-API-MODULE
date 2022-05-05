package brum.persistence.entity;

import brum.model.dto.users.UserStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "users_history")
public class UserHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity currentUser;
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String department;
    private Boolean isActive;
}
