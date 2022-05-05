package brum.security.service;

import brum.common.enums.security.PrivilegeEnum;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.users.*;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import brum.security.model.BlockableUserDetails;
import brum.security.jwt.JWTUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final List<PrivilegeEnum> USER_PRIVILEGES_FOR_SET_PASSWORD = Arrays.asList(PrivilegeEnum.PATCH_PASSWORD, PrivilegeEnum.GET_SMS_CODE);
    private static final List<UserStatus> USER_STATUSES_ALLOWED_TO_AUTHENTICATE = Arrays.asList(UserStatus.REGISTERED, UserStatus.PASSWORD_EXPIRED);

    private final UserPersistenceGateway userPersistenceGateway;
    private final ParameterPersistenceGateway parameterPersistenceGateway;

    public UserDetailsServiceImpl(UserPersistenceGateway userPersistenceGateway, ParameterPersistenceGateway parameterPersistenceGateway) {
        this.userPersistenceGateway = userPersistenceGateway;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         UserPrincipal user = userPersistenceGateway.getUserPrincipalByUsername(username);
         if (user == null || !USER_STATUSES_ALLOWED_TO_AUTHENTICATE.contains(user.getStatus())) {
             throw new UsernameNotFoundException(username);
         }
         return new BlockableUserDetails(
                 user.getUsername(),
                 user.getPassword(),
                 user.getStatus().equals(UserStatus.PASSWORD_EXPIRED) || user.getIsActive(),
                 true,
                 true,
                 checkIfNonLocked(user),
                 getAuthorities(user),
                 user.getFailedLoginAttempts(),
                 user.getBlockedSince()
         );
    }

    public Collection<GrantedAuthority> getPrivilegesByUsername(JWTUtils.JWTData jwtData) {
        UserPrincipal user = userPersistenceGateway.getUserPrincipalByUsername(jwtData.getUsername());
        if (user == null) {
            return new ArrayList<>();
        }
        if (jwtData.getForRefresh() != null && jwtData.getForRefresh()) {
            return getRefreshAuthority();
        }
        if (user.getStatus().equals(UserStatus.PASSWORD_EXPIRED) || (jwtData.getForPatchPassword() != null && jwtData.getForPatchPassword())) {
            return getAuthoritiesForSetPassword();
        }
        return getAuthorities(user);
    }

    public void setFailedLoginAttempts(String username, Integer attempts) {
        userPersistenceGateway.setFailedLoginAttempts(username, attempts);
    }

    public void setBlockedSince(String username, LocalDateTime blockedSince) {
        userPersistenceGateway.setBlockedSince(username, blockedSince);
    }

    private List<GrantedAuthority> getAuthorities(UserPrincipal user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Privilege privilege : user.getRole().getRolePrivilege()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName().name()));
        }
        return authorities;
    }

    private List<GrantedAuthority> getAuthoritiesForSetPassword() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (PrivilegeEnum privilege : USER_PRIVILEGES_FOR_SET_PASSWORD) {
            authorities.add(new SimpleGrantedAuthority(privilege.name()));
        }
        return authorities;
    }

    private List<GrantedAuthority> getRefreshAuthority() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PrivilegeEnum.GET_REFRESH_TOKEN.name()));
        return authorities;
    }

    private boolean checkIfNonLocked(UserPrincipal user) {
        if (user.getBlockedSince() == null) {
            return true;
        }
        Long maxLoginAttempts = parameterPersistenceGateway.getParameterValue(ParameterKey.MAX_FAILED_LOGIN_ATTEMPTS);
        if (maxLoginAttempts == 0) {
            return true;
        }
        Long blockTimeParam = parameterPersistenceGateway.getParameterValue(ParameterKey.FAILED_LOGIN_BLOCK_TIME);
        return !user.getBlockedSince().plusMinutes(blockTimeParam).isAfter(LocalDateTime.now());
    }

}
