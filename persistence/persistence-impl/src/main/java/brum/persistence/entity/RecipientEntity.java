package brum.persistence.entity;

import brum.model.dto.recipients.ContactDetailsType;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "recipients")
public class RecipientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ContactDetailsEntity> contactDetailsList = new ArrayList<>();

    public List<String> getContactDetailsValues(ContactDetailsType type) {
        return contactDetailsList.stream().filter(cd -> cd.getType().equals(type)).map(ContactDetailsEntity::getValue).collect(Collectors.toList());
    }

    public List<ContactDetailsEntity> getContactDetailsEntities(ContactDetailsType type) {
        return contactDetailsList.stream().filter(cd -> cd.getType().equals(type)).collect(Collectors.toList());
    }
}
