package com.cg.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;


@Component
public class ValidateUtils {

    public static final String NUMBER_REGEX = "\\d+";
    public static final String LETTER_WITHOUT_NUMBER_REGEX = "([A-Z]+[a-z]*[ ]?)+$";
    public static final String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,9}$";
    public static final String PASSWORD_COMPLEX_REGEX = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]+)(?!.*['\"`])(?=^.{6,}$).*$";
    public static final String DATE_REGEX = "[0-9]{4}-([0-9]|0[0-9]|1[0-2])-([0-9]|[0-2][0-9]|3[0-1])$";

    public boolean isNumberValid(String number) {
        return Pattern.compile(NUMBER_REGEX).matcher(number).matches();
    }

    public boolean isLetterWithoutNumberValid(String name) {
        return Pattern.compile(LETTER_WITHOUT_NUMBER_REGEX).matcher(name).matches();
    }

    public boolean isEmailValid(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public boolean isPasswordValid(String pwd) {
        return Pattern.compile(PASSWORD_COMPLEX_REGEX).matcher(pwd).matches();
    }

    public boolean isDateValid(String dateStr) {
        return Pattern.compile(DATE_REGEX).matcher(dateStr).matches();
    }

}