package brum.model.dto.users;

import brum.common.enums.security.PrivilegeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {

    @JsonIgnore
    private Long id;
    @JsonProperty("id")
    private String externalId;
    private PrivilegeEnum name;
    private String description;

}
