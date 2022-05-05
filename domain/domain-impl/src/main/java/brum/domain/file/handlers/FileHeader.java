package brum.domain.file.handlers;

import java.util.Arrays;
import java.util.List;

public enum FileHeader {
    FIRST_NAME("imie", "imię", "imi?", "first name", "firstname"),
    LAST_NAME("nazwisko", "last name", "lastname"),
    DOCUMENT_NUMBER("pesel", "document number", "documentnumber"),
    EMAIL("email", "e-mail"),
    PHONE_NUMBER("numer telefonu", "telefon", "phone number", "phonenumber", "phone"),
    IS_ACTIVE("aktywność", "aktywno??", "isactive", "is active", "activity"),
    ID("external_id", "external id", "id", "identyfikator"),
    SOURCE_SYSTEM("source_system_id", "source system id", "source system", "system źródłowy", "system ?r?d?owy");

    private final List<String> headerNames;

    FileHeader(String... names) {
        this.headerNames = Arrays.asList(names);
    }

    public List<String> getHeaderNames() {
        return headerNames;
    }
}
