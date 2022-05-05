package brum.persistence.mapper;

import brum.model.dto.users.Role;
import brum.persistence.entity.RoleEntity;

public enum RoleMapper implements AbstractPersistenceMapper<RoleEntity, Role> {
    INSTANCE;

    @Override
    public Role mapFromEntity(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        Role dto = new Role();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setName(entity.getName());
        dto.setRolePrivilege(PrivilegeMapper.INSTANCE.mapToSetFromEntities(entity.getRolePrivilege()));
        return dto;
    }

    @Override
    public RoleEntity mapFromDto(Role dto) {
        if (dto == null) {
            return null;
        }
        RoleEntity entity = new RoleEntity();
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
        entity.setRolePrivilege(PrivilegeMapper.INSTANCE.mapToSetFromDtos(dto.getRolePrivilege()));
        return entity;
    }
}
