package brum.web.common;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.DataFileType;
import brum.model.dto.documents.Document;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

public class ControllerMapper {
    private ControllerMapper() {}

    public static ResponseEntity<byte[]> mapToDownloadFile(DataFile file) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, file.getFileType().getType())
                .body(file.getFile());
    }

    public static DataFile mapDataFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }
        DataFile result = new DataFile();
        try {
            result.setFile(multipartFile.getBytes());
        } catch (IOException e) {
            throw new BRUMGeneralException(ErrorStatusCode.FILE_ERROR, LocalDateTime.now());
        }
        result.setFileName(multipartFile.getOriginalFilename());
        result.setFileType(DataFileType.fromValue(multipartFile.getContentType()));
        return result;
    }

}
