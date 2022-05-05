package brum.proxy;

import brum.model.dto.common.BemResponse;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.documents.DocumentType;
import brum.model.dto.notifications.*;
import brum.model.dto.recipients.Recipient;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;

import java.util.List;

public interface DocumentProxy {
    BemResponse<Document> getDocumentStatus(String jobId);
    BemResponse<PaginatedResponse<Document>> getDocumentList(DocumentSearchCriteria searchCriteria, String categoryPath);
    BemResponse<Document> getDocumentByBlockchainAddress(String blockchainAddress, boolean fetchSource);
    BemResponse<Document> getDocumentByJobId(String jobId, boolean fetchSource);
    BemResponse<Document> prepareDocument(Document documentInfo);
    BemResponse<Document> publishDocument(Document documentInfo);
    BemResponse<Object> discardDocument(String jobId);
    BemResponse<Object> resendAuthorizationCodes(Document document);
    BemResponse<String> forgetDocument(String blockchainAddress);
    BemResponse<List<YearlyReportEntry>> getYearlyReport();
    BemResponse<List<FairUsageReportEntry>> getFairUsageReport();
    BemResponse<PaginatedResponse<FullInfoReportEntry>> getCustomReport(CustomReportSearchCriteria searchCriteria);
    BemResponse<Object> resendDocumentNotifications(String blockchainAddress, List<Recipient> recipients);
    BemResponse<PaginatedResponse<NotificationStatus>> getDocumentNotificationsStatus(String blockchainAddress, NotificationStatusFilters filters);
    BemResponse<NotificationContent> getNotificationContent(String jobId, NotificationContentFilter filter);
    void updateDocument(String jobId, Document documentInfo, DocumentType type);
}
