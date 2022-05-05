package brum.model.dto.documents;

import brum.model.dto.common.SortOrder;
import lombok.Data;

@Data
public class SortDocuments {
    private SortOrder order;
    private SortDocumentsBy sortBy;
}
