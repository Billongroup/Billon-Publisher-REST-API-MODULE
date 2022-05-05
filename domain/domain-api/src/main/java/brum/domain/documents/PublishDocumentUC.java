package brum.domain.documents;

import brum.model.dto.documents.DocumentType;

public interface PublishDocumentUC {
    void publishDocument(byte[] document, String jobId, String publisherUsername, DocumentType documentType);
}
