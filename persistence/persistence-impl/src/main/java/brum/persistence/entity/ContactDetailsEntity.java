package brum.persistence.entity;

import brum.model.dto.recipients.ContactDetailsType;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact_details")
@Data
public class ContactDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @Enumerated(EnumType.STRING)
    private ContactDetailsType type;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @ToString.Exclude
    private RecipientEntity recipient;

    @OneToMany(mappedBy = "key.contactDetailsId", fetch = FetchType.LAZY)
    private List<DocumentContactDetailsEntity> notifications = new ArrayList<>();
}
