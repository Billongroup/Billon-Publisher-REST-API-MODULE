package brum.service;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.NotificationData;
import brum.model.dto.users.User;
import brum.model.dto.users.UserSearchCriteria;

public interface UserService {
    PaginatedResponse<User> getUserList(UserSearchCriteria searchCriteria);
    User getUser(String externalId);
    void addUser(User user, String creatorUsername);
    void modifyUser(String userId, User user, String issuerUsername);
    void modifyPassword(User passwordData, String username);
    void sendNotification(NotificationData notification, String issuerUsername);
    void sendResetPasswordNotification(String email);
}
