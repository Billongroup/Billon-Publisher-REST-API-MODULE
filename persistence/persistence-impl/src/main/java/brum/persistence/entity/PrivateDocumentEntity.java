package brum.persistence.entity;

import brum.model.dto.documents.ForgettingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "private_documents")
@PrimaryKeyJoinColumn(name = "id")
public class PrivateDocumentEntity extends DocumentBaseEntity {
    private String forgettingJobId;
    @Enumerated(EnumType.STRING)
    private ForgettingStatus forgettingStatus;
    @ManyToOne
    @JoinColumn(name = "identity_id")
    private IdentityEntity identity;
}
