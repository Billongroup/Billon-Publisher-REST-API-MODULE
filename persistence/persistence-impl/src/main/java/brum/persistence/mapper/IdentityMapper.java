package brum.persistence.mapper;

import brum.model.dto.identities.Identity;
import brum.persistence.entity.IdentityEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public enum IdentityMapper implements AbstractPersistenceMapper<IdentityEntity, Identity> {
    INSTANCE;

    @Override
    public Identity mapFromEntity(IdentityEntity entity) {
        if (entity == null) {
            return null;
        }
        Identity dto = new Identity();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setCreatedBy(UserMapper.INSTANCE.mapBasicInfo(entity.getCreatedBy()));
        dto.setStatus(entity.getStatus());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAdditionalInformation(entity.getAdditionalInformation());
        dto.setCreatedAt(LocalDateTime.ofInstant(entity.getCreatedAt().toInstant(), ZoneId.systemDefault()));
        dto.setIsActive(entity.getIsActive());
        dto.setIsGdprSuspended(entity.getIsGdprSuspended());
        return dto;
    }

    @Override
    public IdentityEntity mapFromDto(Identity dto) {
        if (dto == null) {
            return null;
        }
        IdentityEntity entity = new IdentityEntity();
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setStatus(dto.getStatus());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDocumentNumber(dto.getDocumentNumber());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAdditionalInformation(dto.getAdditionalInformation());
        entity.setCreatedAt(Date.from(dto.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        entity.setIsActive(dto.getIsActive());
        entity.setIsGdprSuspended(dto.getIsGdprSuspended());
        return entity;
    }

}
