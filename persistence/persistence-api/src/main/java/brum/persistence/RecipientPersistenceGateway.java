package brum.persistence;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.recipients.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RecipientPersistenceGateway {
    List<Recipient> getDocumentRecipients(String documentId);
    List<DocumentRecipient> getDocumentRecipients(String documentId, Map<String, List<ContactDetailsType>> idContactDetailsMap);
    PaginatedResponse<DocumentRecipient> getDocumentRecipients(String documentId, DocumentRecipientSearchCriteria filters);
    Map<String, Recipient> getIdRecipientMapByExtIdSet(Set<String> extIdSet);
    List<String> getExtIdsByExtIdSet(Set<String> extIdSet);
    void saveRecipients(List<Recipient> recipients);
    void bindRecipientsWithDocument(Document document, DocumentNotificationStatus status);
    void updateRecipients(Map<String, Recipient> updateData, String documentId);
    void setContactDetailsSentStatus(Set<Long> contactDetailsIdSet, String blockchainAddress);
}
