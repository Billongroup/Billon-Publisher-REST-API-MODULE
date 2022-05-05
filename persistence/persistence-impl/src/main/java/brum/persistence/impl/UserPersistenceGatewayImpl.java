package brum.persistence.impl;

import brum.common.utils.CalendarUtils;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.users.*;
import brum.persistence.UserPersistenceGateway;
import brum.persistence.entity.PasswordHistoryEntity;
import brum.persistence.entity.UserEntity;
import brum.persistence.entity.UserHistoryEntity;
import brum.persistence.filters.mapper.PaginationMapper;
import brum.persistence.filters.mapper.SortUsersByMapper;
import brum.persistence.filters.mapper.UserSpecificationMapper;
import brum.persistence.filters.specification.UserSpecification;
import brum.persistence.mapper.*;
import brum.persistence.repository.PasswordHistoryRepository;
import brum.persistence.repository.RoleRepository;
import brum.persistence.repository.UserHistoryRepository;
import brum.persistence.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPersistenceGatewayImpl implements UserPersistenceGateway {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final RoleRepository roleRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public UserPersistenceGatewayImpl(UserRepository userRepository, UserHistoryRepository userHistoryRepository,
                                      RoleRepository roleRepository, PasswordHistoryRepository passwordHistoryRepository) {
        this.userRepository = userRepository;
        this.userHistoryRepository = userHistoryRepository;
        this.roleRepository = roleRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    @Override
    public PaginatedResponse<User> getFilteredUsers(UserSearchCriteria searchCriteria) {
        Specification<UserEntity> specifications = combineSpecifications(
                UserSpecificationMapper.mapToSpecification(searchCriteria == null ? null : searchCriteria.getFilters()));

        PageRequest pagination = PaginationMapper.map(searchCriteria == null ? null : searchCriteria.getPagination());
        SortUsers sort = searchCriteria == null ? null : searchCriteria.getSort();
        pagination = pagination.withSort(SortUsersByMapper.INSTANCE.map(
                sort == null ? null : sort.getSortBy(), sort == null ? null : sort.getOrder()));

        Page<UserEntity> page = userRepository.findAll(specifications, pagination);
        PaginatedResponse<User> result = new PaginatedResponse<>();
        result.setRows(UserMapper.INSTANCE.mapToListFromEntities(page.getContent()));
        result.setCount(page.getTotalElements());
        return result;
    }

    @Override
    public User getUserByExternalId(String externalId, boolean getHistory) {
        UserEntity userEntity = userRepository.getUserEntityByExternalId(externalId);
        if (userEntity == null) {
            return null;
        }
        User dto = UserMapper.INSTANCE.mapFromEntity(userEntity);
        if (getHistory) {
            List<UserHistory> history = UserHistoryMapper.INSTANCE.mapToListFromEntities(userEntity.getHistory());
            Comparator<UserHistory> updateDateDescendingComparator = Comparator.comparing(UserHistory::getUpdatedAt).reversed();
            dto.setHistory(history.stream().sorted(updateDateDescendingComparator).collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public User getUserByUsername(String username) {
        UserEntity userEntity = userRepository.getUserEntityByUsername(username);
        return UserMapper.INSTANCE.mapFromEntity(userEntity);
    }

    @Override
    public User getUserBasicInfoByUsername(String username) {
        UserEntity userEntity = userRepository.getUserEntityByUsername(username);
        return UserMapper.INSTANCE.mapBasicInfo(userEntity);
    }

    @Override
    public UserPrincipal getUserPrincipalByUsername(String username) {
        UserEntity userEntity = userRepository.getUserEntityByUsername(username);
        return UserMapper.INSTANCE.mapPrincipalInfo(userEntity);
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = userRepository.getUserEntityByEmail(email);
        return UserMapper.INSTANCE.mapFromEntity(userEntity);
    }

    @Override
    public void saveUser(User user) {
        UserEntity entity = UserMapper.INSTANCE.mapFromDto(user);
        entity.setCreatedBy(userRepository.getUserEntityByUsername(user.getCreatedBy().getUsername()));
        entity.setRole(roleRepository.getRoleEntityByName(user.getRole()));
        try {
            userRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw ConstraintExceptionMapper.map(e);
        }
    }

    @Override
    public void addHistoryEntry(User user, String updatedBy) {
        UserHistoryEntity entity = UserHistoryMapper.INSTANCE.mapFromDto(user);
        entity.setRole(roleRepository.getRoleEntityByName(user.getRole()));
        entity.setCurrentUser(userRepository.getUserEntityByUsername(user.getUsername()));
        if (updatedBy != null) {
            entity.setUpdatedBy(userRepository.getUserEntityByUsername(updatedBy));
        }
        entity.setUpdatedAt(new Date(System.currentTimeMillis()));
        userHistoryRepository.save(entity);
    }

    @Override
    public void addPasswordHistoryEntry(User user) {
        PasswordHistoryEntity entity = PasswordHistoryMapper.INSTANCE.mapFromDto(user);
        entity.setUser(userRepository.getUserEntityByUsername(user.getUsername()));
        passwordHistoryRepository.save(entity);
    }

    @Override
    public List<String> getUserIdsWithExpiredPassword(Long passwordExpirationDays) {
        LocalDateTime maxPasswordUpdateDate = LocalDateTime.now().minusDays(passwordExpirationDays);
        return userRepository.getUserIdsWithExpiredPassword(CalendarUtils.localDateTimeToDate(maxPasswordUpdateDate));
    }

    @Override
    public void setFailedLoginAttempts(String username, Integer attempts) {
        UserEntity user = userRepository.getUserEntityByUsername(username);
        if (user == null) {
            return;
        }
        if (attempts == null) {
            attempts = 0;
        }
        user.setFailedLoginAttempts(attempts);
        userRepository.save(user);
    }

    @Override
    public void setBlockedSince(String username, LocalDateTime blockedSince) {
        UserEntity user = userRepository.getUserEntityByUsername(username);
        if (user == null) {
            return;
        }
        user.setBlockedSince(CalendarUtils.localDateTimeToDate(blockedSince));
        userRepository.save(user);
    }

    private Specification<UserEntity> combineSpecifications(List<UserSpecification> specifications) {
        Specification<UserEntity> specification =
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        for (UserSpecification userSpecification : specifications) {
            specification = specification.and(userSpecification);
        }
        return specification;
    }
}
