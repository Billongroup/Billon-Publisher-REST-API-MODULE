package brum.persistence.mapper;

import brum.model.dto.users.User;
import brum.model.dto.users.UserHistory;
import brum.persistence.entity.UserHistoryEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

public enum UserHistoryMapper implements AbstractPersistenceMapper<UserHistoryEntity, UserHistory> {
    INSTANCE;


    @Override
    public UserHistory mapFromEntity(UserHistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        UserHistory dto = new UserHistory();
        dto.setUpdatedBy(UserMapper.INSTANCE.mapBasicInfo(entity.getUpdatedBy()));
        dto.setUpdatedAt(LocalDateTime.ofInstant(entity.getUpdatedAt().toInstant(), ZoneId.systemDefault()));
        dto.setStatus(entity.getStatus());
        dto.setRole(entity.getRole().getName());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setDepartment(entity.getDepartment());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    @Override
    public UserHistoryEntity mapFromDto(UserHistory dto) {
        return null;
    }


    public UserHistoryEntity mapFromDto(User dto) {
        if (dto == null) {
            return null;
        }
        UserHistoryEntity entity = new UserHistoryEntity();
        entity.setStatus(dto.getStatus());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setDepartment(dto.getDepartment());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
