package brum.domain.impl.recipients;

import brum.domain.notifications.GetNotificationsUC;
import brum.domain.recipients.GetDocumentRecipientsUC;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.common.PaginationFilter;
import brum.model.dto.common.Sort;
import brum.model.dto.common.SortOrder;
import brum.model.dto.notifications.NotificationStatus;
import brum.model.dto.notifications.NotificationStatusEnum;
import brum.model.dto.notifications.NotificationStatusFilters;
import brum.model.dto.recipients.*;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.persistence.RecipientPersistenceGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GetDocumentRecipientsUCImpl implements GetDocumentRecipientsUC {

    private final RecipientPersistenceGateway recipientPersistenceGateway;
    private final GetNotificationsUC getNotificationsUC;

    public GetDocumentRecipientsUCImpl(RecipientPersistenceGateway recipientPersistenceGateway,
                                       GetNotificationsUC getNotificationsUC) {
        this.recipientPersistenceGateway = recipientPersistenceGateway;
        this.getNotificationsUC = getNotificationsUC;
    }

    @Override
    public PaginatedResponse<DocumentRecipient> getDocumentRecipients(String blockchainAddress, DocumentRecipientSearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            searchCriteria = new DocumentRecipientSearchCriteria();
        }
        if (searchCriteria.getFilters() == null) {
            searchCriteria.setFilters(new DocumentRecipientFilters());
        }
        PaginatedResponse<DocumentRecipient> result = new PaginatedResponse<>();
        result.setRows(new ArrayList<>());
        result.setCount(0);
        List<RecipientGroup> groups = searchCriteria.getFilters().getGroups() == null ? new ArrayList<>() : searchCriteria.getFilters().getGroups();
        if (groups.isEmpty() || groups.contains(RecipientGroup.NEW)) {
            PaginatedResponse<DocumentRecipient> newAndUpdated = getNewAndUpdated(blockchainAddress, searchCriteria);
            result.getRows().addAll(newAndUpdated.getRows());
            result.setCount(result.getCount() + newAndUpdated.getCount());
        }
        if (groups.isEmpty() || groups.contains(RecipientGroup.ERROR)) {
            Integer limit = setLimit(searchCriteria.getPagination(), result.getCount());
            Integer offset = setOffset(searchCriteria.getPagination(), result.getCount());
            PaginatedResponse<DocumentRecipient> sent = getSentNotifications(blockchainAddress, searchCriteria, limit, offset);
            result.getRows().addAll(sent.getRows());
            result.setCount(result.getCount() + sent.getCount());
        }
        return result;
    }

    private PaginatedResponse<DocumentRecipient> getNewAndUpdated(String blockchainAddress, DocumentRecipientSearchCriteria searchCriteria) {
        List<RecipientGroup> originalFilter = searchCriteria.getFilters().getGroups();
        searchCriteria.getFilters().setGroups(Collections.singletonList(RecipientGroup.NEW));
        PaginatedResponse<DocumentRecipient> newAndUpdated = recipientPersistenceGateway.getDocumentRecipients(blockchainAddress, searchCriteria);
        if (newAndUpdated == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        searchCriteria.getFilters().setGroups(originalFilter);
        return newAndUpdated;
    }

    private Integer setLimit(PaginationFilter pagination, long found) {
        if (pagination == null || pagination.getLimit() == null) {
            return null;
        }
        Integer pageSize = pagination.getLimit();
        int page = pagination.getPage() == null ? 0 : pagination.getPage();
        if ((long) pageSize * (page + 1) < found) {
            return 0;
        }
        return pageSize - ((int) found % pageSize);
    }

    private Integer setOffset(PaginationFilter pagination, long found) {
        if (pagination == null || pagination.getLimit() == null) {
            return null;
        }
        Integer pageSize = pagination.getLimit();
        int page = pagination.getPage() == null ? 0 : pagination.getPage();
        if ((long) pageSize * page < found) {
            return 0;
        }
        return (int) found - (pageSize * page);
    }

    private PaginatedResponse<DocumentRecipient> getSentNotifications(String blockchainAddress, DocumentRecipientSearchCriteria searchCriteria, Integer limit, Integer offset) {
        DocumentRecipientSearchCriteria filtersForSent = new DocumentRecipientSearchCriteria();
        filtersForSent.setSort(new Sort());
        filtersForSent.getSort().setOrder(searchCriteria.getSort() == null ? SortOrder.ASCENDING : searchCriteria.getSort().getOrder());
        DocumentRecipientFilters filters = new DocumentRecipientFilters();
        filters.setRecipientId(searchCriteria.getFilters().getRecipientId());
        filters.setContactDetails(searchCriteria.getFilters().getContactDetails());
        filters.setGroups(Collections.singletonList(RecipientGroup.ERROR));
        filtersForSent.setFilters(filters);
        PaginatedResponse<DocumentRecipient> sent = recipientPersistenceGateway.getDocumentRecipients(blockchainAddress, filtersForSent);
        if (sent == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (sent.getCount() == 0) {
            return sent;
        }
        Set<String> extIds = new HashSet<>();
        Set<String> contactDetails = new HashSet<>();
        for (DocumentRecipient recipient : sent.getRows()) {
            extIds.add(recipient.getExternalId());
            contactDetails.add(recipient.getContactDetails());
        }
        NotificationStatusFilters statusFilters = new NotificationStatusFilters();
        statusFilters.setClientIdList(new ArrayList<>(extIds));
        statusFilters.setContactDetailsList(new ArrayList<>(contactDetails));
        statusFilters.setOrder(SortOrder.ASCENDING);
        List<NotificationStatus> notificationStatuses = getNotificationsUC.getDocumentNotificationStatus(blockchainAddress, statusFilters).getRows();
        Map<String, Map<String, NotificationStatusEnum>> contactDetailsStatusMap = new HashMap<>();
        for (NotificationStatus status : notificationStatuses) {
            if (!contactDetailsStatusMap.containsKey(status.getClientId())) {
                contactDetailsStatusMap.put(status.getClientId(), new HashMap<>());
            }
            contactDetailsStatusMap.get(status.getClientId()).put(status.getContactDetails(), status.getStatus());
        }
        if (searchCriteria.getFilters().getGroups() != null && searchCriteria.getFilters().getGroups().contains(RecipientGroup.ERROR)) {
            return getErrorRecipients(sent.getRows(), contactDetailsStatusMap, limit, offset);
        }
        return getAllRecipients(sent, contactDetailsStatusMap, limit, offset);
    }

    private PaginatedResponse<DocumentRecipient> getErrorRecipients(List<DocumentRecipient> rows,
                                                                    Map<String, Map<String, NotificationStatusEnum>> recipientStatusMap,
                                                                    Integer limit, Integer offset) {
        List<DocumentRecipient> toPaginate = new ArrayList<>();
        for (DocumentRecipient recipient : rows) {
            if (recipientStatusMap.get(recipient.getExternalId()).get(recipient.getContactDetails()).equals(NotificationStatusEnum.ERROR)) {
                recipient.setStatus(DocumentRecipientStatus.ERROR);
                toPaginate.add(recipient);
            }
        }
        List<DocumentRecipient> resultList = new ArrayList<>();
        if (limit == null) {
            resultList = toPaginate;
        } else if (limit > 0) {
            if (offset > toPaginate.size()) {
                resultList = new ArrayList<>();
            } else if (offset + limit > toPaginate.size()) {
                resultList = toPaginate.subList(offset, toPaginate.size());
            } else {
                resultList = toPaginate.subList(offset, offset + limit);
            }
        }
        PaginatedResponse<DocumentRecipient> result = new PaginatedResponse<>();
        result.setRows(resultList);
        result.setCount(toPaginate.size());
        return result;
    }

    private PaginatedResponse<DocumentRecipient> getAllRecipients(PaginatedResponse<DocumentRecipient> rows,
                                                                    Map<String, Map<String, NotificationStatusEnum>> recipientStatusMap,
                                                                    Integer limit, Integer offset) {
        List<DocumentRecipient> toReturn = new ArrayList<>();
        if (limit == null) {
            toReturn = rows.getRows();
        } else if (limit > 0) {
            if (offset > rows.getRows().size()) {
                toReturn = new ArrayList<>();
            } else if (offset + limit > rows.getRows().size()) {
                toReturn = rows.getRows().subList(offset, rows.getRows().size());
            } else {
                toReturn = rows.getRows().subList(offset, offset + limit);
            }
        }
        for (DocumentRecipient recipient : toReturn) {
            recipient.setStatus(DocumentRecipientStatus.valueOf(
                    recipientStatusMap.get(recipient.getExternalId()).get(recipient.getContactDetails()).name()));
        }
        PaginatedResponse<DocumentRecipient> result = new PaginatedResponse<>();
        result.setRows(toReturn);
        result.setCount(rows.getCount());
        return result;
    }


}
