package brum.persistence.impl;

import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.common.SortOrder;
import brum.model.dto.documents.Document;
import brum.model.dto.documents.PublicationStatus;
import brum.model.dto.recipients.*;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.RecipientPersistenceGateway;
import brum.persistence.entity.*;
import brum.persistence.filters.mapper.DocumentContactDetailsSpecificationMapper;
import brum.persistence.filters.mapper.PaginationMapper;
import brum.persistence.filters.mapper.SortByMapper;
import brum.persistence.filters.specification.DocumentContactDetailsSpecification;
import brum.persistence.mapper.DocumentRecipientMapper;
import brum.persistence.mapper.RecipientMapper;
import brum.persistence.repository.ContactDetailsRepository;
import brum.persistence.repository.DocumentRecipientRepository;
import brum.persistence.repository.PublicDocumentRepository;
import brum.persistence.repository.RecipientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipientPersistenceGatewayImpl implements RecipientPersistenceGateway {

    private final RecipientRepository recipientRepository;
    private final DocumentRecipientRepository documentRecipientRepository;
    private final PublicDocumentRepository publicDocumentRepository;
    private final ContactDetailsRepository contactDetailsRepository;

    public RecipientPersistenceGatewayImpl(RecipientRepository recipientRepository,
                                           DocumentRecipientRepository documentRecipientRepository,
                                           PublicDocumentRepository publicDocumentRepository,
                                           ContactDetailsRepository contactDetailsRepository) {
        this.recipientRepository = recipientRepository;
        this.documentRecipientRepository = documentRecipientRepository;
        this.publicDocumentRepository = publicDocumentRepository;
        this.contactDetailsRepository = contactDetailsRepository;
    }

    @Override
    public List<Recipient> getDocumentRecipients(String documentId) {
        PublicDocumentEntity document = publicDocumentRepository.getByJobIdOrBlockchainAddress(documentId, documentId);
        if (isGetDocumentRecipientsInvalid(document)) {
            return null;
        }
        return DocumentRecipientMapper.INSTANCE.mapToRecipientList(document.getNotifications());
    }

    @Override
    public List<DocumentRecipient> getDocumentRecipients(String documentId, Map<String, List<ContactDetailsType>> idContactDetailsMap) {
        if (isGetDocumentRecipientsInvalid(documentId)) {
            return null;
        }
        Specification<DocumentContactDetailsEntity> specification = combineSpecifications(
                DocumentContactDetailsSpecificationMapper.mapToSpecification(idContactDetailsMap, documentId));
        List<DocumentContactDetailsEntity> found = documentRecipientRepository.findAll(specification);
        return DocumentRecipientMapper.INSTANCE.mapToDocumentRecipientList(found);
    }

    @Override
    public PaginatedResponse<DocumentRecipient> getDocumentRecipients(String documentId, DocumentRecipientSearchCriteria searchCriteria) {
        if (isGetDocumentRecipientsInvalid(documentId)) {
            return null;
        }
        Specification<DocumentContactDetailsEntity> specification = combineSpecifications(
                DocumentContactDetailsSpecificationMapper.mapToSpecification(
                        searchCriteria == null ? new DocumentRecipientFilters() : searchCriteria.getFilters(),
                        documentId));
        PageRequest pagination = PaginationMapper.map(searchCriteria == null ? null : searchCriteria.getPagination());
        SortOrder order = searchCriteria == null || searchCriteria.getSort() == null || searchCriteria.getSort().getOrder() == null ?
                SortOrder.ASCENDING : searchCriteria.getSort().getOrder();
        pagination = pagination.withSort(SortByMapper.INSTANCE.map("contactDetails.recipient.externalId", order));
        Page<DocumentContactDetailsEntity> page = documentRecipientRepository.findAll(specification, pagination);
        PaginatedResponse<DocumentRecipient> result = new PaginatedResponse<>();
        result.setRows(DocumentRecipientMapper.INSTANCE.mapToDocumentRecipientList(page.getContent()));
        result.setCount(page.getTotalElements());
        return result;
    }

    @Override
    public Map<String, Recipient> getIdRecipientMapByExtIdSet(Set<String> extIdSet) {
        List<RecipientEntity> recipients = recipientRepository.findAllByExternalIdIn(extIdSet);
        Map<String, Recipient> idRecipientMap = new HashMap<>();
        recipients.forEach(r -> idRecipientMap.put(r.getExternalId(), RecipientMapper.INSTANCE.mapFromEntity(r)));
        return idRecipientMap;
    }

    @Override
    public List<String> getExtIdsByExtIdSet(Set<String> extIdSet) {
        return recipientRepository.getExtIdsByExtIdSet(extIdSet);
    }

    @Override
    public void saveRecipients(List<Recipient> recipients) {
        recipientRepository.saveAll(RecipientMapper.INSTANCE.mapToListFromDtos(recipients));
    }

    @Override
    public void bindRecipientsWithDocument(Document document, DocumentNotificationStatus status) {
        PublicDocumentEntity documentEntity = publicDocumentRepository.getByJobIdOrBlockchainAddress(document.getJobId(), document.getJobId());
        if (documentEntity == null) {
            documentEntity = publicDocumentRepository.getByJobIdOrBlockchainAddress(document.getDocumentBlockchainAddress(), document.getDocumentBlockchainAddress());
        }
        Set<String> extIdSet = document.getNotificationReceivers().stream().map(Recipient::getExternalId).collect(Collectors.toSet());
        List<RecipientEntity> recipients = recipientRepository.findAllByExternalIdIn(extIdSet);
        List<DocumentContactDetailsEntity> bindings = new ArrayList<>();
        for (RecipientEntity recipient : recipients) {
            for (ContactDetailsEntity contactDetails : recipient.getContactDetailsList()) {
                DocumentContactDetailsEntity binding = new DocumentContactDetailsEntity();
                DocumentContactDetailsId key = new DocumentContactDetailsId();
                key.setDocumentId(documentEntity.getId());
                key.setContactDetailsId(contactDetails.getId());
                binding.setKey(key);
                binding.setStatus(status);
                bindings.add(binding);
            }
        }
        documentRecipientRepository.saveAll(bindings);
    }

    @Override
    public void updateRecipients(Map<String, Recipient> updateData, String documentId) {
        if (updateData == null || updateData.isEmpty()) {
            return;
        }
        List<RecipientEntity> entities = recipientRepository.findAllByExternalIdIn(updateData.keySet());
        PublicDocumentEntity document = publicDocumentRepository.getByJobIdOrBlockchainAddress(documentId, documentId);
        List<ContactDetailsEntity> toDelete = new ArrayList<>();
        List<ContactDetailsEntity> toUpdate = new ArrayList<>();
        List<ContactDetailsEntity> toAdd = new ArrayList<>();
        for (RecipientEntity entity : entities) {
            String phoneNumber = updateData.get(entity.getExternalId()).getPhoneNumber();
            List<ContactDetailsEntity> phoneNumberEntities = entity.getContactDetailsEntities(ContactDetailsType.PHONE);
            if (!StringUtils.hasText(phoneNumber)) {
                toDelete.addAll(entity.getContactDetailsEntities(ContactDetailsType.PHONE));
            } else if (!phoneNumberEntities.isEmpty()) {
                phoneNumberEntities.get(0).setValue(phoneNumber);
                toUpdate.add(phoneNumberEntities.get(0));
            } else {
                ContactDetailsEntity contactDetails = new ContactDetailsEntity();
                contactDetails.setValue(phoneNumber);
                contactDetails.setType(ContactDetailsType.PHONE);
                contactDetails.setRecipient(entity);
                toAdd.add(contactDetails);
            }
        }
        recipientRepository.saveAll(entities);
        if (!toDelete.isEmpty()) {
            contactDetailsRepository.deleteAll(toDelete);
        }
        if (!toAdd.isEmpty()) {
            List<ContactDetailsEntity> newContactDetails = contactDetailsRepository.saveAll(toAdd);
            List<DocumentContactDetailsEntity> newBindings = new ArrayList<>();
            for (ContactDetailsEntity contactDetails : newContactDetails) {
                DocumentContactDetailsEntity binding = new DocumentContactDetailsEntity();
                binding.setDocument(document);
                binding.setContactDetails(contactDetails);
                binding.setStatus(DocumentNotificationStatus.NEW);
                newBindings.add(binding);
            }
            documentRecipientRepository.saveAll(newBindings);
        }
        if (!toUpdate.isEmpty()) {
            contactDetailsRepository.saveAll(toUpdate);
            Set<Long> contactDetailsIds = toUpdate.stream().map(ContactDetailsEntity::getId).collect(Collectors.toSet());
            documentRecipientRepository.setUpdatedStatus(contactDetailsIds);
        }
    }

    @Override
    public void setContactDetailsSentStatus(Set<Long> contactDetailsIdSet, String blockchainAddress) {
        documentRecipientRepository.setSentStatus(contactDetailsIdSet, blockchainAddress);
    }

    private Specification<DocumentContactDetailsEntity> combineSpecifications(List<DocumentContactDetailsSpecification> documentContactDetailsSpecifications) {
        Specification<DocumentContactDetailsEntity> specification =
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        for (DocumentContactDetailsSpecification documentContactDetailsSpecification : documentContactDetailsSpecifications) {
            specification = specification.and(documentContactDetailsSpecification);
        }
        return specification;
    }

    private boolean isGetDocumentRecipientsInvalid(String documentId) {
        PublicDocumentEntity document = publicDocumentRepository.getByJobIdOrBlockchainAddress(documentId, documentId);
        return isGetDocumentRecipientsInvalid(document);
    }

    private boolean isGetDocumentRecipientsInvalid(PublicDocumentEntity document) {
        if (document == null) {
            return true;
        }
        if (!document.getPublicationStatus().equals(PublicationStatus.PUBLISHING_OK)) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_PUBLISHED, LocalDateTime.now());
        }
        return false;
    }

}
