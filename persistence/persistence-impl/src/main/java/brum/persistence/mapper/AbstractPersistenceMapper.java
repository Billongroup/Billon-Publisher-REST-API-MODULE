package brum.persistence.mapper;

import java.util.*;

public interface AbstractPersistenceMapper<E, D> {

    D mapFromEntity(E entity);

    E mapFromDto(D dto);

    default List<D> mapToListFromEntities(Collection<? extends E> entities) {
        List<D> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (E entity : entities) {
            dtos.add(mapFromEntity(entity));
        }
        return dtos;
    }

    default List<E> mapToListFromDtos(Collection<D> dtos) {
        List<E> entities = new ArrayList<>();
        if (dtos == null) {
            return entities;
        }
        for (D dto : dtos) {
            entities.add(mapFromDto(dto));
        }
        return entities;
    }

    default Set<D> mapToSetFromEntities(Collection<E> entities) {
        Set<D> dtos = new HashSet<>();
        if (entities == null) {
            return dtos;
        }
        for (E entity : entities) {
            dtos.add(mapFromEntity(entity));
        }
        return dtos;
    }

    default Set<E> mapToSetFromDtos(Collection<D> dtos) {
        Set<E> entities = new HashSet<>();
        if (dtos == null) {
            return entities;
        }
        for (D dto : dtos) {
            entities.add(mapFromDto(dto));
        }
        return entities;
    }
}
