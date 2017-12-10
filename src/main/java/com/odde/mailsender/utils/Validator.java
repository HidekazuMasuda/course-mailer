package com.odde.mailsender.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private Validator() {
    }

    public static boolean isMailAddress(final String mailAddress) {
        if (mailAddress == null || "".equals(mailAddress)) {
            return false;
        }
        String mailPattern = "^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(mailPattern);
        Matcher matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    public static boolean isNotEmpty(final String target) {
        if (target == null || "".equals(target)) {
            return false;
        }
        return true;
    }

}

