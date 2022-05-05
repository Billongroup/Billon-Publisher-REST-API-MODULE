package brum.persistence.repository;

import brum.common.enums.security.RoleEnum;
import brum.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity getRoleEntityByName(RoleEnum name);
}
