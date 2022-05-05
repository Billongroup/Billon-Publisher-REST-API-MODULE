package brum.proxy.mapper;

import brum.common.utils.CalendarUtils;
import brum.model.dto.documents.*;
import brum.model.dto.identities.Identity;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunit.dm.common.module.document.DocumentFilterDocumentDto;
import com.zunit.dm.common.module.dto.*;
import com.zunit.dm.common.module.enums.ReportTypeEnum;
import com.zunit.dm.publisher.services.documents.dto.CustomReportParamsDto;
import com.zunit.dm.publisher.services.documents.dto.DocumentDataDto;
import https.types_dm_billongroup.InternalDocumentType;
import https.types_dm_billongroup.InternalSystemStatusErrors;
import org.springframework.util.StringUtils;

import java.util.*;

import static https.types_dm_billongroup.InternalSystemStatusErrors.*;

public class DocumentProxyMapper {
    private DocumentProxyMapper() {
    }

    private static final List<InternalSystemStatusErrors> PUBLISHING_INITIATED_STATUSES = Arrays.asList(
            PUBLISHING_INITIATED, UNINITIALIZED, PUBLISHING_PREPARED);
    private static final List<InternalSystemStatusErrors> FORGETTING_IN_PROGRESS_STATUSES = Arrays.asList(
            FORGETTING_INITIATED, FORGETTING_IN_PROGRESS);
    private static final List<InternalSystemStatusErrors> FORGETTING_OK_STATUSES = Arrays.asList(
            FORGETTING_OK, DOCUMENT_ALREADY_FORGOTTEN, JOB_ID_NOT_FOUND, ACCESS_IMPOSSIBLE, INVALID_JOB_ID);

    public static DocumentFilterDocumentDto mapSearchCriteria(DocumentSearchCriteria searchCriteria, String categoryPath) {
        return DocumentSearchCriteriaMapper.mapSearchCriteria(searchCriteria, categoryPath);
    }

    public static List<Document> map(List<DocumentDto> documentList) {
        List<Document> result = new ArrayList<>();
        if (documentList == null) {
            return result;
        }
        for (DocumentDto document : documentList) {
            result.add(map(document, false));
        }
        return result;
    }

    public static Document map(DocumentDto documentDto) {
        return map(documentDto, true);
    }

    private static Document map(DocumentDto documentDto, boolean getHistory) {
        JsonNode node;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            node = objectMapper.readTree(documentDto.getDocumentMetadata().getAdditionalDetails());
        } catch (JsonProcessingException e) {
            node = null;
        }
        Document result = new Document();
        result.setJobId(documentDto.getDmJobId());
        result.setDocumentBlockchainAddress(documentDto.getDocumentBlockchainAddress());
        result.setTitle(documentDto.getDocumentMetadata().getTitle());
        result.setVersionName(documentDto.getDocumentMetadata().getVersionName());
        result.setFileName(documentDto.getFileName());
        result.setCategory(documentDto.getDocumentMetadata().getDocumentMainCategory());
        result.setPublishingPersonId(documentDto.getDocumentMetadata().getPublishingPersonId());
        result.setSignatoryId(documentDto.getDocumentMetadata().getSignatoryID());
        result.setPublicationDelayMinutes(documentDto.getPublicationDelayMinutes());
        result.setPublicationStartDate(CalendarUtils.epochToLocalDateTime(documentDto.getPublishStartDate()));
        result.setPublicationDate(CalendarUtils.epochToLocalDateTime(documentDto.getDocumentMetadata().getPublicationDate()));
        result.setValidSince(CalendarUtils.epochToLocalDateTime(documentDto.getDocumentMetadata().getLegalValidityStartDate()));
        result.setValidUntil(CalendarUtils.epochToLocalDateTime(documentDto.getDocumentMetadata().getExpirationDate()));
        result.setRetainUntil(CalendarUtils.epochToLocalDateTime(documentDto.getDocumentMetadata().getRetentionDate()));
        result.setDocumentType(DocumentType.valueOf(documentDto.getDocumentType().name()));
        result.setPublicationMode(PublicationMode.valueOf(documentDto.getPublicationMode().name()));
        result.setDocumentPublicationStatus(PublicationStatus.valueOf(documentDto.getDocumentPublicationStatus().name()));
        result.setCkkStatus(documentDto.getCkkStatus());
        result.setStatus(DocumentStatus.valueOf(documentDto.getStatus().name()));
        result.setAdditionalDetails(node);
        result.setRead(documentDto.isReaded());
        result.setDraft(documentDto.isDraft());

        result.setFavourite(documentDto.isFavourite());
        result.setReceiverPublisherId(documentDto.getReceiverPublisherId());
        result.setSenderPublisherId(documentDto.getPublishedBy());
        if (documentDto.getSignedDocument() != null) {
            result.setFileSize(documentDto.getSignedDocument().length);
        }
        if (getHistory) {
            result.setHistory(mapRelatedDocuments(documentDto.getRelatedDocuments()));
        }
        if (result.getDocumentType().equals(DocumentType.PRIVATE)) {
            Identity identity = new Identity();
            identity.setDocumentNumber(documentDto.getIdentityDto().getPublisherCif());
            result.setIdentity(identity);
        }
        return result;

    }

    public static Document mapGetStatus(DocumentDto document, InternalSystemStatusErrors status) {
        Document result = new Document();
        if (document != null) {
            result.setDocumentBlockchainAddress(document.getDocumentBlockchainAddress());
            result.setDocumentPublicationStatus(PublicationStatus.valueOf(document.getDocumentPublicationStatus().name()));
            result.setForgettingStatus(mapForgettingStatus(document.getForgettingStatus()));
            return result;
        }
        PublicationStatus publicationStatus;
        if (PUBLISHING_INITIATED_STATUSES.contains(status)) {
            publicationStatus = PublicationStatus.PUBLISHING_INITIATED;
        } else {
            publicationStatus = PublicationStatus.PUBLISHING_ERROR;
        }
        result.setDocumentPublicationStatus(publicationStatus);
        result.setForgettingStatus(mapForgettingStatus(status.value()));
        return result;
    }

    private static ForgettingStatus mapForgettingStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }
        if (FORGETTING_OK_STATUSES.stream().anyMatch(s -> s.value().equals(status))) {
            return ForgettingStatus.FORGETTING_OK;
        }
        if (FORGETTING_IN_PROGRESS_STATUSES.stream().anyMatch(s -> s.value().equals(status))) {
            return ForgettingStatus.FORGETTING_IN_PROGRESS;
        }
        return ForgettingStatus.FORGETTING_EXCEPTION;
    }

    public static Document getDocumentFile(DocumentDto documentDto) {
        Document result = new Document();
        result.setSource(documentDto.getSignedDocument());
        result.setFileName(documentDto.getFileName());
        return result;
    }

    public static DocumentDataDto map(Document documentInfo) {
        return DocumentDataMapper.mapToPrepare(documentInfo);
    }

    public static Document mapPrepareResponse(DocumentDto document) {
        Document result = new Document();
        result.setJobId(document.getDmJobId());
        return result;
    }

    public static DocumentDataDto mapToResendAuthorizationCodes(Document document) {
        DocumentDataDto result = new DocumentDataDto();
        DocumentDto documentDto = new DocumentDto(InternalDocumentType.PRIVATE);
        documentDto.setIdentityDto(new IdentityDto());
        documentDto.getIdentityDto().setContactDetailsList(mapToContactDetailsList(document.getIdentity()));
        documentDto.setDocumentBlockchainAddress(document.getDocumentBlockchainAddress());
        result.setDocumentDto(documentDto);
        return result;
    }

    public static List<YearlyReportEntry> mapGetYearlyPublicationReport(List<PublicationReportEntryDto> report) {
        List<YearlyReportEntry> result = new ArrayList<>();
        for (PublicationReportEntryDto entry : report) {
            YearlyReportEntry resultEntry = new YearlyReportEntry();
            resultEntry.setName(entry.getName());
            resultEntry.setPublicDocumentPublished(entry.getPublicDocumentsPublications());
            resultEntry.setPrivateDocumentPublished(entry.getPrivateDocumentsPublications());
            result.add(resultEntry);
        }
        return result;
    }

    public static List<FairUsageReportEntry> mapGetFairUsageReport(List<FairUsageReportEntryDto> report) {
        List<FairUsageReportEntry> result = new ArrayList<>();
        for (FairUsageReportEntryDto entry : report) {
            FairUsageReportEntry resultEntry = new FairUsageReportEntry();
            resultEntry.setName(entry.getName());
            resultEntry.setMemoryUsagePercent(entry.getMemoryUsagePercent());
            resultEntry.setAvailableMemory(entry.getAvailableMemory());
            result.add(resultEntry);
        }
        return result;
    }

    public static CustomReportParamsDto mapCustomReportSearchCriteria(CustomReportSearchCriteria searchCriteria) {
        CustomReportParamsDto result = new CustomReportParamsDto();
        result.setReportType(ReportTypeEnum.valueOf(searchCriteria.getReportType().name()));
        if (searchCriteria.getPagination() != null) {
            result.setPageSize(searchCriteria.getPagination().getLimit());
            result.setPageOffset(searchCriteria.getPagination().getPage());
        }
        if (searchCriteria.getDateRange() != null) {
            result.setFrom(searchCriteria.getDateRange().getFrom());
            result.setTo(searchCriteria.getDateRange().getTo());
        }
        return result;
    }

    public static List<FullInfoReportEntry> mapGetCustomReport(List<FullInfoReportEntryDto> report) {
        List<FullInfoReportEntry> result = new ArrayList<>();
        for (FullInfoReportEntryDto entry : report) {
            FullInfoReportEntry resultEntry = new FullInfoReportEntry();
            resultEntry.setStartDate(entry.getStartDate());
            resultEntry.setEndDate(entry.getEndDate());
            resultEntry.setPublicDocumentsPublications(entry.getPublicDocumentsPublications());
            resultEntry.setPrivateDocumentsPublications(entry.getPrivateDocumentsPublications());
            resultEntry.setAllDocumentsPublications(entry.getAllDocumentsPublications());
            resultEntry.setPublicDocumentsSize(entry.getPublicDocumentsSize());
            resultEntry.setPrivateDocumentsSize(entry.getPrivateDocumentsSize());
            resultEntry.setAllDocumentsSize(entry.getAllDocumentsSize());
            resultEntry.setDocumentSystemSize(entry.getDocumentSystemSize());
            resultEntry.setAveragePublicationTimePublic(entry.getAveragePublicationTimePublic());
            resultEntry.setAveragePublicationTimePrivate(entry.getAveragePublicationTimePrivate());
            result.add(resultEntry);
        }
        return result;
    }

    private static List<Document> mapRelatedDocuments(List<DocumentDto> relatedDocuments) {
        List<Document> result = new ArrayList<>(relatedDocuments.size());
        for (DocumentDto relatedDocument : relatedDocuments) {
            Document entry = new Document();
            entry.setTitle(relatedDocument.getDocumentMetadata().getTitle());
            entry.setDocumentBlockchainAddress(relatedDocument.getDocumentBlockchainAddress());
            entry.setVersionName(relatedDocument.getDocumentMetadata().getVersionName());
            entry.setPublicationDate(CalendarUtils.epochToLocalDateTime(relatedDocument.getDocumentMetadata().getPublicationDate()));
            entry.setValidUntil(CalendarUtils.epochToLocalDateTime(relatedDocument.getDocumentMetadata().getExpirationDate()));
            entry.setPublicationMode(PublicationMode.valueOf(relatedDocument.getPublicationMode().name()));
            entry.setStatus(DocumentStatus.valueOf(relatedDocument.getStatus().name()));
            result.add(entry);
        }
        return result;
    }

    private static List<ContactDetailsDto> mapToContactDetailsList(Identity identity) {
        List<ContactDetailsDto> result = new ArrayList<>();
        ContactDetailsDto contactDetails = new ContactDetailsDto();
        contactDetails.setEmailAddress(identity.getEmail());
        contactDetails.setPhoneNumber(identity.getPhoneNumber());
        result.add(contactDetails);
        return result;
    }

}
