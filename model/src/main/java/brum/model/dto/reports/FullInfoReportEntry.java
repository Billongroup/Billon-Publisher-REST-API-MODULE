package brum.model.dto.reports;

import lombok.Data;

@Data
public class FullInfoReportEntry {
    private String startDate;
    private String endDate;
    private String reportName;
    private Integer publicDocumentsPublications;
    private Integer privateDocumentsPublications;
    private Integer allDocumentsPublications;
    private Double publicDocumentsSize;
    private Double privateDocumentsSize;
    private Double allDocumentsSize;
    private Double documentSystemSize;
    private Double averagePublicationTimePublic;
    private Double averagePublicationTimePrivate;
}
