package brum.domain.impl.identities;

import brum.common.utils.ExternalId;
import brum.domain.file.resolvers.IdentityFileResolver;
import brum.domain.identities.AddIdentityUC;
import brum.domain.validator.IdentityValidator;
import brum.model.dto.common.DataFile;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentityStatus;
import brum.model.dto.users.User;
import brum.model.exception.*;
import brum.model.exception.validation.*;
import brum.persistence.IdentityPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static brum.model.exception.validation.ValidationErrorType.*;

@Service
public class AddIdentityUCImpl implements AddIdentityUC {

    private final IdentityPersistenceGateway identityPersistenceGateway;

    public AddIdentityUCImpl(IdentityPersistenceGateway identityPersistenceGateway) {
        this.identityPersistenceGateway = identityPersistenceGateway;
    }

    @Override
    public void addIdentity(Identity identity, String creatorUsername) {

        Map<InvalidField, ValidationErrorType> validationErrors = IdentityValidator.validateIdentity(identity);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors, LocalDateTime.now());
        }
        addDefaultData(identity, creatorUsername);
        try {
            identityPersistenceGateway.saveIdentity(identity);
        } catch (UniqueConstraintException e) {
            throw new ValidationException(InvalidField.valueOf(e.getConstraint().name()), NON_UNIQUE, LocalDateTime.now());
        }
    }

    @Override
    public void addIdentitiesFromFile(DataFile file, String creatorUsername) {
        if (file == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NO_FILE_PASSED, LocalDateTime.now());
        }

        List<Identity> identityList = new IdentityFileResolver(file.getFile(), file.getFileType()).resolve();
        addIdentityList(identityList, creatorUsername);
    }

    private void addIdentityList(List<Identity> identityList, String creatorUsername) {
        int count = 0;
        int success = 0;
        List<ValidationException> validationExceptions = new ArrayList<>();

        for (Identity identity : identityList) {
            count++;
            Map<InvalidField, ValidationErrorType> validationResult = IdentityValidator.validateIdentity(identity);
            if (!validationResult.isEmpty()) {
                validationExceptions.add(new ValidationException(identity, count, validationResult));
                continue;
            }
            addDefaultData(identity, creatorUsername);
            try {
                identityPersistenceGateway.saveIdentity(identity);
                success++;
            } catch (UniqueConstraintException e) {
                validationExceptions.add(new ValidationException(identity, count, InvalidField.valueOf(e.getConstraint().name()), NON_UNIQUE));
            }
        }
        if (identityList.size() != success) {
            throw new ListValidationException(success, identityList.size(), validationExceptions, LocalDateTime.now());
        }
    }

    private void addDefaultData(Identity identity, String creatorUsername) {
        User creator = new User();
        creator.setUsername(creatorUsername);
        identity.setCreatedBy(creator);
        identity.setExternalId(ExternalId.IDENTITY.generateId());
        identity.setCreatedAt(LocalDateTime.now());
        identity.setIsGdprSuspended(false);
        identity.setStatus(IdentityStatus.SHELL);
        if (identity.getIsActive() == null) {
            identity.setIsActive(true);
        }
    }

}
