package brum.domain.file.writers;

import brum.model.dto.reports.FairUsageReportEntry;
import brum.model.dto.reports.FullInfoReportEntry;
import brum.model.dto.reports.YearlyReportEntry;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;

public class WriteStatisticsExcelFile extends ExcelFileWriter {
     private static final List<Column<YearlyReportEntry>> YEARLY_REPORT_COLUMNS = Arrays.asList(
             new Column<>("Data", YearlyReportEntry::getName),
             new Column<>("Liczba opublikowanych dokumentów publicznych", YearlyReportEntry::getPublicDocumentPublished),
             new Column<>("Liczba opublikowanych dokumentów prywatnych", YearlyReportEntry::getPrivateDocumentPublished),
             new Column<>("Liczba opublikowanych dokumentów łącznie", e -> e.getPublicDocumentPublished() + e.getPrivateDocumentPublished())
     );

     private static final List<Column<FairUsageReportEntry>> MEMORY_USAGE_COLUMNS = Arrays.asList(
             new Column<>("Miesiąc", FairUsageReportEntry::getName),
             new Column<>("% Wartość zużycia dostępnej przestrzeni dyskowej", FairUsageReportEntry::getName),
             new Column<>("Ogólna dostępna przestrzeń dyskowa [MB]", e -> e.getAvailableMemory() * 1000),
             new Column<>("Dostępna pozostała przestrzeń dyskowa [MB]", e -> e.getAvailableMemory() * (1 - e.getMemoryUsagePercent()))
     );

     private static final List<Column<FullInfoReportEntry>> HISTORY_COLUMNS = Arrays.asList(
             new Column<>("Data", FullInfoReportEntry::getReportName),
             new Column<>("Liczba opublikowanych dokumentów publicznych", FullInfoReportEntry::getPublicDocumentsPublications),
             new Column<>("Rozmiar opublikowanych dokumentów publicznych [MB]", e -> e.getPublicDocumentsSize() * 1000),
             new Column<>("Liczba opublikowanych dokumentów prywatnych", FullInfoReportEntry::getPrivateDocumentsPublications),
             new Column<>("Rozmiar opublikowanych dokumentów prywatnych [MB]", e -> e.getPrivateDocumentsSize() * 1000),
             new Column<>("Liczba opublikowanych dokumentów łącznie", FullInfoReportEntry::getAllDocumentsPublications),
             new Column<>("Rozmiar opublikowanych dokumentów łącznie [MB]", FullInfoReportEntry::getAllDocumentsSize),
             new Column<>("Rozmiar zajętej przestrzeni w systemie [MB]", FullInfoReportEntry::getDocumentSystemSize),
             new Column<>("Średni czas publikacji - Dok. publiczne [MB]", FullInfoReportEntry::getAveragePublicationTimePublic),
             new Column<>("Średni czas publikacji - Dok. prywatne [MB]", FullInfoReportEntry::getAveragePublicationTimePrivate)
     );

     public byte[] generateStatisticsReport(List<YearlyReportEntry> yearlyReport, List<FairUsageReportEntry> fairUsageReport,
                                            List<FullInfoReportEntry> customReport) {
          Workbook workbook = new XSSFWorkbook();
          Sheet yearlyReportSheet = workbook.createSheet("Raport roczny");
          writeToSheet(yearlyReportSheet, yearlyReport, YEARLY_REPORT_COLUMNS);
          Sheet memoryUsageSheet = workbook.createSheet("Zużycie przestrzeni");
          writeToSheet(memoryUsageSheet, fairUsageReport, MEMORY_USAGE_COLUMNS);
          Sheet historySheet = workbook.createSheet("Historia");
          writeToSheet(historySheet, customReport, HISTORY_COLUMNS);
          return getBytesFromWorkbook(workbook);
     }
}
