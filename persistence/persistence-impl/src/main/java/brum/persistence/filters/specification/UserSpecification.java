package brum.persistence.filters.specification;

import brum.persistence.entity.UserEntity;

public class UserSpecification extends AbstractSpecification<UserEntity> {
    public UserSpecification(String key, Operation operation, Object value) {
        super(key, operation, value);
    }
}
