package brum.domain.users;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.User;
import brum.model.dto.users.UserSearchCriteria;

public interface GetUserUC {
    PaginatedResponse<User> getUserList(UserSearchCriteria searchCriteria);
    User getUser(String id);
}
