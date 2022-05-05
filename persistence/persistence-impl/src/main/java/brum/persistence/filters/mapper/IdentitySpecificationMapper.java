package brum.persistence.filters.mapper;

import brum.model.dto.identities.IdentityFilters;
import brum.persistence.filters.specification.IdentitySpecification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static brum.persistence.filters.specification.Operation.*;

public class IdentitySpecificationMapper {
    private IdentitySpecificationMapper() {}

    public static List<IdentitySpecification> mapToSpecification(IdentityFilters filters) {
        if (filters == null ) {
            return new ArrayList<>();
        }
        List<IdentitySpecification> specifications = new ArrayList<>();
        if (StringUtils.hasText(filters.getFirstName())) {
            specifications.add(new IdentitySpecification("firstName", LIKE, filters.getFirstName()));
        }
        if (StringUtils.hasText(filters.getLastName())) {
            specifications.add(new IdentitySpecification("lastName", LIKE, filters.getLastName()));
        }
        if (StringUtils.hasText(filters.getDocumentNumber())) {
            specifications.add(new IdentitySpecification("documentNumber", LIKE, filters.getDocumentNumber()));
        }
        if (StringUtils.hasText(filters.getEmail())) {
            specifications.add(new IdentitySpecification("email", LIKE, filters.getEmail()));
        }
        if (StringUtils.hasText(filters.getPhoneNumber())) {
            specifications.add(new IdentitySpecification("phoneNumber", LIKE, filters.getPhoneNumber()));
        }
        return specifications;
    }
}
