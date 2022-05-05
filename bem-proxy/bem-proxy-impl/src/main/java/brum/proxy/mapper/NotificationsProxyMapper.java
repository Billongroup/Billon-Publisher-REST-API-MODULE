package brum.proxy.mapper;

import brum.common.utils.CalendarUtils;
import brum.model.dto.notifications.*;
import brum.model.dto.notifications.NotificationStatus;
import brum.model.dto.notifications.NotificationStatusEnum;
import brum.model.dto.recipients.ContactDetailsType;
import com.zunit.dm.common.module.enums.ListOrder;
import com.zunit.dm.publisher.services.notification.dto.*;

import java.util.ArrayList;
import java.util.List;

public class NotificationsProxyMapper {
    private NotificationsProxyMapper() {}

    public static NotificationStatusFiltersDto map(NotificationStatusFilters in) {
        NotificationStatusFiltersDto out = new NotificationStatusFiltersDto();
        if (in == null) {
            return out;
        }
        if (in.getPagination() != null) {
            out.setPage(in.getPagination().getPage());
            out.setSize(in.getPagination().getLimit());
        }
        if (in.getOrder() != null) {
            out.setListOrder(ListOrder.fromValue(in.getOrder().name()));
        }
        if (in.getClientIdList() != null) {
            out.setClientIdList(in.getClientIdList());
        }
        if (in.getContactDetailsList() != null) {
            out.setContactDetailsList(in.getContactDetailsList());
        }
        if (in.getNotificationStatusList() != null) {
            out.setNotificationStatusList(new ArrayList<>());
            for (NotificationStatusEnum status : in.getNotificationStatusList()) {
                out.getNotificationStatusList().add(com.zunit.dm.publisher.services.notification.dto.NotificationStatus.valueOf(status.name()));
            }
        }
        return out;
    }

    public static List<NotificationStatus> map(List<NotificationDto> in) {
        List<NotificationStatus> out = new ArrayList<>();
        if (in == null) {
            return out;
        }
        for (NotificationDto notification : in) {
            out.add(mapNotificationStatus(notification));
        }
        return out;
    }

    public static NotificationContentFilterDto map(NotificationContentFilter in, String jobId) {
        NotificationContentFilterDto out = new NotificationContentFilterDto();
        if (in == null) {
            return null;
        }
        out.setJobId(jobId);
        out.setClientId(in.getClientId());
        out.setContactDetails(in.getContactDetails());
        return out;
    }

    public static NotificationContent map(NotificationContentDto in) {
        NotificationContent out = new NotificationContent();
        if (in == null) {
            return out;
        }
        out.setSubject(in.getSubject());
        out.setContent(in.getContent());
        return out;
    }

    private static NotificationStatus mapNotificationStatus(NotificationDto in) {
        NotificationStatus out = new NotificationStatus();
        out.setJobId(in.getNotificationJobId());
        out.setClientId(in.getClientId());
        out.setContactDetails(in.getContactDetails());
        out.setType(ContactDetailsType.valueOf(in.getType().name()));
        out.setStatus(NotificationStatusEnum.valueOf(in.getStatus().name()));
        out.setSendingStartDate(CalendarUtils.dateToLocalDateTime(in.getSendingStartDate()));
        out.setDeliveryDate(CalendarUtils.dateToLocalDateTime(in.getDeliveryDate()));
        out.setErrorsHistory(mapErrorsHistory(in.getErrorsHistory()));
        return out;
    }

    private static List<NotificationError> mapErrorsHistory(List<NotificationErrorDto> in) {
        List<NotificationError> out = new ArrayList<>();
        if (in == null) {
            return null;
        }
        for (NotificationErrorDto error : in) {
            NotificationError entry = new NotificationError();
            entry.setReason(error.getReason());
            entry.setSendingStartDate(CalendarUtils.dateToLocalDateTime(error.getSendingStartDate()));
            entry.setErrorDate(CalendarUtils.dateToLocalDateTime(error.getErrorDate()));
            out.add(entry);
        }
        return out;
    }
}
