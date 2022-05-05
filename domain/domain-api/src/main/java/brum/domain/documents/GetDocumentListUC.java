package brum.domain.documents;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.DocumentFilters;
import brum.model.dto.documents.DocumentSearchCriteria;

public interface GetDocumentListUC {
    PaginatedResponse<Document> getDocumentList(DocumentSearchCriteria searchCriteria);
    DataFile downloadDocumentsReport(DocumentSearchCriteria searchCriteria);
}
