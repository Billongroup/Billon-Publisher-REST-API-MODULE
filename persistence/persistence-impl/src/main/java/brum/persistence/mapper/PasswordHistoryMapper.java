package brum.persistence.mapper;

import brum.model.dto.users.User;
import brum.model.dto.users.UserHistory;
import brum.persistence.entity.PasswordHistoryEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum PasswordHistoryMapper implements AbstractPersistenceMapper<PasswordHistoryEntity, String> {
    INSTANCE;

    @Override
    public List<String> mapToListFromEntities(Collection<? extends PasswordHistoryEntity> entities) {
        List<String> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        Comparator<PasswordHistoryEntity> idDescendingComparator = Comparator.comparing(PasswordHistoryEntity::getId).reversed();
        List<PasswordHistoryEntity> sorted = entities.stream().sorted(idDescendingComparator).collect(Collectors.toList());
        for (PasswordHistoryEntity entity : sorted) {
            dtos.add(mapFromEntity(entity));
        }
        return dtos;
    }

    @Override
    public String mapFromEntity(PasswordHistoryEntity entity) {
        return entity.getPassword();
    }

    @Override
    public PasswordHistoryEntity mapFromDto(String dto) {
        PasswordHistoryEntity entity = new PasswordHistoryEntity();
        entity.setPassword(dto);
        return entity;
    }

    public PasswordHistoryEntity mapFromDto(User user) {
        PasswordHistoryEntity entity = new PasswordHistoryEntity();
        entity.setPassword(user.getPassword());
        return entity;
    }
}
