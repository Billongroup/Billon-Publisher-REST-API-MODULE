package brum.persistence.mapper;

import brum.model.dto.users.Privilege;
import brum.persistence.entity.PrivilegeEntity;

public enum PrivilegeMapper implements AbstractPersistenceMapper<PrivilegeEntity, Privilege> {
    INSTANCE;

    @Override
    public Privilege mapFromEntity(PrivilegeEntity entity) {
        if (entity == null) {
            return null;
        }
        Privilege dto = new Privilege();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        return dto;
    }

    @Override
    public PrivilegeEntity mapFromDto(Privilege dto) {
        if (dto == null) {
            return null;
        }
        PrivilegeEntity entity = new PrivilegeEntity();
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        return entity;
    }
}
