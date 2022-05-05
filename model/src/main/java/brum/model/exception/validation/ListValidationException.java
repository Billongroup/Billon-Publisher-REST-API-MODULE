package brum.model.exception.validation;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ListValidationException extends BRUMGeneralException {
    @Getter
    private final int success;
    @Getter
    private final int total;
    @Getter
    private final List<ValidationException> validationErrors;

    public ListValidationException(int success, int total, List<ValidationException> validationErrors, LocalDateTime timestamp) {
        super(ErrorStatusCode.VALIDATION_ERROR, timestamp);
        this.success = success;
        this.total = total;
        this.validationErrors = validationErrors;
    }

}
