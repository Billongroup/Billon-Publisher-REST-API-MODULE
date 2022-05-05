package brum.model.exception.file;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class FileParsingException extends BRUMGeneralException {
    @Getter
    private final transient List<FileParsingExceptionEntry> errors;

    public FileParsingException(List<FileParsingExceptionEntry> errors, LocalDateTime timestamp) {
        super(ErrorStatusCode.FILE_ERROR, timestamp);
        this.errors = errors;
    }
}
