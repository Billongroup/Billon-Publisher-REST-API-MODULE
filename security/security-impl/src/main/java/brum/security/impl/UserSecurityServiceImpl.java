package brum.security.impl;

import brum.common.logger.BrumLogger;
import brum.common.logger.factory.BrumLoggerFactory;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.users.User;
import brum.model.dto.users.UserPrincipal;
import brum.model.dto.users.UserStatus;
import brum.model.exception.AccountBlockedException;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import brum.security.UserSecurityService;
import brum.security.jwt.JWTUtils;
import brum.security.model.BlockableUserLockedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    private static final BrumLogger LOG = BrumLoggerFactory.create(UserSecurityServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserPersistenceGateway userPersistenceGateway;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ParameterPersistenceGateway parameterPersistenceGateway;

    public UserSecurityServiceImpl(AuthenticationManager authenticationManager,
                                   UserPersistenceGateway userPersistenceGateway,
                                   BCryptPasswordEncoder bCryptPasswordEncoder,
                                   ParameterPersistenceGateway parameterPersistenceGateway) {
        this.authenticationManager = authenticationManager;
        this.userPersistenceGateway = userPersistenceGateway;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
    }

    @Override
    public String authenticate(User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            LOG.error("Bad credentials during authentication with username {}", user.getUsername());
            throw new BRUMGeneralException(ErrorStatusCode.FORBIDDEN, LocalDateTime.now());
        } catch (DisabledException e) {
            LOG.error("Account inactive during authentication with username {}", user.getUsername());
            throw new BRUMGeneralException(ErrorStatusCode.ACCOUNT_INACTIVE, LocalDateTime.now());
        } catch (LockedException e) {
            LOG.error("Account locked due to exceeding max failed login attempts {}", user.getUsername());
            LocalDateTime unblockDate = null;
            if (e instanceof BlockableUserLockedException) {
                unblockDate = ((BlockableUserLockedException) e).getUnblockDate();
            }
            throw new AccountBlockedException(unblockDate, LocalDateTime.now());
        } catch (AuthenticationException ex) {
                LOG.error("Unexpected error during authentication with username {}", user.getUsername());
            throw new BRUMGeneralException(ErrorStatusCode.FORBIDDEN, LocalDateTime.now());
        }
        UserPrincipal userPrincipal = userPersistenceGateway.getUserPrincipalByUsername(user.getUsername());
        Long expirationMinutes = parameterPersistenceGateway.getParameterValue(ParameterKey.JWT_EXPIRATION_TIME);
        Long refreshExpirationMinutes = parameterPersistenceGateway.getParameterValue(ParameterKey.JWT_REFRESH_EXPIRATION_TIME);
        return "Bearer " + JWTUtils.generateToken(
                userPrincipal, userPrincipal.getStatus().equals(UserStatus.PASSWORD_EXPIRED), expirationMinutes, refreshExpirationMinutes);
    }

    @Override
    public String refreshToken(String oldToken) {
        JWTUtils.JWTData jwtData = JWTUtils.getUserDataFromToken(oldToken);
        if (jwtData == null ||
                jwtData.getRefreshExpirationDate() == null ||
                jwtData.getRefreshExpirationDate().before(new Date(System.currentTimeMillis()))) {
            throw new BRUMGeneralException(ErrorStatusCode.FORBIDDEN, LocalDateTime.now());
        }
        UserPrincipal userPrincipal = userPersistenceGateway.getUserPrincipalByUsername(jwtData.getUsername());
        Long expirationMinutes = parameterPersistenceGateway.getParameterValue(ParameterKey.JWT_EXPIRATION_TIME);
        Long refreshExpirationMinutes = parameterPersistenceGateway.getParameterValue(ParameterKey.JWT_REFRESH_EXPIRATION_TIME);
        return "Bearer " + JWTUtils.generateToken(userPrincipal, jwtData.getForPatchPassword(), expirationMinutes, refreshExpirationMinutes);
    }

    @Override
    public String getTokenForPatchPassword(String username) {
        UserPrincipal userPrincipal = userPersistenceGateway.getUserPrincipalByUsername(username);
        Long expirationMinutes = parameterPersistenceGateway.getParameterValue(ParameterKey.JWT_PATCH_PASSWORD_EXPIRATION_TIME);
        return JWTUtils.generateToken(userPrincipal, true, expirationMinutes, 0L);
    }

    @Override
    public String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean passwordMatches(String password, String encodedPassword) {
        return bCryptPasswordEncoder.matches(password, encodedPassword);
    }
}
