package brum.model.exception.validation;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationException extends BRUMGeneralException {
    @Getter
    private final transient Object invalidData;
    @Getter
    private final Integer line;
    @Getter
    private final transient Map<InvalidField, ValidationErrorType> errorInfo;

    public ValidationException(InvalidField invalidField, ValidationErrorType errorType, LocalDateTime timestamp) {
        super(ErrorStatusCode.VALIDATION_ERROR, timestamp);
        this.invalidData = null;
        this.errorInfo = Collections.singletonMap(invalidField, errorType);
        this.line = null;
    }

    public ValidationException(Map<InvalidField, ValidationErrorType> errorInfo, LocalDateTime timestamp) {
        super(ErrorStatusCode.VALIDATION_ERROR, timestamp);
        this.invalidData = null;
        this.errorInfo = errorInfo;
        this.line = null;
    }

    public ValidationException(Object invalidData, Integer line, Map<InvalidField, ValidationErrorType> errorInfo) {
        super(null);
        this.invalidData = invalidData;
        this.line = line;
        this.errorInfo = errorInfo;
    }

    public ValidationException(Object invalidData, Integer line, InvalidField invalidField, ValidationErrorType errorType) {
        super(null);
        this.invalidData = invalidData;
        this.line = line;
        this.errorInfo = Collections.singletonMap(invalidField, errorType);
    }
}
