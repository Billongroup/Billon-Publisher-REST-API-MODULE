package brum.persistence.filters.specification;

import brum.persistence.entity.IdentityEntity;

public class IdentitySpecification extends AbstractSpecification<IdentityEntity> {
    public IdentitySpecification(String key, Operation operation, Object value) {
        super(key, operation, value);
    }
}
