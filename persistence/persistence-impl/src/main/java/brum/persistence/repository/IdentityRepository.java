package brum.persistence.repository;

import brum.persistence.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IdentityRepository extends
        JpaRepository<IdentityEntity, Long>,
        PagingAndSortingRepository<IdentityEntity, Long>,
        JpaSpecificationExecutor<IdentityEntity> {

    IdentityEntity getIdentityEntityByExternalId(String externalId);
    IdentityEntity getIdentityEntityByDocumentNumber(String documentNumber);
}
