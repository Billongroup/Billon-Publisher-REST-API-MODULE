package brum.domain.file.resolvers;

import brum.domain.file.factory.FileHandlerFactory;
import brum.domain.file.handlers.AbstractFileHandler;
import brum.domain.file.handlers.FileHeader;
import brum.model.dto.common.DataFileType;
import brum.model.exception.file.ValueParsingException;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractFileResolver {

    protected final Map<FileHeader, Integer> valueIndex;
    protected final List<String[]> data;

    protected AbstractFileResolver(byte[] file, DataFileType fileType, Map<FileHeader, Boolean> headersToRead) {
        AbstractFileHandler fileHandler = FileHandlerFactory.getFileHandler(file, fileType, headersToRead);
        valueIndex = fileHandler.resolveHeaders();
        data = fileHandler.readData();
    }

    protected String formatPhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return null;
        }
        if (phoneNumber.startsWith("'")) {
            return phoneNumber.substring(1);
        }
        return phoneNumber;
    }

    protected Boolean parseToBooleanValue(String value, int line) {
        if (!StringUtils.hasText(value)) {
            return BooleanParser.NULL.value;
        }
        if (!BooleanParser.getAllAcceptedValues().contains(value.toLowerCase())) {
            throw new ValueParsingException(value, ValueParsingException.TargetType.BOOLEAN, line, LocalDateTime.now());
        }
        return BooleanParser.fromValue(value).value;
    }

    protected String getNullable(String value) {
        return value == null ? "" : value;
    }

    private enum BooleanParser {
        TRUE(true, "1", "1.0", "true", "tak"),
        FALSE(false, "0", "0.0", "false", "nie"),
        NULL(null, "null");

        private final Boolean value;
        private final List<String> acceptedValues;

        BooleanParser(Boolean value, String... acceptedValues) {
            this.value = value;
            this.acceptedValues = Arrays.asList(acceptedValues);
        }

        public static List<String> getAllAcceptedValues() {
            List<String> result = new ArrayList<>(TRUE.acceptedValues);
            result.addAll(FALSE.acceptedValues);
            result.addAll(NULL.acceptedValues);
            return result;
        }

        public static BooleanParser fromValue(String value) {
            if (TRUE.acceptedValues.contains(value.toLowerCase())) {
                return TRUE;
            }
            if (FALSE.acceptedValues.contains(value.toLowerCase())) {
                return FALSE;
            }
            return NULL;
        }
    }
}
