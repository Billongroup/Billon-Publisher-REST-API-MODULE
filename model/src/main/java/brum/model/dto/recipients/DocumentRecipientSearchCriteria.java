package brum.model.dto.recipients;

import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.Sort;
import lombok.Data;

import java.util.List;

@Data
public class DocumentRecipientSearchCriteria {
    private PaginationFilter pagination;
    private Sort sort;
    private DocumentRecipientFilters filters;
}
