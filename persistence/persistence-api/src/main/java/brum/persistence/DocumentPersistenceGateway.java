package brum.persistence;

import brum.model.dto.documents.Document;
import brum.model.dto.documents.ForgettingStatus;
import brum.model.dto.documents.PublicationStatus;

import java.util.List;

public interface DocumentPersistenceGateway {
    void save(Document document);
    Document getDocument(String id);
    List<Document> getDocumentsByIdentityId(Long identityId);
    void setPublicationStatus(String jobId, String blockchainAddress, PublicationStatus publicationStatus);
    void setForgettingStatus(String jobId, String forgettingJobId, ForgettingStatus forgettingStatus);
    List<String> getPublishingDocumentsJobIdList();
    List<String[]> getForgettingDocumentsJobIdList();
    void deleteByJobId(String jobId);
}
