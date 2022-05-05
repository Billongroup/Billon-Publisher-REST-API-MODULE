package brum.persistence.mapper;

import brum.model.dto.common.UniqueConstraint;
import brum.model.exception.UniqueConstraintException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class ConstraintExceptionMapper {
    private ConstraintExceptionMapper() {}

    public static RuntimeException map(DataIntegrityViolationException e) {
        if (e.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            if (!UniqueConstraint.getHandledConstraints().contains(constraintViolation.getConstraintName())) {
                return e;
            }
            return new UniqueConstraintException(UniqueConstraint.fromValue(constraintViolation.getConstraintName()));
        }
        return e;
    }
}
