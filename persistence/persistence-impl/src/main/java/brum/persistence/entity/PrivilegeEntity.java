package brum.persistence.entity;

import brum.common.enums.security.PrivilegeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "privileges")
@EqualsAndHashCode(exclude = "privilegeRole")
public class PrivilegeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    @Enumerated(EnumType.STRING)
    private PrivilegeEnum name;
    private String description;
    @ManyToMany(mappedBy = "rolePrivilege")
    private Set<RoleEntity> privilegeRole = new HashSet<>();

    @Override
    public String toString() {
        return "PrivilegeEntity{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", name=" + name +
                ", description='" + description + '\'' +
                '}';
    }
}
