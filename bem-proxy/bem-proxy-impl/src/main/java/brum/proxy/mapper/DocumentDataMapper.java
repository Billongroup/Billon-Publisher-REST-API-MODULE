package brum.proxy.mapper;

import brum.common.utils.CalendarUtils;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentType;
import com.zunit.dm.common.module.dto.*;
import com.zunit.dm.common.module.enums.PublicationType;
import com.zunit.dm.publisher.services.documents.dto.DocumentDataDto;
import https.types_dm_billongroup.InternalDocumentType;
import https.types_dm_billongroup.SupportedFileType;

public class DocumentDataMapper {
    private DocumentDataMapper() {}

    public static DocumentDataDto mapToPrepare(Document documentInfo) {
        DocumentDataDto result = new DocumentDataDto();
        DocumentDto document = new DocumentDto(InternalDocumentType.fromValue(documentInfo.getDocumentType().name()));
        document.setPublishStartDate(System.currentTimeMillis() * 1000);
        if (documentInfo.getPublicationMode() != null) {
            document.setPublicationMode(PublicationType.valueOf(documentInfo.getPublicationMode().name()));
        }
        document.setSignedDocument(documentInfo.getSource());
        document.setDmJobId(documentInfo.getJobId());
        document.setDocumentMetadata(mapDocumentMetadata(documentInfo));
        document.setExtension(SupportedFileType.PDF);
        document.setFileName(documentInfo.getFileName());
        document.setSendAuthorizationCodes(true);
        document.setReceiverPublisherId(documentInfo.getReceiverPublisherId());
        document.setDraft(Boolean.TRUE.equals(documentInfo.getDraft()));
        result.setDocumentDto(document);
        if (documentInfo.getNotificationReceivers() != null && !documentInfo.getNotificationReceivers().isEmpty()) {
            result.setNotificationReceivers(IdentityProxyMapper.mapNotificationReceivers(documentInfo.getNotificationReceivers()));
        }
        if (documentInfo.getDocumentType().equals(DocumentType.PRIVATE)) {
            document.setIdentityDto(IdentityProxyMapper.mapIdentity(documentInfo.getIdentity()));
        }
        return result;
    }

    private static DocumentMetadataDto mapDocumentMetadata(Document documentInfo) {
        DocumentMetadataDto result = new DocumentMetadataDto();
        result.setPreviousDocumentBlockchainAddress(documentInfo.getPreviousDocumentBlockchainAddress());
        result.setLegalValidityStartDate(CalendarUtils.dateToEpoch(documentInfo.getValidSince()));
        result.setExpirationDate(CalendarUtils.dateToEpoch(documentInfo.getValidUntil()));
        result.setRetentionDate(CalendarUtils.dateToEpoch(documentInfo.getRetainUntil()));
        if (documentInfo.getPublicationMode() != null) {
            result.setPublicationReason(documentInfo.getPublicationMode().name());
        }
        result.setSignatoryID(documentInfo.getSignatoryId());
        result.setTitle(documentInfo.getTitle());
        result.setVersionName(documentInfo.getVersionName());
        result.setDocumentMainCategory(documentInfo.getCategory());
        result.setPublishingPersonId(documentInfo.getPublishingPersonId());
        result.setAdditionalDetails(documentInfo.getAdditionalDetails().toString());
        return result;
    }

}
