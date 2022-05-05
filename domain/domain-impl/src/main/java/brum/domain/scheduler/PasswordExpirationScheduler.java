package brum.domain.scheduler;

import brum.domain.users.ModifyUserUC;
import brum.model.dto.common.ParameterKey;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordExpirationScheduler {

    private final UserPersistenceGateway userPersistenceGateway;
    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private final ModifyUserUC modifyUserUC;

    public PasswordExpirationScheduler(UserPersistenceGateway userPersistenceGateway,
                                       ParameterPersistenceGateway parameterPersistenceGateway,
                                       ModifyUserUC modifyUserUC) {
        this.userPersistenceGateway = userPersistenceGateway;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        this.modifyUserUC = modifyUserUC;
    }

    @Scheduled(cron = "0 0/1 * * * *") // set cron to once per day at midnight
    public void checkUsersForExpiredPassword() {
        Long passwordExpirationDays = parameterPersistenceGateway.getParameterValue(ParameterKey.PASSWORD_EXPIRATION_TIME);
        List<String> userIds = userPersistenceGateway.getUserIdsWithExpiredPassword(passwordExpirationDays);
        for (String id : userIds) {
            modifyUserUC.requirePasswordChange(id, null);
        }
    }
}
