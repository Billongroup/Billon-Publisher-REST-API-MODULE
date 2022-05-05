package brum.persistence.mapper;

import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentType;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.entity.DocumentBaseEntity;
import brum.persistence.entity.PrivateDocumentEntity;
import brum.persistence.entity.PublicDocumentEntity;

import java.time.LocalDateTime;

public enum DocumentMapper implements AbstractPersistenceMapper<DocumentBaseEntity, Document> {
    INSTANCE;


    @Override
    public Document mapFromEntity(DocumentBaseEntity entity) {
        if (entity == null) {
            return null;
        }
        Document result = mapDocumentFromEntity(entity);
        result.setId(entity.getId());
        result.setJobId(entity.getJobId());
        result.setDocumentBlockchainAddress(entity.getBlockchainAddress());
        result.setDocumentPublicationStatus(entity.getPublicationStatus());
        return result;
    }

    @Override
    public DocumentBaseEntity mapFromDto(Document dto) {
        DocumentBaseEntity result = mapDocumentFromDto(dto);
        result.setId(dto.getId());
        result.setJobId(dto.getJobId());
        result.setBlockchainAddress(dto.getDocumentBlockchainAddress());
        result.setPublicationStatus(dto.getDocumentPublicationStatus());
        return result;
    }

    private Document mapDocumentFromEntity(DocumentBaseEntity entity) {
        if (entity instanceof PublicDocumentEntity) {
            return mapPublicDocumentFromEntity();
        } else if (entity instanceof PrivateDocumentEntity) {
            return mapPrivateDocumentFromEntity((PrivateDocumentEntity) entity);
        }
        throw new BRUMGeneralException(ErrorStatusCode.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    private Document mapPublicDocumentFromEntity() {
        Document dto = new Document();
        dto.setDocumentType(DocumentType.PUBLIC);
        return dto;
    }

    private Document mapPrivateDocumentFromEntity(PrivateDocumentEntity entity) {
        Document dto = new Document();
        dto.setDocumentType(DocumentType.PRIVATE);
        dto.setIdentity(IdentityMapper.INSTANCE.mapFromEntity(entity.getIdentity()));
        return dto;
    }

    private DocumentBaseEntity mapDocumentFromDto(Document dto) {
        if (dto.getDocumentType().equals(DocumentType.PUBLIC)) {
            return mapPublicDocumentFromDto();
        } else {
            return mapPrivateDocumentFromDto();
        }
    }

    private PublicDocumentEntity mapPublicDocumentFromDto() {
        return new PublicDocumentEntity();
    }

    private PrivateDocumentEntity mapPrivateDocumentFromDto() {
        return new PrivateDocumentEntity();
    }
}
