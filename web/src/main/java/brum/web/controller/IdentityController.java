package brum.web.controller;

import brum.common.views.IdentityViews;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentitySearchCriteria;
import brum.service.IdentityService;
import brum.model.dto.common.PaginatedResponse;
import brum.web.common.ControllerMapper;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

import static brum.common.enums.security.EndpointConstants.IdentityControllerConstants.*;
import static brum.web.common.ControllerMapper.mapDataFile;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class IdentityController {
    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping(IDENTITY_WITH_ID_URI)
    @ApiOperation(GET_IDENTITY_DESCRIPTION)
    @JsonView(IdentityViews.GetIdentity.class)
    public Identity getIdentity(@PathVariable String id) {
        return identityService.getIdentity(id);
    }

    @GetMapping(IDENTITY_URI)
    @ApiOperation(GET_IDENTITIES_DESCRIPTION)
    @JsonView(IdentityViews.GetIdentity.class)
    public PaginatedResponse<Identity> getIdentities(
            @ApiParam(name = "Identity search criteria", type = "IdentitySearchCriteria")
                    IdentitySearchCriteria searchCriteria) {
        return identityService.getIdentities(searchCriteria);
    }

    @GetMapping(IDENTITY_FILE_URI)
    @ApiOperation(GET_IDENTITY_FILE_DESCRIPTION)
    public ResponseEntity<byte[]> generateIdentitiesReport() {
        return ControllerMapper.mapToDownloadFile(identityService.generateIdentitiesReport());
    }

    @PostMapping(IDENTITY_URI)
    @ApiOperation(POST_IDENTITY_DESCRIPTION)
    public void addIdentity(
            @JsonView(IdentityViews.Add.class) @RequestBody
                    Identity addIdentityInData,
            @ApiIgnore Principal principal) {
        identityService.addIdentity(addIdentityInData, principal.getName());
    }

    @PostMapping(IDENTITY_FILE_URI)
    @ApiOperation(POST_IDENTITY_FILE_DESCRIPTION)
    public void addIdentitiesFromFile(
            @RequestPart("file")
                    MultipartFile file,
            @ApiIgnore Principal principal) {
        identityService.addIdentitiesFromFile(mapDataFile(file), principal.getName());
    }

    @PatchMapping(IDENTITY_WITH_ID_URI)
    @ApiOperation(PATCH_IDENTITY_DESCRIPTION)
    public void modifyIdentity(@JsonView(IdentityViews.Modify.class) @RequestBody Identity modifyIdentityInData, @PathVariable String id) {
        modifyIdentityInData.setExternalId(id);
        identityService.modifyIdentity(modifyIdentityInData);
    }

    @DeleteMapping(IDENTITY_WITH_ID_URI)
    @ApiOperation(DELETE_IDENTITY_DESCRIPTION)
    public void deleteIdentity(@PathVariable String id) {
        identityService.deleteIdentity(id);
    }

    @PatchMapping(IDENTITY_GDPR_SUSPENDED_URI)
    @ApiOperation(PATCH_IDENTITY_GDPR_SUSPENDED_DESCRIPTION)
    public void suspendIdentity(
            @PathVariable String id,
            @JsonView(IdentityViews.GdprSuspended.class) @RequestBody Identity isSuspended) {
        identityService.setIsGdprSuspended(id, isSuspended);
    }
}
