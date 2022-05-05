package brum.domain.file.writers;

import brum.model.dto.documents.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;

public class WriteDocumentsExcelFile extends ExcelFileWriter {

    private static final List<Column<Document>> COLUMNS = Arrays.asList(
            new Column<>("Data publikacji", Document::getPublicationDate),
            new Column<>("Tytuł dokumentu", Document::getTitle),
            new Column<>("Typ dokumentu", d -> mapDocumentType(d.getDocumentType())),
            new Column<>("Status dokumentu", d -> mapDocumentStatus(d.getStatus(), d.getForgettingStatus())),
            new Column<>("Status publikacji", d -> mapPublicationStatus(d.getDocumentPublicationStatus())),
            new Column<>("Adres blockchain", Document::getDocumentBlockchainAddress),
            new Column<>("Identyfikator operacji", Document::getJobId),
            new Column<>("Ważny od", Document::getValidSince),
            new Column<>("Ważny do", Document::getValidUntil),
            new Column<>("Przechowywany do", Document::getRetainUntil),
            new Column<>("Rozmiar dokumentu [kB]", d -> (float) d.getFileSize() / 1000),
            new Column<>("Kategoria", Document::getCategory),
            new Column<>("Przekazany przez (login)", d -> d.getPublishedBy() == null ? null : d.getPublishedBy().getUsername()),
            new Column<>("Przekazany przez (Imię i nazwisko)", WriteDocumentsExcelFile::getFullName),
            new Column<>("Przekazany przez (email)", d -> d.getPublishedBy() == null ? null : d.getPublishedBy().getEmail()),
            new Column<>("Przekazany przez (tel)", d -> d.getPublishedBy() == null ? null : d.getPublishedBy().getPhoneNumber()),
            new Column<>("ID Odbiorcy", d -> d.getIdentity() == null ? null : d.getIdentity().getDocumentNumber()),
            new Column<>("Imię Odbiorcy", d -> d.getIdentity() == null ? null : d.getIdentity().getFirstName()),
            new Column<>("Nazwisko Odbiorcy", d -> d.getIdentity() == null ? null : d.getIdentity().getLastName()),
            new Column<>("Email Odbiorcy", d -> d.getIdentity() == null ? null : d.getIdentity().getEmail()),
            new Column<>("Telefon kontaktowy Odbiorcy", d -> d.getIdentity() == null ? null : d.getIdentity().getPhoneNumber())
    );

    public byte[] generateDocumentsReport(List<Document> documents) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        writeToSheet(sheet, documents, COLUMNS);
        return getBytesFromWorkbook(workbook);
    }

    private static String getFullName(Document d) {
        if (d == null) {
            return "";
        }
        if (d.getPublishedBy().getFirstName() == null || d.getPublishedBy().getLastName() == null) {
            return "";
        }
        return d.getPublishedBy().getFirstName() + " " + d.getPublishedBy().getLastName();
    }

    private static String mapDocumentType(DocumentType documentType) {
        switch (documentType) {
            case PRIVATE:
                return "Prywatny";
            case PUBLIC:
                return "Publiczny";
            default:
                return null;
        }
    }

    private static String mapDocumentStatus(DocumentStatus documentStatus, ForgettingStatus forgettingStatus) {
        if (ForgettingStatus.FORGETTING_IN_PROGRESS.equals(forgettingStatus)) {
            return "W trakcie zapomnienia";
        }
        switch (documentStatus) {
            case CURRENT:
                return "Opublikowany";
            case UPLOADING:
                return "Wczytywanie";
            case PREPARED_TO_SIGN:
                return "Przygotowany";
            case DEPRECATED:
                return "Poprzednia wersja";
            case NOT_EXIST:
            case NOT_AVAILABLE:
                return "Nie istnieje";
            default:
                return null;
        }
    }

    private static String mapPublicationStatus(PublicationStatus publicationStatus) {
        switch (publicationStatus) {
            case PREPARED_TO_SIGN:
                return "Przygotowany";
            case PUBLISHING_INITIATED:
                return "W trakcie publikacji";
            case PUBLISHING_OK:
                return "Opublikowano";
            case PUBLISHING_ERROR:
                return "Błąd publikacji";
            default:
                return null;
        }
    }

}
