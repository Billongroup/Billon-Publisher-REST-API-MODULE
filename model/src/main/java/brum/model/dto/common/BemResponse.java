package brum.model.dto.common;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.BemException;
import brum.model.exception.ErrorStatusCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static brum.model.exception.ErrorStatusCode.*;

@Data
@Builder
public class BemResponse<T> {
    private T response;
    private ErrorStatusCode status;
    private String bemStatus;

    public boolean isSuccess() {
        return SUCCESS.equals(status);
    }


    public BRUMGeneralException getException() {
        if (!status.equals(BEM_ERROR)) {
            return new BRUMGeneralException(status, LocalDateTime.now());
        }
        return new BemException(bemStatus, LocalDateTime.now());
    }

    public static <K> BemResponse<K> success() {
        return BemResponse.<K>builder().status(SUCCESS).build();
    }

    public static <K> BemResponse<K> success(K value) {
        return BemResponse.<K>builder().status(SUCCESS).response(value).build();
    }

    public static <K> BemResponse<K> bemError(String error) {
        return BemResponse.<K>builder().status(BEM_ERROR).bemStatus(error).build();
    }
}
