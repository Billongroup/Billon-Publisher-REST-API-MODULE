package brum.model.dto.reports;

import lombok.Data;

@Data
public class YearlyReportEntry {
    private String name;
    private Integer publicDocumentPublished;
    private Integer privateDocumentPublished;
}
