package brum.domain.identities;

import brum.model.dto.common.DataFile;
import brum.model.dto.identities.Identity;

public interface AddIdentityUC {
    void addIdentity(Identity identity, String creatorUsername);
    void addIdentitiesFromFile(DataFile file, String creatorUsername);
}
