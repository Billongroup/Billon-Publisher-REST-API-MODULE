package brum.service;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentitySearchCriteria;

public interface IdentityService {
    Identity getIdentity(String id);
    PaginatedResponse<Identity> getIdentities(IdentitySearchCriteria searchCriteria);
    DataFile generateIdentitiesReport();
    void addIdentity(Identity identity, String creatorUsername);
    void addIdentitiesFromFile(DataFile file, String creatorUsername);
    void modifyIdentity(Identity newData);
    void deleteIdentity(String externalId);
    void setIsGdprSuspended(String id, Identity isGdprSuspended);
}
