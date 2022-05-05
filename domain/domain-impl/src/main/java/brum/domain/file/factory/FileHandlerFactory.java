package brum.domain.file.factory;

import brum.domain.file.handlers.CSVFileHandler;
import brum.domain.file.handlers.ExcelFileHandler;
import brum.domain.file.handlers.AbstractFileHandler;
import brum.domain.file.handlers.FileHeader;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.model.dto.common.DataFileType;

import java.time.LocalDateTime;
import java.util.Map;

public class FileHandlerFactory {
    private FileHandlerFactory() {}

    public static AbstractFileHandler getFileHandler(byte[] file, DataFileType fileType, Map<FileHeader, Boolean> headersToRead) {
        if (fileType == null) {
            throw new BRUMGeneralException(ErrorStatusCode.FORMAT_NOT_SUPPORTED, LocalDateTime.now());
        }
        if (fileType.equals(DataFileType.CSV)) {
            return new CSVFileHandler(file, headersToRead);
        } else if (fileType.equals(DataFileType.EXCEL)) {
            return new ExcelFileHandler(file, headersToRead);
        }
        throw new BRUMGeneralException(ErrorStatusCode.FORMAT_NOT_SUPPORTED, LocalDateTime.now());
    }
}
