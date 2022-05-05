package brum.domain.file.writers;

import brum.model.dto.identities.Identity;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;

public class WriteIdentitiesExcelFile extends ExcelFileWriter {

    private static final List<Column<Identity>> COLUMNS = Arrays.asList(
            new Column<>("Id", Identity::getExternalId),
            new Column<>("Imię", Identity::getFirstName),
            new Column<>("Nazwisko", Identity::getLastName),
            new Column<>("Numer dokumentu", Identity::getDocumentNumber),
            new Column<>("Email", Identity::getEmail),
            new Column<>("Numer telefonu", Identity::getPhoneNumber),
            new Column<>("Data dodania", Identity::getCreatedAt),
            new Column<>("Utworzony przez (id)", r -> r.getCreatedBy().getExternalId()),
            new Column<>("Utworzony przez (nazwa użytkownika)", r -> r.getCreatedBy().getUsername()),
            new Column<>("Utworzony przez (imię i nazwisko)", r -> r.getCreatedBy().getFirstName() + " " + r.getCreatedBy().getLastName()),
            new Column<>("Utworzony przez (adres email)", r -> r.getCreatedBy().getEmail()),
            new Column<>("Utworzony przez (numer telefonu)", r -> r.getCreatedBy().getPhoneNumber()),
            new Column<>("Aktywny", Identity::getIsActive)
    );

    public byte[] generateIdentitiesReport(List<Identity> identities) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Identities");
        writeToSheet(sheet, identities, COLUMNS);
        return getBytesFromWorkbook(workbook);
    }

}
