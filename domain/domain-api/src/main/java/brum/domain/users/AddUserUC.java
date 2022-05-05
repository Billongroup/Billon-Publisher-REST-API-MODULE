package brum.domain.users;

import brum.model.dto.users.User;

public interface AddUserUC {
    void addUser(User user, String creatorUsername);
}
