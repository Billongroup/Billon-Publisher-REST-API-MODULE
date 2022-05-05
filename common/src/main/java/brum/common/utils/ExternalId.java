package brum.common.utils;

import lombok.Getter;

import java.util.UUID;

public enum ExternalId {
    GENERAL(""),
    USER("user-"),
    ROLE("role-"),
    PRIVILEGE("privilege-"),
    IDENTITY("identity-");

    @Getter
    private final String prefix;

    ExternalId(String prefix) {
        this.prefix = prefix;
    }

    public String generateId() {
        return this.prefix + UUID.randomUUID();
    }

}
