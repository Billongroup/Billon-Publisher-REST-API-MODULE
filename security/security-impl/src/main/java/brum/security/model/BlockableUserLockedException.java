package brum.security.model;

import lombok.Getter;
import org.springframework.security.authentication.LockedException;

import java.time.LocalDateTime;

public class BlockableUserLockedException extends LockedException {

    @Getter
    private final LocalDateTime unblockDate;

    public BlockableUserLockedException(String msg, LocalDateTime unblockDate) {
        super(msg);
        this.unblockDate = unblockDate;
    }

}
