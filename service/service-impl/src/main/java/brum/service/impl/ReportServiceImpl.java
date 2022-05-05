package brum.service.impl;

import brum.domain.reports.GetReportUC;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;
import brum.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final GetReportUC getReportUC;

    public ReportServiceImpl(GetReportUC getReportUC) {
        this.getReportUC = getReportUC;
    }

    @Override
    public List<YearlyReportEntry> getYearlyReport() {
        return getReportUC.getYearlyReport();
    }

    @Override
    public List<FairUsageReportEntry> getFairUsageReport() {
        return getReportUC.getFairUsageReport();
    }

    @Override
    public PaginatedResponse<FullInfoReportEntry> getCustomReport(CustomReportSearchCriteria searchCriteria) {
        return getReportUC.getCustomReport(searchCriteria);
    }

    @Override
    public DataFile downloadStatisticsReport() {
        return getReportUC.downloadStatisticsReport();
    }
}
