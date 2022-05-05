package brum.model.dto.users;

import brum.common.enums.security.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Role {

    @JsonIgnore
    private Long id;
    @JsonProperty("id")
    private String externalId;
    private RoleEnum name;
    private Set<Privilege> rolePrivilege;

    public Role() {
        this.rolePrivilege = new HashSet<>();
    }

}
