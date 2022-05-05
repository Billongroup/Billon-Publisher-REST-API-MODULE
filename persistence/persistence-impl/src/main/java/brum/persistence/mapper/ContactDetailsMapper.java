package brum.persistence.mapper;

import brum.model.dto.recipients.ContactDetails;
import brum.model.dto.recipients.ContactDetailsType;
import brum.model.dto.recipients.Recipient;
import brum.persistence.entity.ContactDetailsEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum ContactDetailsMapper implements AbstractPersistenceMapper<ContactDetailsEntity, ContactDetails> {
    INSTANCE;


    @Override
    public ContactDetails mapFromEntity(ContactDetailsEntity entity) {
        if (entity == null) {
            return null;
        }
        ContactDetails dto = new ContactDetails();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setType(entity.getType());
        return dto;
    }

    @Override
    public ContactDetailsEntity mapFromDto(ContactDetails dto) {
        if (dto == null) {
            return null;
        }
        ContactDetailsEntity entity = new ContactDetailsEntity();
        entity.setId(dto.getId());
        entity.setValue(dto.getValue());
        entity.setType(dto.getType());
        return entity;
    }
    
    public List<ContactDetailsEntity> mapToListFromRecipientDto(Recipient recipient) {
        List<ContactDetailsEntity> result = new ArrayList<>();
        ContactDetailsEntity email = new ContactDetailsEntity();
        email.setValue(recipient.getEmail());
        email.setType(ContactDetailsType.EMAIL);
        result.add(email);
        if (StringUtils.hasText(recipient.getPhoneNumber())) {
            ContactDetailsEntity phone = new ContactDetailsEntity();
            phone.setValue(recipient.getPhoneNumber());
            phone.setType(ContactDetailsType.PHONE);
            result.add(phone);
        }
        return result;
    }
}
