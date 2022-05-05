package brum.persistence.repository;

import brum.persistence.entity.DocumentContactDetailsEntity;
import brum.persistence.entity.DocumentContactDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DocumentRecipientRepository extends
        JpaRepository<DocumentContactDetailsEntity, DocumentContactDetailsId>,
        PagingAndSortingRepository<DocumentContactDetailsEntity, DocumentContactDetailsId>,
        JpaSpecificationExecutor<DocumentContactDetailsEntity> {

    @Modifying
    @Query("UPDATE DocumentContactDetailsEntity d SET d.status = brum.model.dto.recipients.DocumentNotificationStatus.UPDATED" +
            " WHERE d.key.contactDetailsId IN :contactDetailsId" +
            " AND d.status = brum.model.dto.recipients.DocumentNotificationStatus.NOTIFICATION_SENT")
    void setUpdatedStatus(Set<Long> contactDetailsId);

    @Modifying
    @Query("UPDATE DocumentContactDetailsEntity d SET d.status = brum.model.dto.recipients.DocumentNotificationStatus.NOTIFICATION_SENT" +
            " WHERE d.key.documentId = (SELECT doc.id FROM PublicDocumentEntity doc WHERE doc.blockchainAddress = :blockchainAddress) AND" +
            " d.key.contactDetailsId IN :idSet")
    void setSentStatus(Set<Long> idSet, String blockchainAddress);
}
