package brum.web.controller;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.reports.CustomReportSearchCriteria;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.YearlyReportEntry;
import brum.service.ReportService;
import brum.web.common.ControllerMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static brum.common.enums.security.EndpointConstants.ReportControllerConstants.*;

@RestController
@RequestMapping("/v1.0")
@Api(tags = TAG)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(YEARLY_REPORT_URI)
    @ApiOperation(GET_YEARLY_REPORT_DESCRIPTION)
    public List<YearlyReportEntry> getYearlyReport() {
        return reportService.getYearlyReport();
    }

    @GetMapping(FAIR_USAGE_REPORT_URI)
    @ApiOperation(GET_FAIR_USAGE_REPORT_DESCRIPTION)
    public List<FairUsageReportEntry> getFairUsageReport() {
        return reportService.getFairUsageReport();
    }

    @GetMapping(CUSTOM_REPORT_URI)
    @ApiOperation(GET_CUSTOM_REPORT_DESCRIPTION)
    public PaginatedResponse<FullInfoReportEntry> getCustomReport(CustomReportSearchCriteria searchCriteria) {
        return reportService.getCustomReport(searchCriteria);
    }

    @GetMapping(DOWNLOAD_STATISTICS_REPORT_URI)
    @ApiOperation(GET_DOWNLOAD_STATISTICS_REPORT_DESCRIPTION)
    public ResponseEntity<byte[]> downloadStatisticsReport() {
        return ControllerMapper.mapToDownloadFile(reportService.downloadStatisticsReport());
    }
}