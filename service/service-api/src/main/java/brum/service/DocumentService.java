package brum.service;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.notifications.NotificationStatus;
import brum.model.dto.notifications.NotificationHistorySearchCriteria;
import brum.model.dto.recipients.DocumentRecipient;
import brum.model.dto.recipients.DocumentRecipientSearchCriteria;
import brum.model.dto.recipients.RecipientsToNotify;
import brum.model.dto.tree.DocumentTree;

public interface DocumentService {
    PaginatedResponse<Document> getDocumentList(DocumentSearchCriteria searchCriteria);
    Document getDocument(String blockchainAddress);
    Document preparePublicDocument(DataFile document, DataFile contactDetails, Document documentInfo, String publisherUsername);
    Document preparePrivateDocument(DataFile document, Document documentInfo, String publisherUsername);
    void publishPublicDocument(byte[] document, String jobId, String publisherUsername);
    void publishPrivateDocument(byte[] document, String jobId, String publisherUsername);
    DataFile downloadDocumentFile(String id);
    DataFile downloadRecipientsFile(String id);
    void updateRecipientsFile(String id, DataFile recipientsFile);
    DataFile downloadDocumentsReport(DocumentSearchCriteria searchCriteria);
    void discardDocument(String jobId);
    void forgetDocument(String blockchainAddress);
    void resendAuthorizationCodes(String blockchainAddress);
    PaginatedResponse<DocumentRecipient> getDocumentRecipients(String blockchainAddress, DocumentRecipientSearchCriteria filters);
    void resendNotifications(String blockchainAddress, RecipientsToNotify recipients);
    PaginatedResponse<NotificationStatus> getDocumentNotificationsStatus(String blockchainAddress, NotificationHistorySearchCriteria filters);
    void updateDocument(String id, Document documentInfo);
    DocumentTree getTree(String blockchainAddress);
}
