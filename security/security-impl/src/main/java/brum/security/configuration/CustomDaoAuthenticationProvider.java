package brum.security.configuration;

import brum.model.dto.common.ParameterKey;
import brum.persistence.ParameterPersistenceGateway;
import brum.security.model.BlockableUserDetails;
import brum.security.model.BlockableUserLockedException;
import brum.security.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.time.LocalDateTime;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final ParameterPersistenceGateway parameterPersistenceGateway;

    public CustomDaoAuthenticationProvider(UserDetailsServiceImpl userDetailsService, ParameterPersistenceGateway parameterPersistenceGateway) {
        this.userDetailsService = userDetailsService;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        setPreAuthenticationChecks(null);
        setPostAuthenticationChecks(null);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        if (userDetails instanceof BlockableUserDetails && !userDetails.isAccountNonLocked()) {
            long blockTime = parameterPersistenceGateway.getParameterValue(ParameterKey.FAILED_LOGIN_BLOCK_TIME);
            throw new BlockableUserLockedException(this.messages.getMessage("AccountStatusUserDetailsChecker.locked", "User account is locked"),
                    ((BlockableUserDetails) userDetails).getBlockedSince().plusMinutes(blockTime));

        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            if (userDetails instanceof BlockableUserDetails) {
                performBlockActions((BlockableUserDetails) userDetails);
            }
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        if (userDetails instanceof BlockableUserDetails) {
            performUnblockActions((BlockableUserDetails) userDetails);
        }
    }

    @Override
    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        super.setPreAuthenticationChecks(new NoopPreAuthenticationChecks());
    }

    @Override
    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        super.setPostAuthenticationChecks(new AccountStatusUserDetailsChecker());
    }

    private void performBlockActions(BlockableUserDetails userDetails) {
        long maxLoginAttempts = parameterPersistenceGateway.getParameterValue(ParameterKey.MAX_FAILED_LOGIN_ATTEMPTS);
        long blockTime = parameterPersistenceGateway.getParameterValue(ParameterKey.FAILED_LOGIN_BLOCK_TIME);
        if (blockTime == 0 || maxLoginAttempts == 0) {
            return;
        }
        int attempts = userDetails.getFailedLoginAttempts();
        attempts++;
        if (userDetails.getBlockedSince() != null && userDetails.getBlockedSince().plusMinutes(blockTime).isBefore(LocalDateTime.now()) &&
                userDetails.getFailedLoginAttempts() > maxLoginAttempts) {
            attempts = 1;
        }
        userDetailsService.setFailedLoginAttempts(userDetails.getUsername(), attempts);
        if (attempts >= maxLoginAttempts) {
            LocalDateTime blockedSince = LocalDateTime.now();
            userDetailsService.setBlockedSince(userDetails.getUsername(), blockedSince);
            throw new BlockableUserLockedException(
                    this.messages.getMessage("AccountStatusUserDetailsChecker.locked", "User account is locked"), blockedSince.plusMinutes(blockTime));
        }
    }

    private void performUnblockActions(BlockableUserDetails userDetails) {
        if (userDetails.getFailedLoginAttempts() > 0) {
            userDetailsService.setFailedLoginAttempts(userDetails.getUsername(), 0);
        }
        if (userDetails.getBlockedSince() != null) {
            userDetailsService.setBlockedSince(userDetails.getUsername(), null);
        }
    }

    private static class NoopPreAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails toCheck) {
            // no checks before password validation
        }
    }
}
