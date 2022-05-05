package brum.model.dto.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Parameter {
    private ParameterKey key;
    private Serializable value;
}
