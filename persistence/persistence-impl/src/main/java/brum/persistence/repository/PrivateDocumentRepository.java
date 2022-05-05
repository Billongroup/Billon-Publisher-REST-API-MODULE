package brum.persistence.repository;

import brum.persistence.entity.PrivateDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PrivateDocumentRepository extends
        JpaRepository<PrivateDocumentEntity, Long>,
        PagingAndSortingRepository<PrivateDocumentEntity, Long>,
        JpaSpecificationExecutor<PrivateDocumentEntity> {

    PrivateDocumentEntity getByJobIdOrBlockchainAddress(String jobId, String blockchainAddress);

    @Query("SELECT d.forgettingJobId, d.jobId FROM PrivateDocumentEntity d WHERE d.forgettingStatus = brum.model.dto.documents.ForgettingStatus.FORGETTING_IN_PROGRESS")
    List<String[]> getForgettingDocumentJobIdList();

    @Query("SELECT d FROM PrivateDocumentEntity d WHERE d.identity.id = :identityId")
    List<PrivateDocumentEntity> getAllByIdentityId(Long identityId);
}
