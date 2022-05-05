package brum.domain.impl.reports;

import brum.domain.documents.GetDocumentListUC;
import brum.domain.file.writers.WriteStatisticsExcelFile;
import brum.domain.reports.GetReportUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.DataFile;
import brum.model.dto.common.DataFileType;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.reports.*;
import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import brum.model.exception.validation.ValidationException;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GetReportUCImpl implements GetReportUC {

    private final DocumentProxy documentProxy;

    public GetReportUCImpl(DocumentProxy documentProxy) {
        this.documentProxy = documentProxy;
    }

    @Override
    public List<YearlyReportEntry> getYearlyReport() {
        BemResponse<List<YearlyReportEntry>> response = documentProxy.getYearlyReport();
        if (!response.isSuccess()) {
            throw response.getException();
        }
        return response.getResponse();
    }

    @Override
    public List<FairUsageReportEntry> getFairUsageReport() {
        BemResponse<List<FairUsageReportEntry>> response = documentProxy.getFairUsageReport();
        if (!response.isSuccess()) {
            throw response.getException();
        }
        return response.getResponse();
    }

    @Override
    public PaginatedResponse<FullInfoReportEntry> getCustomReport(CustomReportSearchCriteria searchCriteria) {
        if (searchCriteria == null || searchCriteria.getReportType() == null) {
            throw new ValidationException(InvalidField.REPORT_TYPE, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        BemResponse<PaginatedResponse<FullInfoReportEntry>> response = documentProxy.getCustomReport(searchCriteria);
        if (!response.isSuccess()) {
            throw response.getException();
        }
        PaginatedResponse<FullInfoReportEntry> report = response.getResponse();
        setEntryName(report.getRows());
        return report;
    }

    @Override
    public DataFile downloadStatisticsReport() {
        List<YearlyReportEntry> yearlyReport = getYearlyReport();
        List<FairUsageReportEntry> fairUsageReport = getFairUsageReport();
        CustomReportSearchCriteria searchCriteria = new CustomReportSearchCriteria();
        searchCriteria.setReportType(ReportType.DAILY);
        List<FullInfoReportEntry> customReport = getCustomReport(searchCriteria).getRows();
        setEntryName(customReport);
        DataFile reportFile = new DataFile();
        reportFile.setFile(new WriteStatisticsExcelFile().generateStatisticsReport(yearlyReport, fairUsageReport, customReport));
        reportFile.setFileName("report-statistics.xlsx");
        reportFile.setFileType(DataFileType.EXCEL);
        return reportFile;
    }

    private void setEntryName(List<FullInfoReportEntry> report) {
        for (FullInfoReportEntry entry : report) {
            entry.setReportName(entry.getStartDate().split(" ")[0] + " - " + entry.getEndDate().split(" ")[0]);
        }
    }
}
