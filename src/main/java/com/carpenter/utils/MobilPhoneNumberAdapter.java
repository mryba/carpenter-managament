package com.carpenter.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobilPhoneNumberAdapter extends XmlAdapter<String, String> {

    private static final Pattern MARSHALL_PATTERN = Pattern.compile("(?:\\+48)(\\d{3})(\\d{3})(\\d{3})");
    private static final Pattern UNMARSHALL_PATTERN = Pattern.compile("(\\d{3})\\s*(\\d{3})\\s*(\\d{3})");

    @Override
    public String unmarshal(String phone) throws Exception {
        if (phone == null) {
            return null;
        }
        Matcher matcher = UNMARSHALL_PATTERN.matcher(phone);
        if (matcher.matches()) {
            return phone;
        }
        return "+48" + matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3);
    }

    @Override
    public String marshal(String phone) throws Exception {
        if (phone == null) {
            return null;
        }
        Matcher matcher = MARSHALL_PATTERN.matcher(phone);
        if (matcher.matches()) {
            return phone;
        }
        return matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3);
    }
}
