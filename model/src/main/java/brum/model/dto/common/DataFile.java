package brum.model.dto.common;

import lombok.Data;

import java.io.InputStream;

@Data
public class DataFile {
    private byte[] file;
    private String fileName;
    private DataFileType fileType;
}
