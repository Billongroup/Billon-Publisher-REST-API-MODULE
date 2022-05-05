package brum.domain.identities;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentitySearchCriteria;

public interface GetIdentityUC {
    Identity getIdentity(String id);
    PaginatedResponse<Identity> getFilteredIdentities(IdentitySearchCriteria searchCriteria);
    DataFile generateIdentitiesReport();
}
