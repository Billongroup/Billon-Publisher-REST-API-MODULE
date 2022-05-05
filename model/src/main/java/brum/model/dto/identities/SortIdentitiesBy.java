package brum.model.dto.identities;

public enum SortIdentitiesBy {
    CREATED_AT("createdAt"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    DOCUMENT_NUMBER("documentNumber"),
    PHONE_NUMBER("phoneNumber"),
    FIRST_NAME("firstName");

    private final String path;

    SortIdentitiesBy(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
