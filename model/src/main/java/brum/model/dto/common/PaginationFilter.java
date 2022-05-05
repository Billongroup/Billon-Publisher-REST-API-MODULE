package brum.model.dto.common;

import lombok.Data;

@Data
public class PaginationFilter {
    private Integer limit;
    private Integer page;
}
