package brum.persistence.entity;

import brum.model.dto.identities.IdentityStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "identities")
public class IdentityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;
    @Enumerated(EnumType.STRING)
    private IdentityStatus status;
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String email;
    private String phoneNumber;
    private String additionalInformation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Boolean isActive;
    private Boolean isGdprSuspended;
}
