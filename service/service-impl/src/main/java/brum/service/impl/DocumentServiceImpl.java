package brum.service.impl;

import brum.domain.documents.*;
import brum.domain.notifications.GetNotificationsUC;
import brum.domain.recipients.GetDocumentRecipientsUC;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.documents.DocumentType;
import brum.model.dto.notifications.NotificationStatus;
import brum.model.dto.notifications.NotificationHistorySearchCriteria;
import brum.model.dto.recipients.DocumentRecipient;
import brum.model.dto.recipients.DocumentRecipientSearchCriteria;
import brum.model.dto.recipients.RecipientsToNotify;
import brum.model.dto.tree.DocumentTree;
import brum.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final GetDocumentListUC getDocumentListUC;
    private final GetDocumentUC getDocumentUC;
    private final PrepareDocumentUC prepareDocumentUC;
    private final PublishDocumentUC publishDocumentUC;
    private final DiscardDocumentUC discardDocumentUC;
    private final ForgetDocumentUC forgetDocumentUC;
    private final ResendNotificationUC resendNotificationUC;
    private final UpdateRecipientsUC updateRecipientsUC;
    private final GetNotificationsUC getNotificationsUC;
    private final GetDocumentRecipientsUC getDocumentRecipientsUC;
    private final UpdateDocumentUC updateDocumentUC;

    public DocumentServiceImpl(GetDocumentListUC getDocumentListUC, GetDocumentUC getDocumentUC,
                               PrepareDocumentUC prepareDocumentUC, PublishDocumentUC publishDocumentUC,
                               DiscardDocumentUC discardDocumentUC, ForgetDocumentUC forgetDocumentUC,
                               ResendNotificationUC resendNotificationUC, UpdateRecipientsUC updateRecipientsUC,
                               GetNotificationsUC getNotificationsUC, GetDocumentRecipientsUC getDocumentRecipientsUC, UpdateDocumentUC updateDocumentUC) {
        this.getDocumentListUC = getDocumentListUC;
        this.getDocumentUC = getDocumentUC;
        this.prepareDocumentUC = prepareDocumentUC;
        this.publishDocumentUC = publishDocumentUC;
        this.discardDocumentUC = discardDocumentUC;
        this.forgetDocumentUC = forgetDocumentUC;
        this.resendNotificationUC = resendNotificationUC;
        this.updateRecipientsUC = updateRecipientsUC;
        this.getNotificationsUC = getNotificationsUC;
        this.getDocumentRecipientsUC = getDocumentRecipientsUC;
        this.updateDocumentUC = updateDocumentUC;
    }

    @Override
    public PaginatedResponse<Document> getDocumentList(DocumentSearchCriteria searchCriteria) {
        return getDocumentListUC.getDocumentList(searchCriteria);
    }

    @Override
    public Document getDocument(String blockchainAddress) {
        return getDocumentUC.getDocument(blockchainAddress);
    }

    @Override
    public Document preparePublicDocument(DataFile document, DataFile contactDetails, Document documentInfo, String publisherUsername) {
        return prepareDocumentUC.prepareDocument(
                document, contactDetails, documentInfo, publisherUsername, DocumentType.PUBLIC);
    }

    @Override
    public Document preparePrivateDocument(DataFile document, Document documentInfo, String publisherUsername) {
        return prepareDocumentUC.prepareDocument(document, null, documentInfo, publisherUsername, DocumentType.PRIVATE);
    }

    @Override
    public void publishPublicDocument(byte[] document, String jobId, String publisherUsername) {
        publishDocumentUC.publishDocument(document, jobId, publisherUsername, DocumentType.PUBLIC);
    }

    @Override
    public void publishPrivateDocument(byte[] document, String jobId, String publisherUsername) {
        publishDocumentUC.publishDocument(document, jobId, publisherUsername, DocumentType.PRIVATE);
    }

    @Override
    public DataFile downloadDocumentFile(String id) {
        return getDocumentUC.downloadDocumentFile(id);
    }

    @Override
    public DataFile downloadRecipientsFile(String id) {
        return getDocumentUC.downloadNotificationReceiversFile(id);
    }

    @Override
    public void updateRecipientsFile(String id, DataFile recipientsFile) {
        updateRecipientsUC.updateRecipientsFile(id, recipientsFile);
    }

    @Override
    public DataFile downloadDocumentsReport(DocumentSearchCriteria searchCriteria) {
        return getDocumentListUC.downloadDocumentsReport(searchCriteria);
    }

    @Override
    public void discardDocument(String jobId) {
        discardDocumentUC.discardDocument(jobId);
    }

    @Override
    public void forgetDocument(String blockchainAddress) {
        forgetDocumentUC.forgetDocument(blockchainAddress);
    }

    @Override
    public void resendAuthorizationCodes(String blockchainAddress) {
        resendNotificationUC.resendAuthorizationCodes(blockchainAddress);
    }

    @Override
    public PaginatedResponse<DocumentRecipient> getDocumentRecipients(String blockchainAddress, DocumentRecipientSearchCriteria filters) {
        return getDocumentRecipientsUC.getDocumentRecipients(blockchainAddress, filters);
    }

    @Override
    public void resendNotifications(String blockchainAddress, RecipientsToNotify recipients) {
        resendNotificationUC.resendNotifications(blockchainAddress, recipients);
    }

    @Override
    public PaginatedResponse<NotificationStatus> getDocumentNotificationsStatus(String blockchainAddress, NotificationHistorySearchCriteria filters) {
        return getNotificationsUC.getDocumentNotificationHistory(blockchainAddress, filters);
    }

    @Override
    public void updateDocument(String id, Document documentInfo) {
        updateDocumentUC.updateDocument(id, documentInfo);
    }

    @Override
    public DocumentTree getTree(String blockchainAddress) {
        return getDocumentUC.getDocumentTree(blockchainAddress);
    }


}
