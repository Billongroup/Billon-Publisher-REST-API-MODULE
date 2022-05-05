package brum.domain.reports;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;

import java.util.List;

public interface GetReportUC {
    List<YearlyReportEntry> getYearlyReport();
    List<FairUsageReportEntry> getFairUsageReport();
    PaginatedResponse<FullInfoReportEntry> getCustomReport(CustomReportSearchCriteria searchCriteria);
    DataFile downloadStatisticsReport();
}
