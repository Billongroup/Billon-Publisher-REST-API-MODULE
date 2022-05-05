package brum.service.impl;

import brum.domain.users.AddUserUC;
import brum.domain.users.GetUserUC;
import brum.domain.users.ModifyUserUC;
import brum.domain.users.NotifyUserUC;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.NotificationData;
import brum.model.dto.users.User;
import brum.model.dto.users.UserSearchCriteria;
import brum.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final GetUserUC getUserUC;
    private final AddUserUC addUserUC;
    private final ModifyUserUC modifyUserUC;

    public UserServiceImpl(GetUserUC getUserUC, AddUserUC addUserUC, ModifyUserUC modifyUserUC) {
        this.getUserUC = getUserUC;
        this.addUserUC = addUserUC;
        this.modifyUserUC = modifyUserUC;
    }

    @Override
    public PaginatedResponse<User> getUserList(UserSearchCriteria searchCriteria) {
        return getUserUC.getUserList(searchCriteria);
    }

    @Override
    public User getUser(String externalId) {
        return getUserUC.getUser(externalId);
    }

    @Override
    public void addUser(User user, String creatorUsername) {
        addUserUC.addUser(user, creatorUsername);
    }

    @Override
    public void modifyUser(String userId, User user, String issuerUsername) {
        modifyUserUC.modifyUser(userId, user, issuerUsername);
    }

    @Override
    public void modifyPassword(User passwordData, String username) {
        modifyUserUC.modifyPassword(passwordData, username);
    }

    @Override
    public void sendNotification(NotificationData notification, String issuerUsername) {
        modifyUserUC.sendNotification(notification, issuerUsername);
    }

    @Override
    public void sendResetPasswordNotification(String email) {
        modifyUserUC.sendResetPasswordNotification(email);
    }
}
