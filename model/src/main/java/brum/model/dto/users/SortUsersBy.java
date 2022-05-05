package brum.model.dto.users;

public enum SortUsersBy {
    CREATED_AT("createdAt"),
    CREATED_BY("createdBy.username"),
    ROLE("role.name"),
    USERNAME("username"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    PHONE_NUMBER("phoneNumber");

    private final String path;

    SortUsersBy(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
