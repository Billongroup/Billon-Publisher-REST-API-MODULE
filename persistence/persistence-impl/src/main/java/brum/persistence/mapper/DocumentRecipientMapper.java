package brum.persistence.mapper;

import brum.model.dto.recipients.*;
import brum.persistence.entity.DocumentContactDetailsEntity;

import java.util.*;

public enum DocumentRecipientMapper implements AbstractPersistenceMapper<DocumentContactDetailsEntity, ContactDetails> {
    INSTANCE;
    private static final List<DocumentNotificationStatus> STATUSES_TO_MAP = Arrays.asList(
            DocumentNotificationStatus.NEW, DocumentNotificationStatus.UPDATED);

    @Override
    public ContactDetails mapFromEntity(DocumentContactDetailsEntity entity) {
        if (entity == null) {
            return null;
        }
        ContactDetails dto = ContactDetailsMapper.INSTANCE.mapFromEntity(entity.getContactDetails());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    @Override
    public DocumentContactDetailsEntity mapFromDto(ContactDetails dto) {
        return null;
    }

    public List<Recipient> mapToRecipientList(List<DocumentContactDetailsEntity> entities) {
        Map<Long, Recipient> resultMap = new HashMap<>();
        for (DocumentContactDetailsEntity entity : entities) {
            if (!resultMap.containsKey(entity.getContactDetails().getRecipient().getId())) {
                Recipient recipient = new Recipient();
                recipient.setId(entity.getContactDetails().getRecipient().getId());
                recipient.setExternalId(entity.getContactDetails().getRecipient().getExternalId());
                resultMap.put(entity.getContactDetails().getRecipient().getId(), recipient);
            }
            if (entity.getContactDetails().getType().equals(ContactDetailsType.EMAIL)) {
                resultMap.get(entity.getContactDetails().getRecipient().getId()).setEmail(entity.getContactDetails().getValue());
            } else if (entity.getContactDetails().getType().equals(ContactDetailsType.PHONE)) {
                resultMap.get(entity.getContactDetails().getRecipient().getId()).setPhoneNumber(entity.getContactDetails().getValue());
            }
        }
        return new ArrayList<>(resultMap.values());
    }

    public List<DocumentRecipient> mapToDocumentRecipientList(List<DocumentContactDetailsEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<DocumentRecipient> result = new ArrayList<>();
        for (DocumentContactDetailsEntity entity : entities) {
            DocumentRecipient dto = new DocumentRecipient();
            dto.setContactDetailsId(entity.getContactDetails().getId());
            dto.setExternalId(entity.getContactDetails().getRecipient().getExternalId());
            dto.setContactDetails(entity.getContactDetails().getValue());
            dto.setType(entity.getContactDetails().getType());
            if (STATUSES_TO_MAP.contains(entity.getStatus())) {
                dto.setStatus(DocumentRecipientStatus.valueOf(entity.getStatus().name()));
            }
            result.add(dto);
        }
        return result;
    }
}
