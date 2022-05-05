package brum.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

public class BlockableUserDetails extends User {

    @Getter
    @Setter
    private Integer failedLoginAttempts;
    @Getter
    @Setter
    private LocalDateTime blockedSince;

    public BlockableUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                                boolean credentialsNonExpired, boolean accountNonLocked,
                                Collection<? extends GrantedAuthority> authorities, Integer failedLoginAttempts,
                                LocalDateTime blockedSince) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.failedLoginAttempts = failedLoginAttempts;
        this.blockedSince = blockedSince;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
