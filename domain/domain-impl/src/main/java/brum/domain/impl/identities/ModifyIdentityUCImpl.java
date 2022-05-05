package brum.domain.impl.identities;

import brum.domain.identities.ModifyIdentityUC;
import brum.domain.validator.IdentityValidator;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentityStatus;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import brum.model.exception.validation.ValidationException;
import brum.persistence.IdentityPersistenceGateway;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ModifyIdentityUCImpl implements ModifyIdentityUC {

    private final IdentityPersistenceGateway identityPersistenceGateway;

    public ModifyIdentityUCImpl(IdentityPersistenceGateway identityPersistenceGateway) {
        this.identityPersistenceGateway = identityPersistenceGateway;
    }

    @Override
    public void modifyIdentity(Identity newData) {
        Identity identityFromDB = identityPersistenceGateway.getIdentityByExternalId(newData.getExternalId());
        validateUpdate(identityFromDB, newData);
        addMissingValuesInNewData(newData, identityFromDB);
        Map<InvalidField, ValidationErrorType> errors = IdentityValidator.validateIdentity(newData);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors, LocalDateTime.now());
        }
        identityFromDB.setFirstName(newData.getFirstName());
        identityFromDB.setLastName(newData.getLastName());
        identityFromDB.setEmail(newData.getEmail());
        identityFromDB.setPhoneNumber(newData.getPhoneNumber());
        identityFromDB.setIsActive(newData.getIsActive());
        identityPersistenceGateway.saveIdentity(identityFromDB);
    }

    @Override
    public void setIsGdprSuspended(String id, Identity isGdprSuspended) {
        Identity identity = identityPersistenceGateway.getIdentityByExternalId(id);
        if (identity == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (isGdprSuspended.getIsGdprSuspended() == null) {
            throw new ValidationException(InvalidField.IS_GDPR_SUSPENDED, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        identity.setIsGdprSuspended(isGdprSuspended.getIsGdprSuspended());
        identityPersistenceGateway.saveIdentity(identity);
    }

    public void validateUpdate(Identity identityFromDB, Identity newData) {
        if (identityFromDB == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (Boolean.TRUE.equals(identityFromDB.getIsGdprSuspended())) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_SUSPENDED, LocalDateTime.now());
        }
        if (identityFromDB.getStatus().equals(IdentityStatus.PUBLISHED) && !onlyActivityModified(identityFromDB, newData)) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_PUBLISHED_ON_DLT, LocalDateTime.now());
        }
    }

    private void addMissingValuesInNewData(Identity newData, Identity identityFromDb) {
        if (!StringUtils.hasText(newData.getDocumentNumber())) {
            newData.setDocumentNumber(identityFromDb.getDocumentNumber());
        }
        if (!StringUtils.hasText(newData.getFirstName())) {
            newData.setFirstName(identityFromDb.getFirstName());
        }
        if (!StringUtils.hasText(newData.getLastName())) {
            newData.setLastName(identityFromDb.getLastName());
        }
        if (!StringUtils.hasText(newData.getEmail())) {
            newData.setEmail(identityFromDb.getEmail());
        }
        if (!StringUtils.hasText(newData.getPhoneNumber())) {
            newData.setPhoneNumber(identityFromDb.getPhoneNumber());
        }
        if (newData.getIsActive() == null) {
            newData.setIsActive(identityFromDb.getIsActive());
        }
    }

    private boolean onlyActivityModified(Identity identityFromDB, Identity newData) {
        if (StringUtils.hasText(newData.getFirstName()) && !newData.getFirstName().equals(identityFromDB.getFirstName())) {
            return false;
        }
        if (StringUtils.hasText(newData.getLastName()) && !newData.getLastName().equals(identityFromDB.getLastName())) {
            return false;
        }
        if (StringUtils.hasText(newData.getEmail()) && !newData.getEmail().equals(identityFromDB.getEmail())) {
            return false;
        }
        return !StringUtils.hasText(newData.getPhoneNumber()) || newData.getPhoneNumber().equals(identityFromDB.getPhoneNumber());
    }
}
