package brum.model.dto.identities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityFilters {
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String email;
    private String phoneNumber;
}
