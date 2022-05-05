package brum.domain.file.handlers;

import brum.model.exception.*;
import brum.model.exception.file.FileParsingException;
import brum.model.exception.file.FileParsingExceptionEntry;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static brum.model.exception.file.ParsingErrorType.*;

public abstract class AbstractFileHandler {

    private final Map<FileHeader, Boolean> headersToRead;

    protected AbstractFileHandler(Map<FileHeader, Boolean> headersToRead) {
        this.headersToRead = headersToRead;
    }

    public Map<FileHeader, Integer> resolveHeaders() {
        List<FileParsingExceptionEntry> duplicatedColumns = new ArrayList<>();
        String[] headersFromFile = readHeaders();
        Map<FileHeader, Integer> valueIndex = new EnumMap<>(FileHeader.class);
        for (int i = 0; i < headersFromFile.length; i++) {
            for (FileHeader headerEnum : headersToRead.keySet()) {
                if (!StringUtils.hasText(headersFromFile[i]) || !headerEnum.getHeaderNames().contains(headersFromFile[i].toLowerCase().trim())) {
                    continue;
                }
                if (valueIndex.containsKey(headerEnum)) {
                    duplicatedColumns.add(
                            new FileParsingExceptionEntry(DUPLICATED, headerEnum.name()));
                } else {
                    valueIndex.put(headerEnum, i);
                }
            }
        }
        try {
            closeReader();
        } catch (IOException e) {
            throw new BRUMGeneralException(ErrorStatusCode.FILE_ERROR, LocalDateTime.now());
        }
        List<FileHeader> missingHeaders = new ArrayList<>(headersToRead.keySet());
        missingHeaders.removeAll(valueIndex.keySet());
        missingHeaders = missingHeaders.stream().filter(headersToRead::get).collect(Collectors.toList());
        if (missingHeaders.isEmpty() && duplicatedColumns.isEmpty()) {
            return valueIndex;
        }
        throw throwValidationException(duplicatedColumns, missingHeaders);
    }

    private FileParsingException throwValidationException(List<FileParsingExceptionEntry> duplicatedColumns,
                                List<FileHeader> missingHeaders) {
        List<FileParsingExceptionEntry> parsingErrors = new ArrayList<>();
        if (!duplicatedColumns.isEmpty()) {
            parsingErrors.addAll(duplicatedColumns);
        }
        if (!missingHeaders.isEmpty()) {
            for (FileHeader missingHeader : missingHeaders) {
                parsingErrors.add(new FileParsingExceptionEntry(MISSING, missingHeader.name(), missingHeader.getHeaderNames()));
            }
        }
        return new FileParsingException(parsingErrors, LocalDateTime.now());
    }

    protected abstract String[] readHeaders();

    public abstract List<String[]> readData();

    protected abstract void closeReader() throws IOException;

}