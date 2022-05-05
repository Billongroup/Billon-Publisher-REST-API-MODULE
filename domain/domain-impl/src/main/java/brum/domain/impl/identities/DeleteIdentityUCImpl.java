package brum.domain.impl.identities;

import brum.domain.identities.DeleteIdentityUC;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentityStatus;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.IdentityPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeleteIdentityUCImpl implements DeleteIdentityUC {

    private final IdentityPersistenceGateway identityPersistenceGateway;

    public DeleteIdentityUCImpl(IdentityPersistenceGateway identityPersistenceGateway) {
        this.identityPersistenceGateway = identityPersistenceGateway;
    }

    @Override
    public void deleteIdentity(String externalId) {
        Identity identityFromDB = identityPersistenceGateway.getIdentityByExternalId(externalId);
        if (identityFromDB == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (identityFromDB.getStatus().equals(IdentityStatus.PUBLISHED)) {
            throw new BRUMGeneralException(ErrorStatusCode.IDENTITY_PUBLISHED_ON_DLT, LocalDateTime.now());
        }
        identityPersistenceGateway.deleteIdentityById(identityFromDB.getId());
    }
}
