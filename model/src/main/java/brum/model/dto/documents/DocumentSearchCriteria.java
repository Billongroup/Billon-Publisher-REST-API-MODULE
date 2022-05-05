package brum.model.dto.documents;

import brum.common.views.DocumentViews;
import brum.model.dto.common.PaginationFilter;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class DocumentSearchCriteria {
    private PaginationFilter pagination;
    @JsonView(DocumentViews.DocumentsReport.class)
    private SortDocuments sort;
    @JsonView(DocumentViews.DocumentsReport.class)
    private DocumentFilters filters;
}
