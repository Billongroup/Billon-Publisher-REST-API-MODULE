package brum.persistence.repository;

import brum.persistence.entity.PublicDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicDocumentRepository extends
        JpaRepository<PublicDocumentEntity, Long>,
        PagingAndSortingRepository<PublicDocumentEntity, Long>,
        JpaSpecificationExecutor<PublicDocumentEntity> {

    PublicDocumentEntity getByJobIdOrBlockchainAddress(String jobId, String blockchainAddress);
}
