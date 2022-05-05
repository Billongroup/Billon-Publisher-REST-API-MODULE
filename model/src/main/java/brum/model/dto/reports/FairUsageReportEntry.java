package brum.model.dto.reports;

import lombok.Data;

@Data
public class FairUsageReportEntry {
    private String name;
    private Float memoryUsagePercent;
    private Long availableMemory;
}
