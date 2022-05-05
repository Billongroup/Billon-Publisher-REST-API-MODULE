package brum.service;

import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;

import java.util.List;

public interface ReportService {
    List<YearlyReportEntry> getYearlyReport();
    List<FairUsageReportEntry> getFairUsageReport();
    PaginatedResponse<FullInfoReportEntry> getCustomReport(CustomReportSearchCriteria searchCriteria);
    DataFile downloadStatisticsReport();

}
