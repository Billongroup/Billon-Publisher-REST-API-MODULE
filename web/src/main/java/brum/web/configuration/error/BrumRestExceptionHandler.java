package brum.web.configuration.error;

import brum.common.logger.BrumLogger;
import brum.common.logger.factory.BrumLoggerFactory;
import brum.model.exception.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "brum.web")
@Qualifier("BrumRestExceptionHandler")
public class BrumRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final BrumLogger LOG = BrumLoggerFactory.create(BrumRestExceptionHandler.class);

    @ExceptionHandler(BRUMGeneralException.class)
    protected ResponseEntity<Object> handleBRUMException(BRUMGeneralException ex) {
        logException(ex);
        return new ResponseEntity<>(ex, mapErrorToHttpStatus(ex.getStatusCode()));
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartException(){
        BRUMGeneralException ex = new BRUMGeneralException(ErrorStatusCode.NO_FILE_PASSED, LocalDateTime.now());
        return new ResponseEntity<>(ex, mapErrorToHttpStatus(ex.getStatusCode()));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    protected ResponseEntity<Object> handleUnsupportedException() {
        BRUMGeneralException brumException = new BRUMGeneralException(ErrorStatusCode.ONE_STEP_PUBLISH_ENABLED, LocalDateTime.now());
        return new ResponseEntity<>(brumException, mapErrorToHttpStatus(ErrorStatusCode.ONE_STEP_PUBLISH_ENABLED));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        if (ex.getCause() instanceof UnsupportedOperationException) {
            return handleUnsupportedException();
        }
        ex.printStackTrace();
        BRUMGeneralException brumException = new BRUMGeneralException(ErrorStatusCode.INTERNAL_SERVER_ERROR, LocalDateTime.now());
        return new ResponseEntity<>(brumException, mapErrorToHttpStatus(ErrorStatusCode.INTERNAL_SERVER_ERROR));
    }

    private void logException(BRUMGeneralException ex) {
        LOG.error(ex.getMessage());
    }

    private HttpStatus mapErrorToHttpStatus(ErrorStatusCode code) {
        if (code == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        switch (code) {
            case FORBIDDEN:
                return HttpStatus.FORBIDDEN;
            case INTERNAL_SERVER_ERROR:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            default:
                return HttpStatus.BAD_REQUEST;
        }
    }

}
