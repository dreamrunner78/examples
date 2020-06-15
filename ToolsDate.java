package com.bas.tools;


import org.javatuples.Pair;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ToolsDate {

    public static String cleanDate(String input) {
        if (input.contains("."))
            return input.substring(0, input.indexOf("."));

        return input;

    }

    public static boolean isValidFormat(String format, String value, Locale locale) {
        DateTimeFormatter fotmatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            LocalDateTime ldt = LocalDateTime.parse(value, fotmatter);
            String result = ldt.format(fotmatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fotmatter);
                String result = ld.format(fotmatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fotmatter);
                    String result = lt.format(fotmatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //System.out.println(exp.getMessage());
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        //System.out.println("isValid - dd/MM/yyyy with 20130925 = " + isValidFormat("dd/MM/yyyy", "20130925", Locale.FRENCH));
        //System.out.println("isValid - dd/MM/yyyy hh:mm:ss with 29/01/200000019 09:00:45 = " + isValidFormat(" dd/MM/yyyy hh:mm:ss", "29/01/200000019 09:00:45", Locale.FRENCH));
        String d1 = "08-01-2020 17:14:28:181";
        System.out.println(ToolsDate.inferFormatAndConvertToNewFormatDate(d1, "yyyy-MM-dd HH:mm:ss"));
        long t = 1578446283938l;
        System.out.println(new Timestamp(1578514055823l));

    }

    public static List<String> getPossibleFormats() {
        List<String> localformats = Arrays.asList(
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss:SSS",
                "yyyy-MM-dd HH:mm:ss.SSSSSS", //oracle timestamp
                "yyyy-MM-dd HH:mm+ss",
                "yyyy-MM-dd HH:mm:ssZ",
                "yyyy-MM-dd HH:mm:ssz",

                "yyyy-MM-dd'T'HH:mm",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss:SSS",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                "yyyy-MM-dd'T'HH:mm+ss",
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ssz",

                "dd-MM-yyyy HH:mm",
                "dd-MM-yyyy HH:mm:ss",
                "dd-MM-yyyy HH:mm:ss:SSS",
                "dd-MM-yyyy HH:mm:ss.SSSSSS",
                "dd-MM-yyyy HH:mm+ss",
                "dd-MM-yyyy HH:mm:ssZ",
                "dd-MM-yyyy HH:mm:ssz",

                "dd-MM-yyyy'T'HH:mm",
                "dd-MM-yyyy'T'HH:mm:ss",
                "dd-MM-yyyy'T'HH:mm:ss:SSS",
                "dd-MM-yyyy'T'HH:mm:ss.SSSSSS",
                "dd-MM-yyyy'T'HH:mm+ss",
                "dd-MM-yyyy'T'HH:mm:ssZ",
                "dd-MM-yyyy'T'HH:mm:ssz",

                "dd-MM-yyyy",
                "yyyy-MM-dd",

                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd HH:mm:ss:SSS",
                "yyyy/MM/dd HH:mm:ss.SSSSSS",
                "yyyy/MM/dd HH:mm+ss",
                "yyyy/MM/dd HH:mm:ssZ",
                "yyyy/MM/dd HH:mm:ssz",

                //"yyyy/MM/dd'T'HH:mm",
                "yyyy/MM/dd'T'HH:mm:ss",
                "yyyy/MM/dd'T'HH:mm:ss:SSS",
                "yyyy/MM/dd'T'HH:mm:ss.SSSSSS",
                "yyyy/MM/dd'T'HH:mm+ss",
                "yyyy/MM/dd'T'HH:mm:ssZ",
                "yyyy/MM/dd'T'HH:mm:ssz",

                "dd/MM/yyyy HH:mm",
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm:ss:SSS",
                "dd/MM/yyyy HH:mm:ss.SSSSSS",
                "dd/MM/yyyy HH:mm+ss",
                "dd/MM/yyyy HH:mm:ssZ",
                "dd/MM/yyyy HH:mm:ssz",

                "dd/MM/yyyy'T'HH:mm",
                "dd/MM/yyyy'T'HH:mm:ss",
                "dd/MM/yyyy'T'HH:mm:ss:SSS",
                "dd/MM/yyyy'T'HH:mm:ss.SSSSSS",
                "dd/MM/yyyy'T'HH:mm+ss",
                "dd/MM/yyyy'T'HH:mm:ssZ",
                "dd/MM/yyyy'T'HH:mm:ssz",

                "dd/MM/yyyy",
                "yyyy/MM/dd");

        return localformats;

    }

    public static String inferFormatAndConvertToNewFormat(String date, String new_format) {
        if (date == null || "".equalsIgnoreCase(date))
            return null;
        date = cleanDate(date);
        Date retval = null;
        SimpleDateFormat goodsdf = null;
        for (String stringFormat : getPossibleFormats()) {
            try {
                if (isValidFormat(stringFormat, date, Locale.FRENCH)) {
                    goodsdf = new SimpleDateFormat(stringFormat);
                    retval = goodsdf.parse(date);
                    break;
                }
            } catch (ParseException e) {
                goodsdf = null;
                retval = null;
                continue;
            }
        }
        if (goodsdf != null) {
            goodsdf.applyPattern(new_format);
            return goodsdf.format(retval);
        }
        return null;
    }

    public static Date inferFormatAndConvertToNewFormatDate(String date, String new_format) {
        if (date == null || "".equalsIgnoreCase(date))
            return null;
        date = cleanDate(date);
        Date retval = null;
        SimpleDateFormat goodsdf = null;
        for (String stringFormat : getPossibleFormats()) {
            try {
                if (isValidFormat(stringFormat, date, Locale.FRENCH)) {
                    goodsdf = new SimpleDateFormat(stringFormat);
                    retval = goodsdf.parse(date);
                    break;
                }
            } catch (ParseException e) {
                goodsdf = null;
                retval = null;
                continue;
            }
        }
        return retval;
    }

    public static Date inferDate(String date) {
        Date retval = null;
        for (String stringFormat : getPossibleFormats()) {
            try {
                if (isValidFormat(stringFormat, date, Locale.FRENCH)) {
                    retval = new SimpleDateFormat(stringFormat).parse(date);
                    break;
                }

            } catch (ParseException e) {
                retval = null;
                continue;
            }
        }
        return retval;
    }

    public static int daysBetween(String dateBeforeString, String dateAfterString) throws ParseException {
        Date dateBefore = inferDate(dateBeforeString);
        Date dateAfter = inferDate(dateAfterString);
        int nbDays = (int) ((dateAfter.getTime() - dateBefore.getTime()) / (1000 * 60 * 60 * 24));
        nbDays++;
        return nbDays;
    }

    public static Pair<Integer, Integer> periodsBetweenDates(String dateBeforeString, String dateAfterString) {
        int diffMonth = 1;

        LocalDate startDate = convertToLocalDateViaInstant(inferDate(dateBeforeString));
        LocalDate endDate = convertToLocalDateViaInstant(inferDate(dateAfterString));
        int startMonth = startDate.getMonth().getValue();
        int endMonth = endDate.getMonth().getValue();

        int startYear = startDate.getYear();
        int endYear = endDate.getYear();
        int diffyear = endYear - startYear;
        diffMonth = (endMonth - startMonth) + (diffyear * 12);
        diffMonth++;

        return new Pair<>(diffMonth, diffyear);
    }

    public static String getTheLatestDate(String d1, String d2) {
        Date date1 = inferDate(d1);
        Date date2 = inferDate(d2);

        if (date1.compareTo(date2) > 0) {
            return d2;
        } else if (date1.compareTo(date2) < 0) {
            return d1;
        }
        return d1;
    }

    // Convert Date to LocalDate
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {

        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    //A similar solution to the above one, but with a different way of creating an Instant object – using the ofEpochMilli() method:
    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static int getMonthFromDate(String date) {
        Date tmp_date = inferDate(date);
        return convertToLocalDateViaMilisecond(tmp_date).getMonth().getValue();
    }


}
