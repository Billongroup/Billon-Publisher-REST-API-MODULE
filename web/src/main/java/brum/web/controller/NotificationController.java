package brum.web.controller;

import brum.model.dto.notifications.NotificationContent;
import brum.model.dto.notifications.NotificationContentFilter;
import brum.service.NotificationsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static brum.common.enums.security.EndpointConstants.NotificationControllerConstants.*;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class NotificationController {
    private final NotificationsService notificationsService;

    public NotificationController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping(NOTIFICATION_CONTENT_URI)
    @ApiOperation(GET_NOTIFICATION_CONTENT_DESCRIPTION)
    public NotificationContent getNotificationContent(@PathVariable String id, NotificationContentFilter filters) {
        return notificationsService.getNotificationContent(id, filters);
    }
}
