package brum.persistence.entity;

import brum.common.enums.security.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(exclude = "rolePrivilege")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
    @ManyToMany
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Set<PrivilegeEntity> rolePrivilege = new HashSet<>();

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", name=" + name +
                '}';
    }
}
