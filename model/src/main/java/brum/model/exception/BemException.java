package brum.model.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"message", "suppressed", "localizedMessage", "cause", "stackTrace"})
public class BemException extends BRUMGeneralException {
    @Getter
    @JsonProperty("statusCode")
    private final String bemStatusCode;


    public BemException(String bemErrorCode, LocalDateTime timestamp) {
        super(ErrorStatusCode.BEM_ERROR, timestamp);
        this.bemStatusCode = bemErrorCode;
    }
}
