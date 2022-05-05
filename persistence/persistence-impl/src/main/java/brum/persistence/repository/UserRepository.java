package brum.persistence.repository;

import brum.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends
        JpaRepository<UserEntity, Long>,
        PagingAndSortingRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {
    UserEntity getUserEntityByExternalId(String externalId);
    UserEntity getUserEntityByUsername(String username);
    UserEntity getUserEntityByEmail(String email);

    @Query("SELECT u.externalId FROM UserEntity u WHERE u.passwordUpdatedAt <= :expiryLimit AND u.status = brum.model.dto.users.UserStatus.REGISTERED")
    List<String> getUserIdsWithExpiredPassword(Date expiryLimit);
}
