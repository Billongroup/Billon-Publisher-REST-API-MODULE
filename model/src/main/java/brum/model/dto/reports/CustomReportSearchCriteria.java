package brum.model.dto.reports;

import brum.model.dto.common.DateRange;
import brum.model.dto.common.PaginationFilter;
import lombok.Data;

@Data
public class CustomReportSearchCriteria {
    private PaginationFilter pagination;
    private DateRange dateRange;
    private ReportType reportType;
}
