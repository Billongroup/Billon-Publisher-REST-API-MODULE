package brum.domain.recipients;

import brum.model.dto.documents.Document;
import brum.model.dto.recipients.DocumentNotificationStatus;
import brum.model.dto.recipients.Recipient;

import java.util.List;

public interface AddRecipientUC {
    void addRecipients(Document document, DocumentNotificationStatus status);
}
