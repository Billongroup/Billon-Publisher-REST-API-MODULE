package brum.domain.users;

import brum.model.dto.users.NotificationData;
import brum.model.dto.users.User;

public interface ModifyUserUC {
    void modifyUser(String userId, User user, String issuerUsername);
    void modifyPassword(User passwordData, String username);
    void sendNotification(NotificationData notification, String issuerUsername);
    void requirePasswordChange(String id, String issuerUsername);
    void sendResetPasswordNotification(String email);
}
