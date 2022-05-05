package brum.domain.documents;

import brum.model.dto.common.DataFile;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentType;

public interface PrepareDocumentUC {
    Document prepareDocument(DataFile document, DataFile contactDetails, Document documentInfo, String publisherUsername, DocumentType documentType);
}
