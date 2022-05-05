package brum.domain.validator;

import brum.model.exception.validation.InvalidField;
import brum.model.exception.validation.ValidationErrorType;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static brum.model.exception.validation.InvalidField.EMAIL;
import static brum.model.exception.validation.InvalidField.PHONE_NUMBER;
import static brum.model.exception.validation.ValidationErrorType.EMPTY;
import static brum.model.exception.validation.ValidationErrorType.INVALID;

public class CommonValidator {
    private static final List<String> ALLOWED_PHONE_COUNTRIES = new ArrayList<>(Arrays.asList(
            "PL", "GB"
    ));

    private CommonValidator() {}

    public static boolean emptyString(String value) {
        return !StringUtils.hasText(value);
    }

    public static boolean validatePesel(String pesel) {
        if (pesel.length() != 11) {
            return true;
        }
        int first = Integer.parseInt(pesel.substring(0, 1));
        int second = Integer.parseInt(pesel.substring(1, 2));
        int third = Integer.parseInt(pesel.substring(2, 3));
        int fourth = Integer.parseInt(pesel.substring(3, 4));
        int fifth = Integer.parseInt(pesel.substring(4, 5));
        int sixth = Integer.parseInt(pesel.substring(5, 6));
        int seventh = Integer.parseInt(pesel.substring(6, 7));
        int eighth = Integer.parseInt(pesel.substring(7, 8));
        int ninth = Integer.parseInt(pesel.substring(8, 9));
        int tenth = Integer.parseInt(pesel.substring(9, 10));
        int eleventh = Integer.parseInt(pesel.substring(10, 11));

        int check = first + (3 * second) + (7 * third) + (9 * fourth) + fifth + (3 * sixth) + (7 * seventh) + (9 * eighth) + ninth + (3 * tenth);
        int lastNumber = check % 10;
        int controlNumber;
        if (lastNumber == 0) {
            controlNumber = 0;
        } else {
            controlNumber = 10 - lastNumber;
        }
        return eleventh != controlNumber;
    }

    public static boolean isPhoneNumberInvalid(String number) {
        if (!number.startsWith("+")) {
            return true;
        }
        for (String countryCode : ALLOWED_PHONE_COUNTRIES) {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, countryCode);
                if (phoneNumberUtil.isValidNumberForRegion(phoneNumber, countryCode)) {
                    return false;
                }
            } catch (NumberParseException ignored) {
                return true;
            }
        }
        return true;
    }

    public static void validatePhoneNumber(String number, Map<InvalidField, ValidationErrorType> errors) {
        if (emptyString(number)) {
            errors.put(PHONE_NUMBER, EMPTY);
        } else if (isPhoneNumberInvalid(number)) {
            errors.put(PHONE_NUMBER, INVALID);
        }
    }

    public static boolean isEmailInvalid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    public static void validateEmail(String email, Map<InvalidField, ValidationErrorType> errors) {
        if (emptyString(email)) {
            errors.put(EMAIL, EMPTY);
        } else if (isEmailInvalid(email)) {
            errors.put(EMAIL, INVALID);
        }
    }

}
