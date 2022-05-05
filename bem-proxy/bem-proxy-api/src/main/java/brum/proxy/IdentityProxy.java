package brum.proxy;

import brum.model.dto.common.BemResponse;
import brum.model.dto.identities.Identity;

public interface IdentityProxy {
    BemResponse<Object> updateIdentity(Identity oldIdentity, Identity newIdentity);
}
