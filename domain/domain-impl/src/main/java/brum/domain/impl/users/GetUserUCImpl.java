package brum.domain.impl.users;

import brum.domain.users.GetUserUC;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.User;
import brum.model.dto.users.UserSearchCriteria;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.UserPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetUserUCImpl implements GetUserUC {

    private final UserPersistenceGateway userPersistenceGateway;

    public GetUserUCImpl(UserPersistenceGateway userPersistenceGateway) {
        this.userPersistenceGateway = userPersistenceGateway;
    }

    @Override
    public PaginatedResponse<User> getUserList(UserSearchCriteria searchCriteria) {
        return userPersistenceGateway.getFilteredUsers(searchCriteria);
    }

    @Override
    public User getUser(String id) {
        User user = userPersistenceGateway.getUserByExternalId(id, true);
        if (user == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        return user;
    }
}
