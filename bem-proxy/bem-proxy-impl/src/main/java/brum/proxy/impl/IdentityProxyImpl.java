package brum.proxy.impl;

import brum.model.dto.common.BemResponse;
import brum.model.dto.identities.Identity;
import brum.model.exception.ErrorStatusCode;
import brum.proxy.IdentityProxy;
import brum.proxy.mapper.IdentityProxyMapper;
import com.zunit.dm.publisher.services.identity.dto.IdentityResponseDto;
import com.zunit.dm.publisher.services.identity.facade.IdentityFacade;
import https.types_dm_billongroup.InternalSystemStatusErrors;
import org.springframework.stereotype.Service;

@Service
public class IdentityProxyImpl implements IdentityProxy {

    private final IdentityFacade identityFacade;

    public IdentityProxyImpl(IdentityFacade identityFacade) {
        this.identityFacade = identityFacade;
    }

    @Override
    public BemResponse<Object> updateIdentity(Identity oldIdentity, Identity newIdentity) {
        IdentityResponseDto response = identityFacade.updateIdentity(IdentityProxyMapper.mapIdentity(oldIdentity), IdentityProxyMapper.mapIdentity(newIdentity));
        if (response == null) {
            return BemResponse.builder().status(ErrorStatusCode.INTERNAL_SERVER_ERROR).build();
        }
        if (!response.getErrorMessage().equals(InternalSystemStatusErrors.SUCCESS)) {
            return BemResponse.bemError(response.getErrorMessage().name());
        }
        return BemResponse.success();
    }
}
