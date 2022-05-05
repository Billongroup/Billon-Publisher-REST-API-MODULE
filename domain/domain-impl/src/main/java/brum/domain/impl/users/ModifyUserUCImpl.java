package brum.domain.impl.users;

import brum.common.enums.security.RoleEnum;
import brum.domain.users.ModifyUserUC;
import brum.domain.users.NotifyUserUC;
import brum.domain.validator.CommonValidator;
import brum.model.dto.common.ParameterKey;
import brum.model.dto.users.NotificationData;
import brum.model.dto.users.NotificationType;
import brum.model.dto.users.User;
import brum.model.dto.users.UserStatus;
import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import brum.model.exception.UniqueConstraintException;
import brum.model.exception.validation.*;
import brum.persistence.ParameterPersistenceGateway;
import brum.persistence.UserPersistenceGateway;
import brum.security.UserSecurityService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ModifyUserUCImpl implements ModifyUserUC {

    private static final List<String> PASSWORD_GROUPS_REGEX = Arrays.asList(
            "(.*[a-z].*)", // any lower case
            "(.*[A-Z].*)", // any upper case
            "(.*\\d.*)", // any numeric
            "(.*\\W.*)" // any special
    );

    private final UserPersistenceGateway userPersistenceGateway;
    private final UserSecurityService userSecurityService;
    private final ParameterPersistenceGateway parameterPersistenceGateway;
    private final NotifyUserUC notifyUserUC;

    public ModifyUserUCImpl(UserPersistenceGateway userPersistenceGateway, UserSecurityService userSecurityService,
                            ParameterPersistenceGateway parameterPersistenceGateway, NotifyUserUC notifyUserUC) {
        this.userPersistenceGateway = userPersistenceGateway;
        this.userSecurityService = userSecurityService;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
        this.notifyUserUC = notifyUserUC;
    }

    @Override
    public void modifyUser(String userId, User user, String issuerUsername) {
        if (user == null) {
            throw new ValidationException(InvalidField.USER, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        User userFromDB = userPersistenceGateway.getUserByExternalId(userId, false);
        if (userFromDB == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (isNewDataIdentical(userFromDB, user)) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_UNIQUE, LocalDateTime.now());
        }
        Map<InvalidField, ValidationErrorType> errors = mapModifyUser(userFromDB, user);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors, LocalDateTime.now());
        }
        try {
            userPersistenceGateway.saveUser(userFromDB);
        } catch (UniqueConstraintException e) {
            throw new ValidationException(InvalidField.valueOf(e.getConstraint().name()), ValidationErrorType.NON_UNIQUE, LocalDateTime.now());
        }
        userPersistenceGateway.addHistoryEntry(userFromDB, issuerUsername);
    }

    @Override
    public void modifyPassword(User passwordData, String username) {
        if (!StringUtils.hasText(passwordData.getPassword())) {
            throw new PasswordValidationException(PasswordValidationErrorType.PASSWORD_EMPTY, LocalDateTime.now());
        }
        User user = userPersistenceGateway.getUserByUsername(username);
        validateSmsCode(passwordData.getSmsCode(), user);
        validatePassword(passwordData.getPassword(), user);

        user.setPasswordUpdatedAt(LocalDateTime.now());
        if (user.getStatus().equals(UserStatus.PASSWORD_EXPIRED) || user.getStatus().equals(UserStatus.PENDING)) {
            user.setStatus(UserStatus.REGISTERED);
            userPersistenceGateway.addHistoryEntry(user, username);
        }
        user.setPassword(userSecurityService.encryptPassword(passwordData.getPassword()));
        user.setLastNotificationAt(null);
        user.setSmsCode(null);
        user.setSmsCodeGenerationTime(null);
        userPersistenceGateway.saveUser(user);
        userPersistenceGateway.addPasswordHistoryEntry(user);
    }

    @Override
    public void sendNotification(NotificationData notification, String issuerUsername) {
        if (notification == null) {
            throw new ValidationException(InvalidField.NOTIFICATION, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        if (notification.getType() == null) {
            throw new ValidationException(InvalidField.NOTIFICATION_TYPE, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        if (notification.getType() == NotificationType.SET_PASSWORD) {
            sendSetPasswordNotification(notification.getUserId(), issuerUsername);
        } else if (notification.getType() == NotificationType.PASSWORD_EXPIRED) {
            requirePasswordChange(notification.getUserId(), issuerUsername);
        }
    }

    @Override
    public void requirePasswordChange(String id, String issuerUsername) {
        User user = userPersistenceGateway.getUserByExternalId(id, false);
        if (user == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (!user.getStatus().equals(UserStatus.REGISTERED)) {
            throw new BRUMGeneralException(ErrorStatusCode.USER_NOT_REGISTERED, LocalDateTime.now());
        }
        user.setStatus(UserStatus.PASSWORD_EXPIRED);
        userPersistenceGateway.saveUser(user);
        userPersistenceGateway.addHistoryEntry(user, issuerUsername);
        notifyUserUC.sendPasswordExpiredNotification(user);
    }

    @Override
    public void sendResetPasswordNotification(String email) {
        if (!StringUtils.hasText(email)) {
            throw new ValidationException(InvalidField.EMAIL, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        User userFromDB = userPersistenceGateway.getUserByEmail(email);
        if (userFromDB == null || !Arrays.asList(UserStatus.REGISTERED, UserStatus.PASSWORD_EXPIRED).contains(userFromDB.getStatus())) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        notifyUserUC.sendResetPasswordNotification(userFromDB);
    }

    private boolean isNewDataIdentical(User userFromDb, User newUserData) {
        if (newUserData.getRole() != null && !newUserData.getRole().equals(userFromDb.getRole())) {
            return false;
        }
        if (StringUtils.hasText(newUserData.getFirstName()) && !newUserData.getFirstName().equals(userFromDb.getFirstName())) {
            return false;
        }
        if (StringUtils.hasText(newUserData.getLastName()) && !newUserData.getLastName().equals(userFromDb.getLastName())) {
            return false;
        }
        if (StringUtils.hasText(newUserData.getPhoneNumber()) && !newUserData.getPhoneNumber().equals(userFromDb.getPhoneNumber())) {
            return false;
        }
        if (StringUtils.hasText(newUserData.getEmail()) && !newUserData.getEmail().equals(userFromDb.getEmail())) {
            return false;
        }
        if (StringUtils.hasText(newUserData.getDepartment()) && !newUserData.getDepartment().equals(userFromDb.getDepartment())) {
            return false;
        }
        return newUserData.getIsActive() == null || newUserData.getIsActive().equals(userFromDb.getIsActive());
    }

    private Map<InvalidField, ValidationErrorType> mapModifyUser(User userFromDb, User newUserData) {
        Map<InvalidField, ValidationErrorType> errors = new EnumMap<>(InvalidField.class);
        if (newUserData.getRole() != null) {
            userFromDb.setRole(newUserData.getRole());
        }
        if (StringUtils.hasText(newUserData.getFirstName())) {
            userFromDb.setFirstName(newUserData.getFirstName());
        }
        if (StringUtils.hasText(newUserData.getLastName())) {
            userFromDb.setLastName(newUserData.getLastName());
        }
        if (StringUtils.hasText(newUserData.getPhoneNumber())) {
            if (CommonValidator.isPhoneNumberInvalid(newUserData.getPhoneNumber())) {
                errors.put(InvalidField.PHONE_NUMBER, ValidationErrorType.INVALID);
            }
            userFromDb.setPhoneNumber(newUserData.getPhoneNumber());
        }
        if (StringUtils.hasText(newUserData.getEmail())) {
            if (CommonValidator.isEmailInvalid(newUserData.getEmail())) {
                errors.put(InvalidField.EMAIL, ValidationErrorType.INVALID);
            }
            userFromDb.setEmail(newUserData.getEmail());
        }
        if (StringUtils.hasText(newUserData.getDepartment())) {
            userFromDb.setDepartment(newUserData.getDepartment());
        }
        if (newUserData.getIsActive() != null) {
            userFromDb.setIsActive(newUserData.getIsActive());
        }
        return errors;
    }

    private void sendSetPasswordNotification(String id, String issuerUsername) {
        User user = userPersistenceGateway.getUserByExternalId(id, false);
        if (user == null) {
            throw new BRUMGeneralException(ErrorStatusCode.NOT_FOUND, LocalDateTime.now());
        }
        if (!Arrays.asList(UserStatus.SHELL, UserStatus.PENDING).contains(user.getStatus())) {
            throw new BRUMGeneralException(ErrorStatusCode.ALREADY_REGISTERED, LocalDateTime.now());
        }
        notifyUserUC.sendSetPasswordNotification(user);
        user.setLastNotificationAt(LocalDateTime.now());
        if (user.getStatus().equals(UserStatus.SHELL)) {
            user.setStatus(UserStatus.PENDING);
            userPersistenceGateway.addHistoryEntry(user, issuerUsername);
        }
        userPersistenceGateway.saveUser(user);
    }

    private void validatePassword(String password, User user) {
        Long hoursToChangePassword = parameterPersistenceGateway.getParameterValue(ParameterKey.HOURS_TO_CHANGE_PASSWORD);
        if (user.getStatus().equals(UserStatus.REGISTERED) && LocalDateTime.now().minusHours(hoursToChangePassword).isBefore(user.getPasswordUpdatedAt())) {
            throw new PasswordValidationException(PasswordValidationErrorType.PASSWORD_CHANGED_TOO_OFTEN, LocalDateTime.now());
        }
        checkUniqueness(user, password);
        List<PasswordValidationErrorType> errors = new ArrayList<>();
        checkLength(user, password, errors);
        checkComplexity(user, password, errors);
        if (!errors.isEmpty()) {
            throw new PasswordValidationException(errors, LocalDateTime.now());
        }
    }

    private int checkRegex(String password) {
        int counter = 0;
        for (String regex : PASSWORD_GROUPS_REGEX) {
            if (password.matches(regex)) {
                counter++;
            }
        }
        return counter;
    }

    private void validateSmsCode(String smsCode, User user) {
        if (!StringUtils.hasText(smsCode)) {
            throw new ValidationException(InvalidField.CODE, ValidationErrorType.EMPTY, LocalDateTime.now());
        }
        if (!StringUtils.hasText(user.getSmsCode())) {
            throw new BRUMGeneralException(ErrorStatusCode.CODE_NOT_GENERATED, LocalDateTime.now());
        }
        Long smsCodeExpirationMinutes = parameterPersistenceGateway.getParameterValue(ParameterKey.SMS_CODE_EXPIRATION_TIME);
        if (LocalDateTime.now().minusMinutes(smsCodeExpirationMinutes).isAfter(user.getSmsCodeGenerationTime())) {
            throw new ValidationException(InvalidField.CODE, ValidationErrorType.EXPIRED, LocalDateTime.now());
        }
        if (!user.getSmsCode().equals(smsCode)) {
            throw new ValidationException(InvalidField.CODE, ValidationErrorType.INVALID, LocalDateTime.now());
        }
    }

    private void checkUniqueness(User user, String password) {
        long uniquePasswords;
        if (user.getRole().equals(RoleEnum.ADMIN)) {
            uniquePasswords = parameterPersistenceGateway.getParameterValue(ParameterKey.ADMIN_UNIQUE_PASSWORDS);
        } else {
            uniquePasswords = parameterPersistenceGateway.getParameterValue(ParameterKey.USER_UNIQUE_PASSWORDS);
        }
        int counter = 1;
        for (String historicPassword : user.getPasswordHistory()) {
            if (counter >= uniquePasswords) {
                break;
            }
            if (userSecurityService.passwordMatches(password, historicPassword)) {
                throw new PasswordValidationException(PasswordValidationErrorType.PASSWORD_PREVIOUSLY_USED, LocalDateTime.now());
            }
            counter++;
        }
    }

    private void checkLength(User user, String password, List<PasswordValidationErrorType> errors) {
        long minLength;
        if (user.getRole().equals(RoleEnum.ADMIN)) {
            minLength = parameterPersistenceGateway.getParameterValue(ParameterKey.ADMIN_PASSWORD_LENGTH);
        } else {
            minLength = parameterPersistenceGateway.getParameterValue(ParameterKey.USER_PASSWORD_LENGTH);
        }
        if (password.length() < minLength) {
            errors.add(PasswordValidationErrorType.PASSWORD_TOO_SHORT);
        }
    }

    private void checkComplexity(User user, String password, List<PasswordValidationErrorType> errors) {
        int regexMatches = checkRegex(password);
        long requiredMatches;
        if (user.getRole().equals(RoleEnum.ADMIN)) {
            requiredMatches = parameterPersistenceGateway.getParameterValue(ParameterKey.ADMIN_PASSWORD_COMPLEXITY_GROUPS);
        } else {
            requiredMatches = parameterPersistenceGateway.getParameterValue(ParameterKey.USER_PASSWORD_COMPLEXITY_GROUPS);
        }
        if (regexMatches < requiredMatches) {
            errors.add(PasswordValidationErrorType.PASSWORD_NO_UNIQUE_CHARACTERS);
        }
    }
}
