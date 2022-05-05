package brum.domain.documents;

import brum.model.dto.common.DataFile;
import brum.model.dto.documents.Document;
import brum.model.dto.tree.DocumentTree;

public interface GetDocumentUC {
    Document getDocument(String id);
    DataFile downloadDocumentFile(String id);
    DataFile downloadNotificationReceiversFile(String id);
    DocumentTree getDocumentTree(String id);
}
