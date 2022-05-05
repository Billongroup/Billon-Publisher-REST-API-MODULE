package brum.persistence.mapper;

import brum.model.dto.recipients.ContactDetailsType;
import brum.model.dto.recipients.Recipient;
import brum.persistence.entity.ContactDetailsEntity;
import brum.persistence.entity.RecipientEntity;

public enum RecipientMapper implements AbstractPersistenceMapper<RecipientEntity, Recipient> {
    INSTANCE;


    @Override
    public Recipient mapFromEntity(RecipientEntity entity) {
        if (entity == null) {
            return null;
        }
        Recipient result = new Recipient();
        result.setId(entity.getId());
        result.setExternalId(entity.getExternalId());
        result.setEmail(entity.getContactDetailsValues(ContactDetailsType.EMAIL).get(0));
        if (!entity.getContactDetailsValues(ContactDetailsType.PHONE).isEmpty()) {
            result.setPhoneNumber(entity.getContactDetailsValues(ContactDetailsType.PHONE).get(0));
        }
        return result;
    }

    @Override
    public RecipientEntity mapFromDto(Recipient dto) {
        if (dto == null) {
            return null;
        }
        RecipientEntity result = new RecipientEntity();
        result.setId(dto.getId());
        result.setExternalId(dto.getExternalId());
        result.setContactDetailsList(ContactDetailsMapper.INSTANCE.mapToListFromRecipientDto(dto));
        for (ContactDetailsEntity cd : result.getContactDetailsList()) {
            cd.setRecipient(result);
        }
        return result;
    }

}
