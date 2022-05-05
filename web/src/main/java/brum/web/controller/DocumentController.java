package brum.web.controller;

import brum.common.views.CategoryViews;
import brum.common.views.DocumentViews;
import brum.model.dto.categories.Category;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.notifications.NotificationStatus;
import brum.model.dto.notifications.NotificationHistorySearchCriteria;
import brum.model.dto.recipients.DocumentRecipient;
import brum.model.dto.recipients.DocumentRecipientSearchCriteria;
import brum.model.dto.recipients.RecipientsToNotify;
import brum.model.dto.tree.DocumentTree;
import brum.service.DocumentService;
import brum.web.common.ControllerMapper;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.security.Principal;

import static brum.common.enums.security.EndpointConstants.DocumentControllerConstants.*;
import static brum.web.common.ControllerMapper.mapDataFile;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class DocumentController {

    protected final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(DOCUMENT_URI)
    @ApiOperation(GET_DOCUMENT_LIST_DESCRIPTION)
    @JsonView(DocumentViews.GetDocumentList.class)
    public PaginatedResponse<Document> getDocumentList(
            @ApiParam(name = "Document search criteria", type = "DocumentSearchCriteria")
                    DocumentSearchCriteria searchCriteria) {
        return documentService.getDocumentList(searchCriteria);
    }

    @GetMapping(DOCUMENT_WITH_ID_URI)
    @ApiOperation(GET_DOCUMENT_DESCRIPTION)
    @JsonView(DocumentViews.GetDocument.class)
    public Document getDocument(@PathVariable String id) {
        return documentService.getDocument(id);
    }

    @GetMapping(DOCUMENT_TREE_URI)
    @ApiOperation(TREE_DOCUMENT_DESCRIPTION)
    public DocumentTree getDocumentTree(@PathVariable String id) {
        return documentService.getTree(id);
    }

    @PostMapping(value = PUBLIC_DOCUMENT_URI,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(POST_ADD_PUBLIC_DOCUMENT_DESCRIPTION)
    @JsonView(DocumentViews.AddResponse.class)
    public Document preparePublicDocument(
            @RequestPart(value = "documentFile")
                    MultipartFile documentFile,
            @RequestPart(value = "contactDetails", required = false)
                    MultipartFile contactDetails,
            @RequestPart(value = "documentInfo") @JsonView(DocumentViews.Add.class)
                    Document documentInfo,
            @ApiIgnore Principal principal) {
        return documentService.preparePublicDocument(
                mapDataFile(documentFile), mapDataFile(contactDetails),
                documentInfo, principal.getName());
    }

    @PostMapping(value = PRIVATE_DOCUMENT_URI,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(POST_ADD_PRIVATE_DOCUMENT_DESCRIPTION)
    @JsonView(DocumentViews.AddResponse.class)
    public Document preparePrivateDocument(
            @RequestPart(value = "documentFile")
                    MultipartFile documentFile,
            @RequestPart(value = "documentInfo") @JsonView(DocumentViews.AddPrivate.class)
                    Document documentInfo,
            @ApiIgnore Principal principal) {
        return documentService.preparePrivateDocument(mapDataFile(documentFile), documentInfo, principal.getName());
    }

    @PostMapping(value = PUBLISH_PUBLIC_DOCUMENT_URI)
    @ApiOperation(POST_PUBLISH_PUBLIC_DOCUMENT_DESCRIPTION)
    public void publishPublicDocument(
            @PathVariable
                    String id,
            @ApiIgnore Principal principal) {
        documentService.publishPublicDocument(new byte[0], id, principal.getName());
    }

    @PostMapping(value = PUBLISH_PRIVATE_DOCUMENT_URI)
    @ApiOperation(POST_PUBLISH_PRIVATE_DOCUMENT_DESCRIPTION)
    public void publishPrivateDocument(
            @PathVariable
                    String id,
            @ApiIgnore Principal principal) {
        documentService.publishPrivateDocument(new byte[0], id, principal.getName());
    }

    @GetMapping(DOCUMENT_FILE_URI)
    @ApiOperation(GET_DOWNLOAD_DOCUMENT_DESCRIPTION)
    public ResponseEntity<byte[]> downloadDocumentFile(@PathVariable String id) {
        return ControllerMapper.mapToDownloadFile(documentService.downloadDocumentFile(id));
    }

    @GetMapping(RECIPIENTS_FILE_URI)
    @ApiOperation(GET_DOWNLOAD_RECIPIENTS_FILE_DESCRIPTION)
    public ResponseEntity<byte[]> downloadRecipientsFile(@PathVariable String id) {
        return ControllerMapper.mapToDownloadFile(documentService.downloadRecipientsFile(id));
    }

    @PatchMapping(RECIPIENTS_FILE_URI)
    @ApiOperation(PATCH_RECIPIENTS_FILE_DESCRIPTION)
    public void updateRecipientsFile(
            @PathVariable String
                    id,
            @RequestPart(value = "contactDetails")
                    MultipartFile contactDetails) {
        documentService.updateRecipientsFile(id, mapDataFile(contactDetails));
    }

    @GetMapping(DOCUMENTS_REPORT_URI)
    @ApiOperation(GET_DOWNLOAD_DOCUMENTS_REPORT_DESCRIPTION)
    public ResponseEntity<byte[]> downloadDocumentsReport(
            @JsonView(DocumentViews.DocumentsReport.class) DocumentSearchCriteria searchCriteria) {
        return ControllerMapper.mapToDownloadFile(documentService.downloadDocumentsReport(searchCriteria));
    }

    @DeleteMapping(DOCUMENT_WITH_ID_URI)
    @ApiOperation(DELETE_DISCARD_DOCUMENT_DESCRIPTION)
    public void discardDocument(@PathVariable String id) {
        documentService.discardDocument(id);
    }

    @DeleteMapping(FORGET_DOCUMENT_URI)
    @ApiOperation(DELETE_FORGET_DOCUMENT_DESCRIPTION)
    public void forgetDocument(@PathVariable String id) {
        documentService.forgetDocument(id);
    }

    @GetMapping(RESEND_AUTHORIZATION_CODES_URI)
    @ApiOperation(GET_RESEND_AUTHORIZATION_CODES_DESCRIPTION)
    public void resendAuthorizationCodes(@PathVariable String id) {
        documentService.resendAuthorizationCodes(id);
    }

    @GetMapping(DOCUMENT_RECIPIENTS_URI)
    @ApiOperation(GET_DOCUMENT_RECIPIENTS_DESCRIPTION)
    public PaginatedResponse<DocumentRecipient> getDocumentRecipients(@PathVariable String id, DocumentRecipientSearchCriteria filters) {
        return documentService.getDocumentRecipients(id, filters);
    }

    @PostMapping(RESEND_NOTIFICATIONS_URI)
    @ApiOperation(POST_RESEND_NOTIFICATIONS_DESCRIPTION)
    public void resendNotifications(@PathVariable String id, @RequestBody RecipientsToNotify recipients) {
        documentService.resendNotifications(id, recipients);
    }

    @GetMapping(NOTIFICATIONS_HISTORY_URI)
    @ApiOperation(GET_NOTIFICATIONS_HISTORY_DESCRIPTION)
    public PaginatedResponse<NotificationStatus> getNotificationHistory(@PathVariable String id, NotificationHistorySearchCriteria filters) {
        return documentService.getDocumentNotificationsStatus(id, filters);
    }

    @PatchMapping(DOCUMENT_WITH_ID_URI)
    @ApiOperation(PATCH_DOCUMENT_DESCRIPTION)
    public void updateDocument(
            @PathVariable String id,
            @JsonView(DocumentViews.UpdateDocument.class) @RequestBody Document documentInfo) {
        documentService.updateDocument(id, documentInfo);
    }

}