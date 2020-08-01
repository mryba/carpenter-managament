package com.carpenter.utils;

public class ConstantsRegex {

    private ConstantsRegex() {
    }

    public static final String MSISDN_PATTERN = "(?:\\+\\d{11,})?";
    public static final String EMAIL_PATTERN = "(?:[^@]+@[^@]+\\.[a-z]{2,})?";
    public static final String POSTAL_CODE_PATTERN = "(\\d{2}\\-\\d{3})?";
    public static final String RAW_PHONE_PATTERN = "\\d{9,}";
    public static final String RAW_EMAIL_PATTERN = "[^@]+@[^@]+\\.[a-z]{2,}";
    public static final String INVOICE_NUMBER_PATTERN = "(\\d\\/\\d{4})?";
    public static final String FETCH_GRAPH = "javax.persistence.fetchgraph";
}
