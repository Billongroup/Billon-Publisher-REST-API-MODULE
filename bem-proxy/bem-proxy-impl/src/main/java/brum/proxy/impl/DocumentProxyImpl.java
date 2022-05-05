package brum.proxy.impl;

import brum.model.dto.common.BemResponse;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.notifications.*;
import brum.model.dto.recipients.Recipient;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.proxy.DocumentProxy;
import brum.proxy.mapper.DocumentProxyMapper;
import brum.proxy.mapper.IdentityProxyMapper;
import brum.proxy.mapper.NotificationsProxyMapper;
import com.zunit.dm.common.module.dto.*;
import com.zunit.dm.publisher.services.documents.dto.ResponseDto;
import com.zunit.dm.publisher.services.documents.facade.mvp.MVPDocumentsFacade;
import com.zunit.dm.publisher.services.notification.dto.FilteredNotificationsDto;
import com.zunit.dm.publisher.services.notification.dto.NotificationContentDto;
import https.types_dm_billongroup.DocumentType;
import https.types_dm_billongroup.InternalSystemStatusErrors;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static https.types_dm_billongroup.InternalSystemStatusErrors.*;

@Service
public class DocumentProxyImpl implements DocumentProxy {

    private final MVPDocumentsFacade mvpDocumentsFacade;

    private static final List<InternalSystemStatusErrors> DOCUMENT_NOT_FOUND_ERRORS = new ArrayList<>(Arrays.asList(
            JOB_ID_NOT_FOUND, DOCUMENT_NOT_FOUND, INVALID_JOB_ID, ACCESS_IMPOSSIBLE));

    public DocumentProxyImpl(MVPDocumentsFacade mvpDocumentsFacade) {
        this.mvpDocumentsFacade = mvpDocumentsFacade;
    }

    @Override
    public BemResponse<Document> getDocumentStatus(String jobId) {
        ResponseDto response = mvpDocumentsFacade.getPublishStatus(jobId);
        if (response == null) {
            return BemResponse.<Document>builder().status(ErrorStatusCode.INTERNAL_SERVER_ERROR).build();
        }
        InternalSystemStatusErrors error = response.getErrorMessage().orElse(null);
        if (error == null) {
            return BemResponse.<Document>builder().status(ErrorStatusCode.INTERNAL_SERVER_ERROR).build();
        }
        DocumentDto document = response.getDocumentDto().orElse(null);
        return BemResponse.success(DocumentProxyMapper.mapGetStatus(document, error));
    }

    @Override
    public BemResponse<PaginatedResponse<Document>> getDocumentList(DocumentSearchCriteria searchCriteria, String categoryPath) {
        FilteredDocumentsDto response = mvpDocumentsFacade.getFilteredDocuments(DocumentProxyMapper.mapSearchCriteria(searchCriteria, categoryPath));
        BemResponse<PaginatedResponse<Document>> validate = checkStatusCode(response.getSystemStatus(), SUCCESS);
        if (!validate.isSuccess()) {
            return validate;
        }
        Long count = response.getNumberOfDocumentsFound().orElseThrow(
                () -> new BRUMGeneralException(ErrorStatusCode.INTERNAL_SERVER_ERROR, LocalDateTime.now()));
        PaginatedResponse<Document> result = new PaginatedResponse<>();
        result.setCount(count);
        result.setRows(DocumentProxyMapper.map(response.getList()));
        return BemResponse.success(result);
    }

    @Override
    public BemResponse<Document> getDocumentByBlockchainAddress(String blockchainAddress, boolean fetchSource) {
        return getDocument(blockchainAddress, null, fetchSource);
    }

    @Override
    public BemResponse<Document> getDocumentByJobId(String jobId, boolean fetchSource) {
        return getDocument(null, jobId, fetchSource);
    }

    private BemResponse<Document> getDocument(String blockchainAddress, String jobId, boolean fetchSource) {
        ResponseDto response = mvpDocumentsFacade.getPublisherDocument(blockchainAddress, jobId);
        BemResponse<Document> validate = checkStatusCode(response.getErrorMessage(), SUCCESS);
        if (!validate.isSuccess()) {
            return validate;
        }
        DocumentDto documentDto = getDocumentFromResponse(response);
        Document document;
        if (fetchSource) {
            document = DocumentProxyMapper.getDocumentFile(documentDto);
        } else {
            document = DocumentProxyMapper.map(documentDto);
        }
        return BemResponse.success(document);
    }

    @Override
    public BemResponse<Document> prepareDocument(Document documentInfo) {
        ResponseDto response = mvpDocumentsFacade.prepareDocument(
                DocumentProxyMapper.map(documentInfo),
                DocumentType.fromValue(documentInfo.getDocumentType().name()));
        BemResponse<Document> validate = checkResponse(response, PUBLISHING_PREPARED);
        if (!validate.isSuccess()) {
            return validate;
        }
        DocumentDto documentDto = getDocumentFromResponse(response);
        return BemResponse.success(DocumentProxyMapper.mapPrepareResponse(documentDto));
    }

    @Override
    public BemResponse<Document> publishDocument(Document documentInfo) {
        ResponseDto response = mvpDocumentsFacade.publishDocument(
                DocumentProxyMapper.map(documentInfo),
                DocumentType.fromValue(documentInfo.getDocumentType().name()));
        BemResponse<Document> validate = checkResponse(response, PUBLISHING_INITIATED);
        if (!validate.isSuccess()) {
            return validate;
        }
        DocumentDto documentDto = getDocumentFromResponse(response);
        return BemResponse.success(DocumentProxyMapper.mapPrepareResponse(documentDto));
    }

    @Override
    public BemResponse<Object> discardDocument(String jobId) {
        ResponseDto response = mvpDocumentsFacade.discardDocument(jobId);
        return checkResponse(response, SUCCESS, DOCUMENT_ALREADY_DISCARDED);
    }

    @Override
    public BemResponse<Object> resendAuthorizationCodes(Document document) {
        ResponseDto response = mvpDocumentsFacade.resendAuthorizationCodes(DocumentProxyMapper.mapToResendAuthorizationCodes(document));
        return checkResponse(response, SEARCH_IN_PROGRESS, SEARCH_INITIATED);
    }

    @Override
    public BemResponse<String> forgetDocument(String blockchainAddress) {
        ResponseDto response = mvpDocumentsFacade.forgetDocument(blockchainAddress);
        checkResponse(response, FORGETTING_INITIATED);
        DocumentDto document = getDocumentFromResponse(response);
        return BemResponse.success(document.getDmJobId());
    }

    @Override
    public BemResponse<List<YearlyReportEntry>> getYearlyReport() {
        List<PublicationReportEntryDto> response = mvpDocumentsFacade.generateYearlyPublicationReport();
        return BemResponse.success(DocumentProxyMapper.mapGetYearlyPublicationReport(response));
    }

    @Override
    public BemResponse<List<FairUsageReportEntry>> getFairUsageReport() {
        List<FairUsageReportEntryDto> response = mvpDocumentsFacade.generateFairUsageReport();
        return BemResponse.success(DocumentProxyMapper.mapGetFairUsageReport(response));
    }

    @Override
    public BemResponse<PaginatedResponse<FullInfoReportEntry>> getCustomReport(CustomReportSearchCriteria searchCriteria) {
        FullInfoReportDto response = mvpDocumentsFacade.generateCustomFullInfoReport(DocumentProxyMapper.mapCustomReportSearchCriteria(searchCriteria));
        PaginatedResponse<FullInfoReportEntry> result = new PaginatedResponse<>();
        result.setCount(response.getNumberOfRecords());
        result.setRows(DocumentProxyMapper.mapGetCustomReport(response.getReportEntryDtoList()));
        return BemResponse.success(result);
    }

    @Override
    public BemResponse<Object> resendDocumentNotifications(String blockchainAddress, List<Recipient> recipients) {
        ResponseDto response = mvpDocumentsFacade.resendPublicDocumentNotifications(blockchainAddress, IdentityProxyMapper.mapNotificationReceivers(recipients));
        return checkResponse(response, SUCCESS);
    }

    @Override
    public BemResponse<PaginatedResponse<NotificationStatus>> getDocumentNotificationsStatus(String blockchainAddress, NotificationStatusFilters filters) {
        FilteredNotificationsDto response = mvpDocumentsFacade.getNotificationsStatus(blockchainAddress, NotificationsProxyMapper.map(filters));
        if (response == null) {
            return BemResponse.<PaginatedResponse<NotificationStatus>>builder().status(ErrorStatusCode.NOT_FOUND).build();
        }
        PaginatedResponse<NotificationStatus> result = new PaginatedResponse<>();
        result.setCount(response.getCount());
        result.setRows(NotificationsProxyMapper.map(response.getNotificationList()));
        return BemResponse.success(result);
    }

    @Override
    public BemResponse<NotificationContent> getNotificationContent(String jobId, NotificationContentFilter filter) {
        NotificationContentDto response = mvpDocumentsFacade.getNotificationContent(NotificationsProxyMapper.map(filter, jobId));
        if (response == null) {
            return BemResponse.<NotificationContent>builder().status(ErrorStatusCode.NOT_FOUND).build();
        }
        return BemResponse.success(NotificationsProxyMapper.map(response));
    }

    @Override
    public void updateDocument(String jobId, Document documentInfo, brum.model.dto.documents.DocumentType type) {
        mvpDocumentsFacade.updateDocument(jobId, documentInfo.getFavourite(), DocumentType.fromValue(type.name()));
    }

    private DocumentDto getDocumentFromResponse(ResponseDto response) {
        return response.getDocumentDto().orElseThrow(
                () -> new BRUMGeneralException(ErrorStatusCode.INTERNAL_SERVER_ERROR, LocalDateTime.now()));
    }

    private <T> BemResponse<T> checkResponse(ResponseDto response, InternalSystemStatusErrors... expectedStatus) {
        if (response == null) {
            return BemResponse.<T>builder().status(ErrorStatusCode.INTERNAL_SERVER_ERROR).build();
        }
        return checkStatusCode(response.getErrorMessage(), expectedStatus);
    }

    private <T> BemResponse<T> checkStatusCode(Optional<InternalSystemStatusErrors> statusCode, InternalSystemStatusErrors... expectedStatus) {
        InternalSystemStatusErrors error = statusCode.orElse(null);
        if (error == null) {
            return BemResponse.<T>builder().status(ErrorStatusCode.INTERNAL_SERVER_ERROR).build();
        }
        if (DOCUMENT_NOT_FOUND_ERRORS.contains(error)) {
            return BemResponse.<T>builder().status(ErrorStatusCode.NOT_FOUND).bemStatus(error.name()).build();
        }
        if (Arrays.stream(expectedStatus).noneMatch(e -> e.equals(error))) {
            return BemResponse.bemError(error.name());
        }
        return BemResponse.success();
    }

}
