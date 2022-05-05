package brum.model.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"message", "suppressed", "localizedMessage", "cause", "stackTrace"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BRUMGeneralException extends RuntimeException {
    @Getter
    private final ErrorStatusCode statusCode;
    @Getter
    private final LocalDateTime timestamp;

    protected BRUMGeneralException(ErrorStatusCode statusCode) {
        this.statusCode = statusCode;
        this.timestamp = null;
    }

    public BRUMGeneralException(ErrorStatusCode statusCode, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

}
