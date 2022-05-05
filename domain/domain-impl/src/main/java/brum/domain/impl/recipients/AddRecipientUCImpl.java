package brum.domain.impl.recipients;

import brum.common.utils.ExternalId;
import brum.domain.recipients.AddRecipientUC;
import brum.model.dto.documents.Document;
import brum.model.dto.recipients.DocumentNotificationStatus;
import brum.model.dto.recipients.Recipient;
import brum.persistence.RecipientPersistenceGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddRecipientUCImpl implements AddRecipientUC {

    private final RecipientPersistenceGateway recipientPersistenceGateway;

    public AddRecipientUCImpl(RecipientPersistenceGateway recipientPersistenceGateway) {
        this.recipientPersistenceGateway = recipientPersistenceGateway;
    }

    @Override
    public void addRecipients(Document document, DocumentNotificationStatus status) {
        if (document.getNotificationReceivers().isEmpty()) {
            return;
        }
        List<Recipient> recipients = document.getNotificationReceivers();
        Set<String> extIdSet = recipients.stream().map(Recipient::getExternalId).collect(Collectors.toSet());

        List<String> dbExtIdList = recipientPersistenceGateway.getExtIdsByExtIdSet(extIdSet);
        List<Recipient> newRecipients = recipients.stream().filter(r -> !dbExtIdList.contains(r.getExternalId())).collect(Collectors.toList());
        recipientPersistenceGateway.saveRecipients(newRecipients);
        recipientPersistenceGateway.bindRecipientsWithDocument(document, status);
    }

}
