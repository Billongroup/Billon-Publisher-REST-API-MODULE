package brum.persistence.entity;

import brum.model.dto.recipients.DocumentNotificationStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "document_contact_details")
public class DocumentContactDetailsEntity {
    @EmbeddedId
    @AttributeOverride(name = "documentId", column = @Column(name = "document_id"))
    @AttributeOverride(name = "contactDetailsId", column = @Column(name = "contact_details_id"))
    private DocumentContactDetailsId key;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id" , insertable = false, updatable = false)
    private PublicDocumentEntity document;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_details_id", insertable = false, updatable = false)
    private ContactDetailsEntity contactDetails;
    @Enumerated(EnumType.STRING)
    private DocumentNotificationStatus status;
}
