package brum.web.controller;

import brum.service.RecipientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static brum.common.enums.security.EndpointConstants.RecipientControllerConstants.*;
import static brum.web.common.ControllerMapper.mapDataFile;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class RecipientController {

    private final RecipientService recipientService;

    public RecipientController(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @PostMapping(FILE_VALIDITY_URI)
    @ApiOperation(POST_FILE_VALIDITY_DESCRIPTION)
    public void getFileValidity(@RequestPart("file") MultipartFile file) {
        recipientService.getFileValidity(mapDataFile(file));
    }
}
