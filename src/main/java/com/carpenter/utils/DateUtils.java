package com.carpenter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static Date convertToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            Date convertedDate = format.parse(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
