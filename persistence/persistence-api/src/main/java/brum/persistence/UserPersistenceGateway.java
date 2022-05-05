package brum.persistence;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.*;

import java.time.LocalDateTime;
import java.util.List;

public interface UserPersistenceGateway {
    PaginatedResponse<User> getFilteredUsers(UserSearchCriteria searchCriteria);
    User getUserByExternalId(String externalId, boolean getHistory);
    User getUserByUsername(String username);
    User getUserBasicInfoByUsername(String username);
    UserPrincipal getUserPrincipalByUsername(String username);
    User getUserByEmail(String email);
    void saveUser(User user);
    void addHistoryEntry(User user, String updatedBy);
    void addPasswordHistoryEntry(User user);
    List<String> getUserIdsWithExpiredPassword(Long passwordExpirationDays);
    void setFailedLoginAttempts(String username, Integer attempts);
    void setBlockedSince(String username, LocalDateTime blockedSince);
}
