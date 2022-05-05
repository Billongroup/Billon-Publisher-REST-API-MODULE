package brum.model.dto.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UniqueConstraint {
    // Identities
    DOCUMENT_NUMBER("identities_document_number_key"),
    EMAIL("identities_email_key", "users_email_key"),
    PHONE_NUMBER("identities_phone_number_key", "users_phone_number_key"),

    // Users
    USERNAME("users_username_key");

    private final String[] constraintNames;

    UniqueConstraint(String... constraintNames) {
        this.constraintNames = constraintNames;
    }

    public static List<String> getHandledConstraints() {
        List<String> result = new ArrayList<>();
        for (UniqueConstraint constraint : values()) {
            result.addAll(Arrays.asList(constraint.constraintNames));
        }
        return result;
    }

    public static UniqueConstraint fromValue(String value) {
        for (UniqueConstraint constraint : values()) {
            if (Arrays.asList(constraint.constraintNames).contains(value)) {
                return constraint;
            }
        }
        return null;
    }
}
