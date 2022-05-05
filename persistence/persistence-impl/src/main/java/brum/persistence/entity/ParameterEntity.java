package brum.persistence.entity;

import brum.model.dto.common.ParameterKey;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "parameters")
public class ParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ParameterKey key;
    private String value;
    private Boolean hidden;
    private String description;
}
