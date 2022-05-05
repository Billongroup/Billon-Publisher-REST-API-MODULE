package brum.model.exception;

import brum.model.dto.common.UniqueConstraint;
import lombok.Getter;

public class UniqueConstraintException extends BRUMGeneralException {
    @Getter
    private final UniqueConstraint constraint;

    public UniqueConstraintException(UniqueConstraint constraint) {
        super(ErrorStatusCode.NOT_UNIQUE);
        this.constraint = constraint;
    }
}
