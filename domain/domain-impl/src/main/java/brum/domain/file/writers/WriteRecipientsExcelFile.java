package brum.domain.file.writers;

import brum.model.dto.recipients.Recipient;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;

public class WriteRecipientsExcelFile extends ExcelFileWriter {

    private static final List<Column<Recipient>> COLUMNS = Arrays.asList(
      new Column<>("System źródłowy", r -> r.getExternalId().split("_", 2)[0]),
      new Column<>("Id", r -> r.getExternalId().split("_", 2)[1]),
      new Column<>("Email", Recipient::getEmail),
      new Column<>("Numer telefonu", Recipient::getPhoneNumber)
    );

    public byte[] generateRecipientsReport(List<Recipient> recipients) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Odbiorcy");
        writeToSheet(sheet, recipients, COLUMNS);
        return getBytesFromWorkbook(workbook);
    }

}
