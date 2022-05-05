package brum.domain.impl.utilities;

import brum.domain.users.NotifyUserUC;
import brum.domain.utilities.UtilitiesUC;
import brum.model.dto.common.Parameter;
import brum.model.dto.users.User;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UtilitiesUCImpl implements UtilitiesUC {

    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private final UserPersistenceGateway userPersistenceGateway;
    private final NotifyUserUC notifyUserUC;

    public UtilitiesUCImpl(ParameterPersistenceGateway parameterPersistenceGateway,
                           UserPersistenceGateway userPersistenceGateway,
                           NotifyUserUC notifyUserUC) {
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        this.userPersistenceGateway = userPersistenceGateway;
        this.notifyUserUC = notifyUserUC;
    }

    @Override
    public List<Parameter> getSettings() {
        return parameterPersistenceGateway.getNonHiddenParameters();
    }

    @Override
    public void getSmsCode(String username) {
        User user = userPersistenceGateway.getUserByUsername(username);
        String code = String.valueOf(new Random().nextInt(1000000));
        code = "0".repeat(6 - code.length()) + code;
        user.setSmsCode(code);
        user.setSmsCodeGenerationTime(LocalDateTime.now());
        userPersistenceGateway.saveUser(user);
        notifyUserUC.sendSmsCodeNotification(user);
    }
}
