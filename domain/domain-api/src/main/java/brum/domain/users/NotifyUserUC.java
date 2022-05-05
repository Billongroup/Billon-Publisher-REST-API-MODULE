package brum.domain.users;

import brum.model.dto.users.User;

public interface NotifyUserUC {
    void sendSetPasswordNotification(User user);
    void sendResetPasswordNotification(User user);
    void sendPasswordExpiredNotification(User user);
    void sendSmsCodeNotification(User user);
}
