package brum.service.impl;

import brum.domain.utilities.UtilitiesUC;
import brum.model.dto.common.Parameter;
import brum.model.dto.users.LoginResponse;
import brum.model.dto.users.User;
import brum.security.UserSecurityService;
import brum.service.UtilService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilServiceImpl implements UtilService {

    private final UserSecurityService userSecurityService;
    private final UtilitiesUC utilitiesUC;

    public UtilServiceImpl(UserSecurityService userSecurityService, UtilitiesUC utilitiesUC) {
        this.userSecurityService = userSecurityService;
        this.utilitiesUC = utilitiesUC;
    }

    @Override
    public LoginResponse login(User user) {
        return new LoginResponse(userSecurityService.authenticate(user));
    }

    @Override
    public List<Parameter> getSettings() {
        return utilitiesUC.getSettings();
    }

    @Override
    public void getSmsCode(String username) {
        utilitiesUC.getSmsCode(username);
    }

    @Override
    public LoginResponse refreshToken(String jwtToken) {
        return new LoginResponse(userSecurityService.refreshToken(jwtToken));
    }
}
