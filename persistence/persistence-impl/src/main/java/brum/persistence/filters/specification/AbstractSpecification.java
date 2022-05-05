package brum.persistence.filters.specification;

import brum.common.utils.CalendarUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public abstract class AbstractSpecification<T> implements Specification<T> {

    private final String key;
    private final Operation operation;
    private final transient Object value;
    private String propertyKey;
    private String mapValueKey;
    private String mapValuePropertyKey;

    protected AbstractSpecification(String key, Operation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    protected AbstractSpecification(String key, String mapValueKey, Operation operation, Object value) {
        this(key, operation, value);
        this.mapValueKey = mapValueKey;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        Path<T> path = root;
        propertyKey = key;
        while (propertyKey.contains(".")) {
            String subKey = propertyKey.substring(0, propertyKey.indexOf("."));
            propertyKey = propertyKey.substring(propertyKey.indexOf(".") + 1);
            path = path.get(subKey);
        }
        Path<T> mapValuePath = root;
        mapValuePropertyKey = mapValueKey;
        if (mapValueKey != null) {
            while (mapValuePropertyKey.contains(".")) {
                String subKey = mapValuePropertyKey.substring(0, mapValuePropertyKey.indexOf("."));
                mapValuePropertyKey = mapValuePropertyKey.substring(mapValuePropertyKey.indexOf(".") + 1);
                mapValuePath = mapValuePath.get(subKey);
            }
        }
        switch (operation) {
            case LIKE:
                return handleLike(path, builder);
            case EQUAL:
                return handleEqual(path, builder);
            case LESS_THAN:
                return handleLessThan(path, builder);
            case GREATER_THAN:
                return handleGreaterThan(path, builder);
            case IN:
                return handleIn(path, builder);
            case MAP_IN:
                return handleMapIn(path, mapValuePath, builder);
            default:
                return null;
        }
    }

    protected Predicate handleLike(Path<T> path, CriteriaBuilder builder) {
        if (path.get(propertyKey).getJavaType() == String.class) {
            return builder.like(builder.lower(path.get(propertyKey)), ((String) value).toLowerCase() + "%");
        }
        return builder.and();
    }

    protected Predicate handleEqual(Path<T> path, CriteriaBuilder builder) {
        return builder.equal(path.get(propertyKey), value);
    }

    protected Predicate handleLessThan(Path<T> path, CriteriaBuilder builder) {
        if (path.get(propertyKey).getJavaType() == Date.class) {
            return builder.lessThanOrEqualTo(path.get(propertyKey), CalendarUtils.localDateTimeToDate((LocalDateTime) value));
        }
        return builder.and();
    }

    protected Predicate handleGreaterThan(Path<T> path, CriteriaBuilder builder) {
        if (path.get(propertyKey).getJavaType() == Date.class) {
            return builder.greaterThanOrEqualTo(path.get(propertyKey), CalendarUtils.localDateTimeToDate((LocalDateTime) value));
        }
        return builder.and();
    }

    protected Predicate handleIn(Path<T> path, CriteriaBuilder builder) {
        if (!(value instanceof Collection<?>)) {
            return builder.and();
        }
        CriteriaBuilder.In<Object> in = builder.in(path.get(propertyKey));
        for (Object object : (Collection<?>) value) {
            in.value(object);
        }
        return in;
    }

    protected Predicate handleMapIn(Path<T> path, Path<T> mapValuePath, CriteriaBuilder builder) {
        if (!(value instanceof Map<?, ?>)) {
            return builder.and();
        }
        Predicate dj = builder.disjunction();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            Predicate cj = builder.conjunction();
            cj = builder.and(cj, builder.equal(path.get(propertyKey), entry.getKey()));
            if (entry.getValue() instanceof Collection<?>) {
                CriteriaBuilder.In<Object> in = builder.in(mapValuePath.get(mapValuePropertyKey));
                for (Object object : (Collection<?>) entry.getValue()) {
                    in.value(object);
                }
                cj = builder.and(cj, in);
            } else {
                cj = builder.and(cj, builder.equal(mapValuePath.get(mapValuePropertyKey), entry.getValue()));
            }
            dj = builder.or(dj, cj);
        }
        return builder.and(dj);
    }

}
