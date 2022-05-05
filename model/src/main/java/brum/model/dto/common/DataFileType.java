package brum.model.dto.common;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public enum DataFileType {
    CSV("text/csv"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PDF("application/pdf");

    private final String type;

    private static final Map<String, DataFileType> lookup = new HashMap<>();

    static {
        for (DataFileType t : DataFileType.values()) {
            lookup.put(t.getType(), t);
        }
    }

    DataFileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DataFileType fromValue(String type) {
        DataFileType result = lookup.get(type);
        if (result == null) {
            throw new BRUMGeneralException(ErrorStatusCode.FORMAT_NOT_SUPPORTED, LocalDateTime.now());
        }
        return result;
    }
}
