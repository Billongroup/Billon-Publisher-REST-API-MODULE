package brum.model.dto.categories;

import brum.common.views.CategoryViews;
import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.SortOrder;
import brum.model.dto.documents.DocumentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
public class CategoryFilters {
    private boolean accountingNumberOfDocuments;
    private List<DocumentStatus> documentStatusList;
}
