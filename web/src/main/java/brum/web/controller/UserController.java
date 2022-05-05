package brum.web.controller;

import brum.common.views.UserViews;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.NotificationData;
import brum.model.dto.users.User;
import brum.model.dto.users.UserSearchCriteria;
import brum.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

import static brum.common.enums.security.EndpointConstants.UserControllerConstants.*;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(USER_URI)
    @ApiOperation(GET_USERS_DESCRIPTION)
    @JsonView(UserViews.GetList.class)
    public PaginatedResponse<User> getUsers(
            @ApiParam(name = "User search criteria", type = "UserSearchCriteria")
                    UserSearchCriteria searchCriteria) {
        return userService.getUserList(searchCriteria);
    }

    @GetMapping(USER_WITH_ID)
    @ApiOperation(GET_USER_DESCRIPTION)
    @JsonView(UserViews.Get.class)
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping(USER_URI)
    @ApiOperation(POST_USER_DESCRIPTION)
    public void addUser(
            @JsonView(UserViews.Add.class) @RequestBody User userData,
            @ApiIgnore Principal principal) {
        userService.addUser(userData, principal.getName());
    }

    @PatchMapping(USER_WITH_ID)
    @ApiOperation(PATCH_USER_DESCRIPTION)
    public void modifyUser(
            @PathVariable String id,
            @JsonView(UserViews.Modify.class) @RequestBody User user,
            @ApiIgnore Principal principal) {
        userService.modifyUser(id, user, principal.getName());
    }

    @PatchMapping(PASSWORD_URI)
    @ApiOperation(PATCH_PASSWORD_DESCRIPTION)
    public void modifyPassword(
            @JsonView(UserViews.Password.class) @RequestBody
                    User passwordData,
            @ApiIgnore
                Principal principal) {
        userService.modifyPassword(passwordData, principal.getName());
    }

    @GetMapping(NOTIFICATION_URI)
    @ApiOperation(GET_NOTIFICATION_DESCRIPTION)
    public void sendNotification(
            NotificationData notification,
            @ApiIgnore Principal principal) {
        userService.sendNotification(notification, principal.getName());
    }

    @GetMapping(RESET_PASSWORD_URI)
    @ApiOperation(GET_RESET_PASSWORD_DESCRIPTION)
    public void resetPassword(String email) {
        userService.sendResetPasswordNotification(email);
    }
}
