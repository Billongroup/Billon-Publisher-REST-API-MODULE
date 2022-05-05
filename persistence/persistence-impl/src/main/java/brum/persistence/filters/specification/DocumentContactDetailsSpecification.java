package brum.persistence.filters.specification;

import brum.persistence.entity.DocumentContactDetailsEntity;

public class DocumentContactDetailsSpecification extends AbstractSpecification<DocumentContactDetailsEntity> {
    public DocumentContactDetailsSpecification(String key, Operation operation, Object value) {
        super(key, operation, value);
    }

    public DocumentContactDetailsSpecification(String key, String mapValueKey, Operation operation, Object value) {
        super(key, mapValueKey, operation, value);
    }
}
