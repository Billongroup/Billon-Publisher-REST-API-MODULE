package brum.persistence.entity;

import brum.model.dto.documents.PublicationStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "base_documents")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DocumentBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus;
    private String jobId;
    private String blockchainAddress;
}
