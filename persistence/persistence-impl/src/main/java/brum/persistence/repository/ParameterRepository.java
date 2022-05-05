package brum.persistence.repository;

import brum.model.dto.common.ParameterKey;
import brum.persistence.entity.ParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity, Long> {
    ParameterEntity getParameterEntityByKey(ParameterKey key);

    @Query("SELECT p FROM ParameterEntity p WHERE p.hidden = FALSE")
    List<ParameterEntity> getNonHiddenParameterEntities();
}
