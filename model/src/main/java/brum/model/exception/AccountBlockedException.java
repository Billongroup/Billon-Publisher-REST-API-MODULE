package brum.model.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountBlockedException extends BRUMGeneralException {

    @Getter
    private final LocalDateTime unblockDate;

    public AccountBlockedException(LocalDateTime unblockDate, LocalDateTime timestamp) {
        super(ErrorStatusCode.ACCOUNT_TEMPORARY_BLOCKED, timestamp);
        this.unblockDate = unblockDate;
    }
}
