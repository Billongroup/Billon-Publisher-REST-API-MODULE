package brum.domain.recipients;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.recipients.DocumentRecipient;
import brum.model.dto.recipients.DocumentRecipientSearchCriteria;

public interface GetDocumentRecipientsUC {
    PaginatedResponse<DocumentRecipient> getDocumentRecipients(String blockchainAddress, DocumentRecipientSearchCriteria filters);
}
