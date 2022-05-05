package brum.persistence;

import brum.model.dto.common.Parameter;
import brum.model.dto.common.ParameterKey;

import java.io.Serializable;
import java.util.List;

public interface ParameterPersistenceGateway {
    <O extends Serializable> O getParameterValue(ParameterKey key);
    List<Parameter> getNonHiddenParameters();
}
