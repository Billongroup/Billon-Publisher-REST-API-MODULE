package brum.domain.impl.users;

import brum.common.utils.ExternalId;
import brum.domain.users.AddUserUC;
import brum.domain.users.NotifyUserUC;
import brum.domain.validator.UserValidator;
import brum.model.dto.users.User;
import brum.model.dto.users.UserStatus;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.UniqueConstraintException;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import brum.model.exception.validation.ValidationException;
import brum.persistence.UserPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static brum.model.exception.validation.ValidationErrorType.NON_UNIQUE;

@Service
public class AddUserUCImpl implements AddUserUC {

    private final UserPersistenceGateway userPersistenceGateway;
    private final NotifyUserUC notifyUserUC;

    public AddUserUCImpl(UserPersistenceGateway userPersistenceGateway, NotifyUserUC notifyUserUC) {
        this.userPersistenceGateway = userPersistenceGateway;
        this.notifyUserUC = notifyUserUC;
    }

    @Override
    public void addUser(User user, String creatorUsername) {
        Map<InvalidField, ValidationErrorType> validationErrors = UserValidator.validateUser(user);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors, LocalDateTime.now());
        }
        user.setExternalId(ExternalId.USER.generateId());
        User creator = new User();
        creator.setUsername(creatorUsername);
        user.setCreatedBy(creator);
        user.setCreatedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        if (user.getIsRobot() == null) {
            user.setIsRobot(false);
        }
        if (Boolean.TRUE.equals(user.getNotify())) {
            user.setLastNotificationAt(LocalDateTime.now());
            user.setStatus(UserStatus.PENDING);
        } else {
            user.setStatus(UserStatus.SHELL);
        }
        try {
            userPersistenceGateway.saveUser(user);
        } catch (UniqueConstraintException e) {
            throw new ValidationException(InvalidField.valueOf(e.getConstraint().name()), NON_UNIQUE, LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(user.getNotify())) {
            try {
                notifyUserUC.sendSetPasswordNotification(user);
                userPersistenceGateway.addHistoryEntry(user, creatorUsername);
            } catch (BRUMGeneralException e) {
                User userFromDB = userPersistenceGateway.getUserByExternalId(user.getExternalId(), false);
                userFromDB.setStatus(UserStatus.SHELL);
                userFromDB.setLastNotificationAt(null);
                userPersistenceGateway.saveUser(userFromDB);
                throw e;
            }
        } else {
            userPersistenceGateway.addHistoryEntry(user, creatorUsername);
        }
    }
}
