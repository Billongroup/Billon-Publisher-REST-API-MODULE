package brum.domain.file.handlers;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import com.opencsv.CSVReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CSVFileHandler extends AbstractFileHandler {
    private final CSVReader reader;

    public CSVFileHandler(byte[] file, Map<FileHeader, Boolean> headersToRead) {
        super(headersToRead);
        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(file)));
    }

    @Override
    public List<String[]> readData() {
        try {
            return reader.readAll();
        } catch (Exception e) {
            throw new BRUMGeneralException(ErrorStatusCode.FILE_ERROR, LocalDateTime.now());
        }

    }

    @Override
    protected String[] readHeaders() {
        try {
            return reader.readNext();
        } catch (Exception e) {
            throw new BRUMGeneralException(ErrorStatusCode.FILE_ERROR, LocalDateTime.now());
        }
    }

    @Override
    protected void closeReader() throws IOException {
        reader.close();
    }
}
