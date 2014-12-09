package com.yandex.disk.rest.conv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * http://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
 * <br/>
 * Helper class for handling ISO 8601 strings of the following format:
 * "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 */
public class ISO8601 {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat(FORMAT).format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /**
     * Get current date and time formatted as ISO 8601 string.
     */
    public static String nowInSeconds() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() - calendar.getTimeInMillis() % 1000L);
        return fromCalendar(calendar);
    }

    /**
     * Transform ISO 8601 string to Calendar.
     */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat(FORMAT).parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static Date parse(final String iso8601string) {
        try {
            return toCalendar(iso8601string).getTime();
        } catch (ParseException e) {
            return null;
        }
    }
}
