package com.dairy.take12.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {

    public  Date convertToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); // No 'X'
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Convert to UTC
        return sdf.parse(dateString);
    }
}
