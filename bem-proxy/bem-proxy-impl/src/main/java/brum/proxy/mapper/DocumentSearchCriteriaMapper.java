package brum.proxy.mapper;

import brum.common.utils.CalendarUtils;
import brum.model.dto.common.PaginationFilter;
import brum.model.dto.documents.DocumentFilters;
import brum.model.dto.documents.DocumentSearchCriteria;
import brum.model.dto.documents.PublicationStatus;
import brum.model.dto.documents.SortDocuments;
import brum.model.dto.identities.IdentityFilters;
import com.zunit.dm.common.module.document.DocumentFilterDocumentDto;
import com.zunit.dm.common.module.dto.ContactDetailsDto;
import com.zunit.dm.common.module.dto.IdentityDocumentExtendedDto;
import com.zunit.dm.common.module.dto.IdentityDto;
import com.zunit.dm.common.module.enums.ListOrder;
import com.zunit.dm.common.module.enums.SortBy;
import https.types_dm_billongroup.DocumentPublicationStatus;
import https.types_dm_billongroup.DocumentStatus;
import https.types_dm_billongroup.DocumentType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentSearchCriteriaMapper {
    private DocumentSearchCriteriaMapper () {}
    public static DocumentFilterDocumentDto mapSearchCriteria(DocumentSearchCriteria searchCriteria, String categoryPath) {
        DocumentFilterDocumentDto result = new DocumentFilterDocumentDto();
        setFundamentals(result, searchCriteria.getFilters());
        result.setCategoryId(categoryPath);
        if (searchCriteria.getPagination() != null) {
            setPagination(result, searchCriteria.getPagination());
        }
        setSortBy(result, searchCriteria.getSort());
        return result;
    }

    private static void setPagination(DocumentFilterDocumentDto result, PaginationFilter pagination) {
        result.setOffset(pagination.getPage());
        result.setMaxPageSize(pagination.getLimit());
    }

    private static void setFundamentals(DocumentFilterDocumentDto result, DocumentFilters filters) {
        result.setDocumentType(DocumentType.valueOf(filters.getDocumentType().name()));
        result.setDocumentBlockchainAddress(filters.getBlockchainAddress());
        result.setTitle(filters.getTitle());
        result.setSignatoryId(filters.getSignatoryId());
        result.setPublishingPersonId(filters.getPublishedBy());
        result.setFavourite(filters.getFavourite());
        result.setReaded(filters.getRead());
        result.setPublishedBy(filters.getSenderPublisherId());
        result.setLcReferenceNumber(filters.getLcReferenceNumber());
        result.setLcStatus(filters.getLcStatus());
        result.setComponentPassportId(filters.getComponentPassportId());
        result.setDraft(filters.getDraft());
        if (filters.getCreationDateRange() != null) {
            result.setCreationDateFrom(CalendarUtils.toGregorianCalendar(filters.getCreationDateRange().getFrom()));
            result.setCreationDateTo(CalendarUtils.toGregorianCalendar(filters.getCreationDateRange().getTo()));
        }
        if (filters.getPublicationDateRange() != null) {
            result.setPublicationDateFrom(CalendarUtils.toGregorianCalendar(filters.getPublicationDateRange().getFrom()));
            result.setPublicationDateTo(CalendarUtils.toGregorianCalendar(filters.getPublicationDateRange().getTo()));
        }
        if (filters.getValidSinceDateRange() != null) {
            result.setLegalValidityStartDateFrom(CalendarUtils.toGregorianCalendar(filters.getValidSinceDateRange().getFrom()));
            result.setLegalValidityStartDateTo(CalendarUtils.toGregorianCalendar(filters.getValidSinceDateRange().getTo()));
        }
        if (filters.getValidUntilDateRange() != null) {
            result.setLegalValidityFinishDateFrom(CalendarUtils.toGregorianCalendar(filters.getValidUntilDateRange().getFrom()));
            result.setLegalValidityFinishDateTo(CalendarUtils.toGregorianCalendar(filters.getValidUntilDateRange().getTo()));
        }
        if (filters.getRetainUntilDateRange() != null) {
            result.setRetentionDateFrom(CalendarUtils.toGregorianCalendar(filters.getRetainUntilDateRange().getFrom()));
            result.setRetentionDateTo(CalendarUtils.toGregorianCalendar(filters.getRetainUntilDateRange().getTo()));
        }
        if (filters.getPublicationStatusList() != null && !filters.getPublicationStatusList().isEmpty()) {
            result.setDocumentPublicationStatusList(mapPublicationStatusList(filters.getPublicationStatusList()));
        }
        if (filters.getDocumentStatusList() != null && !filters.getDocumentStatusList().isEmpty()) {
            result.setDocumentStatusList(mapDocumentStatusList(filters.getDocumentStatusList()));
        }
        if (filters.getIdentity() != null) {
            result.setIdentity(mapIdentityFilters(filters.getIdentity()));
        }
    }

    private static void setSortBy(DocumentFilterDocumentDto result, SortDocuments sort) {
        if (sort == null) {
            sort = new SortDocuments();
        }
        if (sort.getSortBy() != null) {
            result.setSortBy(SortBy.fromName(sort.getSortBy().name()));
        } else {
            result.setSortBy(SortBy.CREATION_DATE);
        }
        if (sort.getOrder() != null) {
            result.setListOrder(ListOrder.fromValue(sort.getOrder().name()));
        } else {
            result.setListOrder(ListOrder.DESCENDING);
        }
    }

    private static List<DocumentPublicationStatus> mapPublicationStatusList(List<PublicationStatus> publicationStatusList) {
        List<DocumentPublicationStatus> result = new ArrayList<>();
        for (PublicationStatus status : publicationStatusList) {
            result.add(DocumentPublicationStatus.valueOf(status.name()));
        }
        return result;
    }

    private static List<DocumentStatus> mapDocumentStatusList(List<brum.model.dto.documents.DocumentStatus> documentStatusList) {
        List<DocumentStatus> result = new ArrayList<>();
        for (brum.model.dto.documents.DocumentStatus status : documentStatusList) {
            result.add(DocumentStatus.valueOf(status.name()));
        }
        return result;
    }

    private static IdentityDto mapIdentityFilters(IdentityFilters identity) {
        IdentityDto result = new IdentityDto();
        IdentityDocumentExtendedDto documentFilters = new IdentityDocumentExtendedDto();
        documentFilters.setDocumentNumber(identity.getDocumentNumber());
        documentFilters.setForename(identity.getFirstName());
        documentFilters.setSurname(identity.getLastName());
        result.setIdDocumentList(Collections.singletonList(documentFilters));

        ContactDetailsDto contactDetailsFilters = new ContactDetailsDto();
        contactDetailsFilters.setEmailAddress(identity.getEmail());
        contactDetailsFilters.setPhoneNumber(identity.getPhoneNumber());
        result.setContactDetailsList(Collections.singletonList(contactDetailsFilters));

        return result;
    }
}
