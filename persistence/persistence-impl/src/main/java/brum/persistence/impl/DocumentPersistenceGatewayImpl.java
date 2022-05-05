package brum.persistence.impl;

import brum.model.dto.documents.Document;
import brum.model.dto.documents.ForgettingStatus;
import brum.model.dto.documents.PublicationStatus;
import brum.persistence.DocumentPersistenceGateway;
import brum.persistence.entity.DocumentBaseEntity;
import brum.persistence.entity.PrivateDocumentEntity;
import brum.persistence.mapper.DocumentMapper;
import brum.persistence.repository.DocumentRepository;
import brum.persistence.repository.PrivateDocumentRepository;
import brum.persistence.repository.IdentityRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DocumentPersistenceGatewayImpl implements DocumentPersistenceGateway {

    private final DocumentRepository documentRepository;
    private final PrivateDocumentRepository privateDocumentRepository;
    private final IdentityRepository identityRepository;

    public DocumentPersistenceGatewayImpl(DocumentRepository documentRepository,
                                          PrivateDocumentRepository privateDocumentRepository,
                                          IdentityRepository identityRepository) {
        this.documentRepository = documentRepository;
        this.privateDocumentRepository = privateDocumentRepository;
        this.identityRepository = identityRepository;
    }

    @Override
    public void save(Document document) {
        DocumentBaseEntity entity = DocumentMapper.INSTANCE.mapFromDto(document);
        if (entity instanceof PrivateDocumentEntity) {
            ((PrivateDocumentEntity) entity).setIdentity(
                    identityRepository.getIdentityEntityByExternalId(document.getIdentity().getExternalId()));
        }
        documentRepository.save(entity);
    }

    @Override
    public Document getDocument(String id) {
        DocumentBaseEntity entity = documentRepository.getByJobIdOrBlockchainAddress(id, id);
        return DocumentMapper.INSTANCE.mapFromEntity(entity);
    }

    @Override
    public List<Document> getDocumentsByIdentityId(Long identityId) {
        List<PrivateDocumentEntity> documentList = privateDocumentRepository.getAllByIdentityId(identityId);
        return DocumentMapper.INSTANCE.mapToListFromEntities(documentList);
    }

    @Override
    public void setPublicationStatus(String jobId, String blockchainAddress, PublicationStatus publicationStatus) {
        if (publicationStatus == null) {
            return;
        }
        DocumentBaseEntity document = documentRepository.getByJobId(jobId);
        document.setPublicationStatus(publicationStatus);
        document.setBlockchainAddress(blockchainAddress);
        documentRepository.save(document);
    }

    @Override
    public void setForgettingStatus(String id, String forgettingJobId, ForgettingStatus forgettingStatus) {
        PrivateDocumentEntity document = privateDocumentRepository.getByJobIdOrBlockchainAddress(id, id);
        if (forgettingStatus != null) {
            document.setForgettingStatus(forgettingStatus);
        }
        if (forgettingJobId != null) {
            document.setForgettingJobId(forgettingJobId);
        }
        privateDocumentRepository.save(document);
    }

    @Override
    public List<String> getPublishingDocumentsJobIdList() {
        return documentRepository.getPublishingDocumentJobIdList();
    }

    @Override
    public List<String[]> getForgettingDocumentsJobIdList() {
        return privateDocumentRepository.getForgettingDocumentJobIdList();
    }

    @Override
    public void deleteByJobId(String jobId) {
        DocumentBaseEntity entity = documentRepository.getByJobId(jobId);
        documentRepository.delete(entity);
    }
}
