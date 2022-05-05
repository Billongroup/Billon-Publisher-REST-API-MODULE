package brum.model.dto.common;

import brum.common.views.PaginatedView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponse<T> {
    @JsonView({PaginatedView.PaginatedCategories.class, PaginatedView.PaginatedUsers.class,
            PaginatedView.PaginatedDocuments.class, PaginatedView.PaginatedIdentities.class})
    private List<T> rows;
    @JsonView({PaginatedView.PaginatedCategories.class, PaginatedView.PaginatedUsers.class,
            PaginatedView.PaginatedDocuments.class, PaginatedView.PaginatedIdentities.class})
    private long count;
}
