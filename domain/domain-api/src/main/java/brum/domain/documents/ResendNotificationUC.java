package brum.domain.documents;

import brum.model.dto.recipients.RecipientsToNotify;

public interface ResendNotificationUC {
    void resendNotifications(String blockchainAddress, RecipientsToNotify recipients);
    void resendAuthorizationCodes(String blockchainAddress);
}
