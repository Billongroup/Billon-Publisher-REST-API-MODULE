package brum.service.impl;

import brum.domain.recipients.ValidateRecipientsUC;
import brum.model.dto.common.DataFile;
import brum.service.RecipientService;
import org.springframework.stereotype.Service;

@Service
public class RecipientServiceImpl implements RecipientService {

    private final ValidateRecipientsUC validateRecipientsUC;

    public RecipientServiceImpl(ValidateRecipientsUC validateRecipientsUC) {
        this.validateRecipientsUC = validateRecipientsUC;
    }

    @Override
    public void getFileValidity(DataFile recipientFile) {
        validateRecipientsUC.getFileValidity(recipientFile);
    }
}
