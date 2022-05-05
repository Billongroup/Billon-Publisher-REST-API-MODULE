package brum.service.impl;

import brum.domain.identities.*;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentitySearchCriteria;
import brum.service.IdentityService;
import org.springframework.stereotype.Service;

@Service
public class IdentityServiceImpl implements IdentityService {

    private final AddIdentityUC addIdentityUC;
    private final ModifyIdentityUC modifyIdentityUC;
    private final GetIdentityUC getIdentityUC;
    private final DeleteIdentityUC deleteIdentityUC;

    public IdentityServiceImpl(AddIdentityUC addIdentityUC, ModifyIdentityUC modifyIdentityUC,
                               GetIdentityUC getIdentityUC, DeleteIdentityUC deleteIdentityUC) {
        this.addIdentityUC = addIdentityUC;
        this.modifyIdentityUC = modifyIdentityUC;
        this.getIdentityUC = getIdentityUC;
        this.deleteIdentityUC = deleteIdentityUC;
    }

    @Override
    public Identity getIdentity(String id) {
        return getIdentityUC.getIdentity(id);
    }

    @Override
    public PaginatedResponse<Identity> getIdentities(IdentitySearchCriteria searchCriteria) {
        return getIdentityUC.getFilteredIdentities(searchCriteria);
    }

    @Override
    public DataFile generateIdentitiesReport() {
        return getIdentityUC.generateIdentitiesReport();
    }

    @Override
    public void addIdentity(Identity identity, String creatorUsername) {
        addIdentityUC.addIdentity(identity, creatorUsername);
    }

    @Override
    public void addIdentitiesFromFile(DataFile file, String creatorUsername) {
        addIdentityUC.addIdentitiesFromFile(file, creatorUsername);
    }

    @Override
    public void modifyIdentity(Identity newData) {
        modifyIdentityUC.modifyIdentity(newData);
    }

    @Override
    public void deleteIdentity(String externalId) {
        deleteIdentityUC.deleteIdentity(externalId);
    }

    @Override
    public void setIsGdprSuspended(String id, Identity isGdprSuspended) {
        modifyIdentityUC.setIsGdprSuspended(id, isGdprSuspended);
    }
}
