package brum.model.dto.recipients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recipient {
    @JsonIgnore
    private Long id;
    @JsonProperty("id")
    private String externalId;
    private String email;
    private String phoneNumber;
}
