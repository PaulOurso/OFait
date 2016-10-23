package com.devmobile.ofait.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Tony on 20/10/2016.
 */
public class FormatHelper {

    public static final String DATE_FORMAT_ENCODE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_DECODE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String formatCalToString(Calendar cal) {
        if (cal == null)
            return "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_ENCODE);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(cal.getTime());
    }

    public static Calendar formatStringToCal(String str) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_DECODE);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            cal.setTime(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}
