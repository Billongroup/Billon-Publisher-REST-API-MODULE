package brum.persistence.repository;

import brum.persistence.entity.DocumentBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends
        JpaRepository<DocumentBaseEntity, Long>,
        PagingAndSortingRepository<DocumentBaseEntity, Long>,
        JpaSpecificationExecutor<DocumentBaseEntity> {

    DocumentBaseEntity getByJobId(String jobId);
    DocumentBaseEntity getByJobIdOrBlockchainAddress(String jobId, String blockchainAddress);

    @Query("SELECT d.jobId FROM DocumentBaseEntity d WHERE d.publicationStatus = brum.model.dto.documents.PublicationStatus.PUBLISHING_INITIATED")
    List<String> getPublishingDocumentJobIdList();
}
