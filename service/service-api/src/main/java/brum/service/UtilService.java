package brum.service;

import brum.model.dto.common.Parameter;
import brum.model.dto.users.LoginResponse;
import brum.model.dto.users.User;

import java.util.List;

public interface UtilService {
    LoginResponse login(User user);
    List<Parameter> getSettings();
    void getSmsCode(String username);
    LoginResponse refreshToken(String jwtToken);
}
