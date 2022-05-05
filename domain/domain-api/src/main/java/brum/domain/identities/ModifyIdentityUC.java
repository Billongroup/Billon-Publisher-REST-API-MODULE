package brum.domain.identities;

import brum.model.dto.identities.Identity;

public interface ModifyIdentityUC {
    void modifyIdentity(Identity newData);
    void setIsGdprSuspended(String id, Identity isGdprSuspended);
}
