package brum.persistence.filters.mapper;

import brum.model.dto.users.UserFilters;
import brum.persistence.filters.specification.UserSpecification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static brum.persistence.filters.specification.Operation.*;

public class UserSpecificationMapper {
    private UserSpecificationMapper() {}

    public static List<UserSpecification> mapToSpecification(UserFilters filters) {
        if (filters == null) {
            return new ArrayList<>();
        }
        List<UserSpecification> specifications = new ArrayList<>();
        if (StringUtils.hasText(filters.getId())) {
            specifications.add(new UserSpecification("externalId", LIKE, filters.getId()));
        }
        if (StringUtils.hasText(filters.getUsername())) {
            specifications.add(new UserSpecification("username", LIKE, filters.getUsername()));
        }
        if (filters.getRole() != null) {
            specifications.add(new UserSpecification("role.name", EQUAL, filters.getRole()));
        }
        if (StringUtils.hasText(filters.getCreatedBy())) {
            specifications.add(new UserSpecification("createdBy.username", LIKE, filters.getCreatedBy()));
        }
        if (StringUtils.hasText(filters.getFirstName())) {
            specifications.add(new UserSpecification("firstName", LIKE, filters.getFirstName()));
        }
        if (StringUtils.hasText(filters.getLastName())) {
            specifications.add(new UserSpecification("lastName", LIKE, filters.getLastName()));
        }
        if (filters.getIsActive() != null) {
            specifications.add(new UserSpecification("isActive", EQUAL, filters.getIsActive()));
        }
        if (filters.getIsRobot() != null) {
            specifications.add(new UserSpecification("isRobot", EQUAL, filters.getIsRobot()));
        }
        if (filters.getCreatedAt() != null) {
            if (filters.getCreatedAt().getFrom() != null) {
                specifications.add(new UserSpecification("createdAt", GREATER_THAN, filters.getCreatedAt().getFrom()));
            }
            if (filters.getCreatedAt().getTo() != null) {
                specifications.add(new UserSpecification("createdAt", LESS_THAN, filters.getCreatedAt().getTo()));
            }
        }
        return specifications;
    }
}
