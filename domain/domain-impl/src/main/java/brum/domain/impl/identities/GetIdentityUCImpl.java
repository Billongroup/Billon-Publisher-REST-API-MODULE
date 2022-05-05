package brum.domain.impl.identities;

import brum.domain.file.writers.WriteIdentitiesExcelFile;
import brum.domain.identities.GetIdentityUC;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.DataFileType;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.identities.Identity;
import brum.model.dto.identities.IdentitySearchCriteria;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.IdentityPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GetIdentityUCImpl implements GetIdentityUC {

    private final IdentityPersistenceGateway identityPersistenceGateway;

    public GetIdentityUCImpl(IdentityPersistenceGateway identityPersistenceGateway) {
        this.identityPersistenceGateway = identityPersistenceGateway;
    }

    @Override
    public Identity getIdentity(String id) {
        Identity identity = identityPersistenceGateway.getIdentityByExternalId(id);
        if (identity == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        return identity;
    }

    @Override
    public PaginatedResponse<Identity> getFilteredIdentities(IdentitySearchCriteria searchCriteria) {
        return identityPersistenceGateway.getFilteredIdentities(searchCriteria);
    }

    @Override
    public DataFile generateIdentitiesReport() {
        List<Identity> identities = identityPersistenceGateway.getAllIdentities();
        DataFile identitiesFile = new DataFile();
        identitiesFile.setFile(new WriteIdentitiesExcelFile().generateIdentitiesReport(identities));
        identitiesFile.setFileName("identityList.xlsx");
        identitiesFile.setFileType(DataFileType.EXCEL);
        return identitiesFile;
    }
}
