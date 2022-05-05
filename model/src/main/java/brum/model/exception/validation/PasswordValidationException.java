package brum.model.exception.validation;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class PasswordValidationException extends BRUMGeneralException {
    @Getter
    private List<PasswordValidationErrorType> reasons;

    public PasswordValidationException(List<PasswordValidationErrorType> reasons, LocalDateTime timestamp) {
        super(ErrorStatusCode.INVALID_PASSWORD, timestamp);
        this.reasons = reasons;
    }

    public PasswordValidationException(PasswordValidationErrorType reason, LocalDateTime timestamp) {
        super(ErrorStatusCode.INVALID_PASSWORD, timestamp);
        this.reasons = Collections.singletonList(reason);
    }
}
