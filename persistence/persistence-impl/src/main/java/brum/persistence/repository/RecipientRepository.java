package brum.persistence.repository;

import brum.persistence.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipientRepository extends JpaRepository<RecipientEntity, Long> {
    @Query("SELECT r.externalId FROM RecipientEntity r WHERE r.externalId IN :extIdSet")
    List<String> getExtIdsByExtIdSet(Set<String> extIdSet);
    List<RecipientEntity> findAllByExternalIdIn(Set<String> externalId);
}
