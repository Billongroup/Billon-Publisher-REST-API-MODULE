package brum.web.controller;

import brum.common.views.UserViews;
import brum.model.dto.common.Parameter;
import brum.model.dto.users.LoginResponse;
import brum.model.dto.users.User;
import brum.service.UtilService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static brum.common.enums.security.EndpointConstants.UtilControllerConstants.*;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class UtilController {

    private final UtilService utilService;

    public UtilController(UtilService utilService) {
        this.utilService = utilService;
    }

    @PostMapping(LOGIN_URI)
    @ApiOperation(LOGIN_DESCRIPTION)
    public LoginResponse login(@JsonView(UserViews.Login.class) @RequestBody User userLoginRequest) {
        return utilService.login(userLoginRequest);
    }

    @GetMapping(SETTINGS_URI)
    @ApiOperation(GET_SETTINGS_DESCRIPTION)
    public List<Parameter> getSettings() {
        return utilService.getSettings();
    }

    @GetMapping(HELLO_URI)
    @ApiOperation(GET_HELLO_DESCRIPTION)
    public LocalDateTime hello() {
        return LocalDateTime.now();
    }

    @GetMapping(SMS_CODE_URI)
    @ApiOperation(GET_SMS_CODE_DESCRIPTION)
    public void getSmsCode(@ApiIgnore Principal principal) {
        utilService.getSmsCode(principal.getName());
    }

    @GetMapping(REFRESH_TOKEN_URI)
    @ApiOperation(GET_REFRESH_TOKEN_DESCRIPTION)
    public LoginResponse refreshToken(@ApiIgnore @RequestHeader(value = "Authorization", required = false) String jwtToken) {
        return utilService.refreshToken(jwtToken);
    }
}
