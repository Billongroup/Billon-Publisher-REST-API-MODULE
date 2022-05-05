package brum.model.exception.file;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ValueParsingException extends BRUMGeneralException {
    @Getter
    private final String value;
    @Getter
    private final TargetType targetType;
    @Getter
    private final int line;

    public ValueParsingException(String value, TargetType targetType, int line, LocalDateTime timestamp) {
        super(ErrorStatusCode.PARSING_ERROR, timestamp);
        this.value = value;
        this.targetType = targetType;
        this.line = line;
    }

    public enum TargetType {
        BOOLEAN
    }
}
