package brum.persistence.filters.mapper;

import brum.model.dto.common.SortOrder;
import brum.model.dto.identities.SortIdentitiesBy;
import org.springframework.data.domain.Sort;

import static brum.model.dto.identities.SortIdentitiesBy.*;

public enum SortIdentitiesByMapper {
    INSTANCE;

    public Sort map(SortIdentitiesBy sortBy, SortOrder order) {
        Sort.Direction direction;
        if (order == null || order.equals(SortOrder.ASCENDING)) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        sortBy = sortBy == null ? LAST_NAME : sortBy;
        switch(sortBy) {
            case CREATED_AT:
                return sortByCreatedAt(direction);
            case EMAIL:
                return sortByEmail(direction);
            case DOCUMENT_NUMBER:
                return sortByDocumentNumber(direction);
            case PHONE_NUMBER:
                return sortByPhoneNumber(direction);
            case FIRST_NAME:
                return sortByFirstName(direction);
            default:
                return sortByLastName(direction);
        }
    }

    private Sort sortByCreatedAt(Sort.Direction direction) {
        return Sort.by(direction, CREATED_AT.getPath()).and(sortByLastName(direction));
    }

    private Sort sortByLastName(Sort.Direction direction) {
        return Sort.by(direction, LAST_NAME.getPath()).and(sortByEmail(direction));
    }

    private Sort sortByEmail(Sort.Direction direction) {
        return Sort.by(direction, EMAIL.getPath()).and(sortByDocumentNumber(direction));
    }

    private Sort sortByDocumentNumber(Sort.Direction direction) {
        return Sort.by(direction, DOCUMENT_NUMBER.getPath()).and(sortByPhoneNumber(direction));
    }

    private Sort sortByPhoneNumber(Sort.Direction direction) {
        return Sort.by(direction, PHONE_NUMBER.getPath()).and(sortByFirstName(direction));
    }

    private Sort sortByFirstName(Sort.Direction direction) {
        return Sort.by(direction, FIRST_NAME.getPath());
    }

}
