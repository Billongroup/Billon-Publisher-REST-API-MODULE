package brum.persistence.mapper;

import brum.common.utils.CalendarUtils;
import brum.model.dto.users.User;
import brum.model.dto.users.UserPrincipal;
import brum.persistence.entity.UserEntity;

public enum UserMapper implements AbstractPersistenceMapper<UserEntity, User> {
    INSTANCE;

    @Override
    public User mapFromEntity(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User dto = new User();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setCreatedBy(mapBasicInfo(entity.getCreatedBy()));
        dto.setCreatedAt(CalendarUtils.dateToLocalDateTime(entity.getCreatedAt()));
        dto.setPasswordUpdatedAt(CalendarUtils.dateToLocalDateTime(entity.getPasswordUpdatedAt()));
        dto.setLastNotificationAt(CalendarUtils.dateToLocalDateTime(entity.getLastNotificationAt()));
        dto.setStatus(entity.getStatus());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole().getName());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setDepartment(entity.getDepartment());
        dto.setIsActive(entity.getIsActive());
        dto.setSmsCode(entity.getSmsCode());
        dto.setSmsCodeGenerationTime(CalendarUtils.dateToLocalDateTime(entity.getSmsCodeGenerationTime()));
        dto.setIsRobot(entity.getIsRobot());
        dto.setPasswordHistory(PasswordHistoryMapper.INSTANCE.mapToListFromEntities(entity.getPasswordHistory()));
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setBlockedSince(CalendarUtils.dateToLocalDateTime(entity.getBlockedSince()));
        return dto;
    }

    @Override
    public UserEntity mapFromDto(User dto) {
        if (dto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setCreatedAt(CalendarUtils.localDateTimeToDate(dto.getCreatedAt()));
        entity.setPasswordUpdatedAt(CalendarUtils.localDateTimeToDate(dto.getPasswordUpdatedAt()));
        entity.setLastNotificationAt(CalendarUtils.localDateTimeToDate(dto.getLastNotificationAt()));
        entity.setStatus(dto.getStatus());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDepartment(dto.getDepartment());
        entity.setIsActive(dto.getIsActive());
        entity.setSmsCode(dto.getSmsCode());
        entity.setSmsCodeGenerationTime(CalendarUtils.localDateTimeToDate(dto.getSmsCodeGenerationTime()));
        entity.setIsRobot(dto.getIsRobot());
        entity.setFailedLoginAttempts(dto.getFailedLoginAttempts());
        entity.setBlockedSince(CalendarUtils.localDateTimeToDate(dto.getBlockedSince()));
        return entity;
    }

    public UserPrincipal mapPrincipalInfo(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserPrincipal dto = new UserPrincipal();
        dto.setStatus(entity.getStatus());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(RoleMapper.INSTANCE.mapFromEntity(entity.getRole()));
        dto.setIsActive(entity.getIsActive());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setBlockedSince(CalendarUtils.dateToLocalDateTime(entity.getBlockedSince()));
        dto.setDepartment(entity.getDepartment());
        return dto;
    }

    public User mapBasicInfo(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User dto = new User();
        dto.setExternalId(entity.getExternalId());
        dto.setRole(entity.getRole().getName());
        dto.setUsername(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        return dto;
    }
}
