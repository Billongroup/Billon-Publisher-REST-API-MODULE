package brum.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "public_documents")
@PrimaryKeyJoinColumn(name = "id")
public class PublicDocumentEntity extends DocumentBaseEntity {
    @OneToMany(mappedBy = "key.documentId", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<DocumentContactDetailsEntity> notifications;
}
