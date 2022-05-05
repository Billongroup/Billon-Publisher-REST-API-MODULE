package brum.domain.impl.users;

import brum.domain.users.NotifyUserUC;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.users.User;
import brum.persistence.ParameterPersistenceGateway;
import brum.remote.model.NotificationDetails;
import brum.remote.model.NotificationReceiver;
import brum.remote.model.ReceiverType;
import brum.remote.service.NotificationService;
import brum.security.UserSecurityService;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class NotifyUserUCImpl implements NotifyUserUC {
    private static final String URL_TAG = "<<LINK>>";
    private static final String SMS_CODE_TAG = "<<CODE>>";

    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private final NotificationService notificationService;
    private final UserSecurityService userSecurityService;

    public NotifyUserUCImpl(ParameterPersistenceGateway parameterPersistenceGateway, NotificationService notificationService, UserSecurityService userSecurityService) {
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        this.notificationService = notificationService;
        this.userSecurityService = userSecurityService;
    }

    @Override
    public void sendSetPasswordNotification(User user) {
        NotificationReceiver notificationReceiver = NotificationReceiver.builder()
                .type(ReceiverType.MAIL)
                .contactData(user.getEmail()).build();
        String jobId = notificationService.prepareNotification(Collections.singletonList(notificationReceiver));

        String panelUrl = parameterPersistenceGateway.getParameterValue(ParameterKey.FRONT_END_URL);
        String prefix = parameterPersistenceGateway.getParameterValue(ParameterKey.SET_PASSWORD_URL_PREFIX);
        String jwt = userSecurityService.getTokenForPatchPassword(user.getUsername());
        String link = panelUrl + prefix + "/" + jwt;
        String title = parameterPersistenceGateway.getParameterValue(ParameterKey.SET_PASSWORD_EMAIL_TITLE);
        String emailContent = parameterPersistenceGateway.getParameterValue(ParameterKey.SET_PASSWORD_EMAIL_CONTENT);
        emailContent = StringUtils.replace(emailContent, URL_TAG, link);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .jobId(jobId)
                .subject(title)
                .emailContent(emailContent).build();
        notificationService.sendNotification(notificationDetails);
    }

    @Override
    public void sendResetPasswordNotification(User user) {
        NotificationReceiver notificationReceiver = NotificationReceiver.builder()
                .type(ReceiverType.MAIL)
                .contactData(user.getEmail()).build();
        String jobId = notificationService.prepareNotification(Collections.singletonList(notificationReceiver));

        String panelUrl = parameterPersistenceGateway.getParameterValue(ParameterKey.FRONT_END_URL);
        String prefix = parameterPersistenceGateway.getParameterValue(ParameterKey.RESET_PASSWORD_URL_PREFIX);
        String jwt = userSecurityService.getTokenForPatchPassword(user.getUsername());
        String link = panelUrl + prefix + "/" + jwt;
        String title = parameterPersistenceGateway.getParameterValue(ParameterKey.RESET_PASSWORD_NOTIFICATION_TITLE);
        String emailContent = parameterPersistenceGateway.getParameterValue(ParameterKey.RESET_PASSWORD_EMAIL_CONTENT);
        emailContent = StringUtils.replace(emailContent, URL_TAG, link);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .jobId(jobId)
                .subject(title)
                .emailContent(emailContent).build();
        notificationService.sendNotification(notificationDetails);
    }

    @Override
    public void sendPasswordExpiredNotification(User user) {
        NotificationReceiver notificationReceiver = NotificationReceiver.builder()
                .type(ReceiverType.MAIL)
                .contactData(user.getEmail()).build();
        String jobId = notificationService.prepareNotification(Collections.singletonList(notificationReceiver));

        String title = parameterPersistenceGateway.getParameterValue(ParameterKey.PASSWORD_EXPIRED_TITLE);
        String emailContent = parameterPersistenceGateway.getParameterValue(ParameterKey.PASSWORD_EXPIRED_EMAIL_CONTENT);

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .jobId(jobId)
                .subject(title)
                .emailContent(emailContent).build();
        notificationService.sendNotification(notificationDetails);
    }

    @Override
    public void sendSmsCodeNotification(User user) {
        NotificationReceiver notificationReceiver = NotificationReceiver.builder()
                .type(ReceiverType.SMS)
                .contactData(user.getPhoneNumber()).build();
        String jobId = notificationService.prepareNotification(Collections.singletonList(notificationReceiver));

        String smsContent = parameterPersistenceGateway.getParameterValue(ParameterKey.SMS_CODE_CONTENT);
        smsContent = StringUtils.replace(smsContent, SMS_CODE_TAG, user.getSmsCode());

        NotificationDetails notificationDetails = NotificationDetails.builder()
                .jobId(jobId)
                .smsContent(smsContent).build();
        notificationService.sendNotification(notificationDetails);
    }
}
