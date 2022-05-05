package brum.persistence.filters.mapper;

import brum.model.dto.common.SortOrder;
import brum.model.dto.users.SortUsersBy;
import org.springframework.data.domain.Sort;

import static brum.model.dto.users.SortUsersBy.*;

public enum SortUsersByMapper {
    INSTANCE;

    public Sort map(SortUsersBy sortBy, SortOrder order) {
        Sort.Direction direction;
        if (order == null || order.equals(SortOrder.ASCENDING)) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        sortBy = sortBy == null ? LAST_NAME : sortBy;
        switch (sortBy) {
            case CREATED_AT:
                return sortByCreatedAt(direction);
            case USERNAME:
                return sortByUsername(direction);
            case LAST_NAME:
                return sortByLastName(direction);
            case EMAIL:
                return sortByEmail(direction);
            case ROLE:
                return sortByRole(direction);
            case CREATED_BY:
                return sortByCreatedBy(direction);
            case PHONE_NUMBER:
                return sortByPhoneNumber(direction);
            default:
                return sortByFirstName(direction);
        }
    }

    private Sort sortByCreatedAt(Sort.Direction direction) {
        return Sort.by(direction, CREATED_AT.getPath());
    }

    private Sort sortByUsername(Sort.Direction direction) {
        return Sort.by(direction, USERNAME.getPath());
    }

    private Sort sortByLastName(Sort.Direction direction) {
        return Sort.by(direction, LAST_NAME.getPath()).and(Sort.by(direction, FIRST_NAME.getPath()).and(sortByCreatedAt(direction)));
    }

    private Sort sortByFirstName(Sort.Direction direction) {
        return Sort.by(direction, FIRST_NAME.getPath()).and(Sort.by(direction, LAST_NAME.getPath()).and(sortByCreatedAt(direction)));
    }

    private Sort sortByEmail(Sort.Direction direction) {
        return Sort.by(direction, EMAIL.getPath());
    }

    private Sort sortByPhoneNumber(Sort.Direction direction) {
        return Sort.by(direction, PHONE_NUMBER.getPath());
    }

    private Sort sortByRole(Sort.Direction direction) {
        return Sort.by(direction, ROLE.getPath()).and(Sort.by(direction, CREATED_BY.getPath()).and(sortByCreatedAt(direction)));
    }

    private Sort sortByCreatedBy(Sort.Direction direction) {
        return Sort.by(direction, CREATED_BY.getPath()).and(Sort.by(direction, ROLE.getPath()).and(sortByCreatedAt(direction)));
    }
}
