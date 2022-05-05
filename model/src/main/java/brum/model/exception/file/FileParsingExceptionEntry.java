package brum.model.exception.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileParsingExceptionEntry {
    @Getter
    private final ParsingErrorType type;
    @Getter
    private final List<String> acceptedValues;
    @Getter
    private final String columnName;

    public FileParsingExceptionEntry(ParsingErrorType type, String columnName, List<String> acceptedValues) {
        this.type = type;
        this.acceptedValues = acceptedValues;
        this.columnName = columnName;
    }

    public FileParsingExceptionEntry(ParsingErrorType type, String columnName) {
        this.type = type;
        this.columnName = columnName;
        acceptedValues = null;
    }
}
