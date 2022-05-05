package brum.security;

import brum.model.dto.users.User;

public interface UserSecurityService {
    String authenticate(User user);
    String refreshToken(String oldToken);
    String getTokenForPatchPassword(String username);
    String encryptPassword(String password);
    boolean passwordMatches(String password, String encodedPassword);
}
