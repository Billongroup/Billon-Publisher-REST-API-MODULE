package brum.domain.recipients;

import brum.model.dto.common.DataFile;
import brum.model.dto.recipients.Recipient;
import brum.model.exception.validation.ValidationException;

import java.util.List;

public interface ValidateRecipientsUC {
    void getFileValidity(DataFile recipientFile);
    List<ValidationException> validateRecipientList(List<Recipient> recipientList);
}
