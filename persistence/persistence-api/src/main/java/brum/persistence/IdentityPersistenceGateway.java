package brum.persistence;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentitySearchCriteria;

import java.util.List;

public interface IdentityPersistenceGateway {
    List<Identity> getAllIdentities();
    PaginatedResponse<Identity> getFilteredIdentities(IdentitySearchCriteria searchCriteria);
    Identity getIdentityByExternalId(String externalId);
    Identity getIdentityByDocumentNumber(String documentNumber);
    void saveIdentity(Identity identity);
    void deleteIdentityById(Long id);
}
