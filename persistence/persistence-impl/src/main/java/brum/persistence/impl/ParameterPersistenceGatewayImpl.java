package brum.persistence.impl;

import brum.model.dto.common.Parameter;
import brum.model.dto.common.ParameterKey;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.entity.ParameterEntity;
import brum.persistence.repository.ParameterRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional
public class ParameterPersistenceGatewayImpl implements ParameterPersistenceGateway {

    private final ParameterRepository parameterRepository;

    private static final Map<Class<? extends Serializable>, Function<String, ? extends Serializable>> VALUE_MAPPERS = new HashMap<>();

    static {
        VALUE_MAPPERS.put(String.class, v -> v);
        VALUE_MAPPERS.put(Boolean.class, Boolean::valueOf);
        VALUE_MAPPERS.put(Long.class, Long::valueOf);
    }

    public ParameterPersistenceGatewayImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public <O extends Serializable> O getParameterValue(ParameterKey key) {
        ParameterEntity entity = parameterRepository.getParameterEntityByKey(key);
        if (entity == null) {
            return null;
        }
        try {
            return (O) (VALUE_MAPPERS.get(entity.getKey().getClazz()).apply(entity.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Parameter> getNonHiddenParameters() {
        List<ParameterEntity> entities = parameterRepository.getNonHiddenParameterEntities();
        List<Parameter> result = new ArrayList<>();
        for (ParameterEntity entity : entities) {
            Parameter parameter = new Parameter();
            parameter.setKey(entity.getKey());
            parameter.setValue(VALUE_MAPPERS.get(entity.getKey().getClazz()).apply(entity.getValue()));
            result.add(parameter);
        }
        return result;
    }
}
