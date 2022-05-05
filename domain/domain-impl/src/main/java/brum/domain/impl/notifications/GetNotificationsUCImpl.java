package brum.domain.impl.notifications;

import brum.domain.notifications.GetNotificationsUC;
import brum.model.dto.common.BemResponse;
import brum.model.dto.common.PaginatedResponse;
import brum.model.dto.notifications.*;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.proxy.DocumentProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class GetNotificationsUCImpl implements GetNotificationsUC {

    private final DocumentProxy documentProxy;

    public GetNotificationsUCImpl(DocumentProxy documentProxy) {
        this.documentProxy = documentProxy;
    }

    @Override
    public PaginatedResponse<NotificationStatus> getDocumentNotificationHistory(String blockchainAddress, NotificationHistorySearchCriteria searchCriteria) {
        if (searchCriteria == null) {
            return getDocumentNotificationStatus(blockchainAddress, null);
        }
        NotificationStatusFilters statusFilters = new NotificationStatusFilters();
        statusFilters.setPagination(searchCriteria.getPagination());
        statusFilters.setOrder(searchCriteria.getSort() == null ? null : searchCriteria.getSort().getOrder());
        NotificationHistoryFilters filters = searchCriteria.getFilters() == null ? new NotificationHistoryFilters() : searchCriteria.getFilters();
        if (StringUtils.hasText(filters.getClientId())) {
            statusFilters.setClientIdList(Collections.singletonList(filters.getClientId()));
        }
        if (StringUtils.hasText(filters.getContactDetails())) {
            statusFilters.setContactDetailsList(Collections.singletonList(filters.getContactDetails()));
        }
        statusFilters.setNotificationStatusList(filters.getNotificationStatusList());
        return getDocumentNotificationStatus(blockchainAddress, statusFilters);
    }

    @Override
    public PaginatedResponse<NotificationStatus> getDocumentNotificationStatus(String blockchainAddress, NotificationStatusFilters filters) {
        BemResponse<PaginatedResponse<NotificationStatus>> bemResponse = documentProxy.getDocumentNotificationsStatus(blockchainAddress, filters);
        if (!bemResponse.isSuccess()) {
            throw bemResponse.getException();
        }
        return bemResponse.getResponse();
    }

    @Override
    public NotificationContent getNotificationContent(String jobId, NotificationContentFilter filter) {
        BemResponse<NotificationContent> bemResponse = documentProxy.getNotificationContent(jobId, filter);
        if (!bemResponse.isSuccess()) {
            throw bemResponse.getException();
        }
        NotificationContent response = bemResponse.getResponse();
        if (!StringUtils.hasText(response.getSubject()) && !StringUtils.hasText(response.getContent())) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        return bemResponse.getResponse();
    }
}
