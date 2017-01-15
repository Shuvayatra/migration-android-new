package com.taf.data.utils;

import android.util.SparseArray;

import com.taf.data.utils.Logger;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rakeeb on 10/19/16.
 */

public class DateUtils {

    public static int getAge(Date now, Date dateOfBirth) {
        long timeBetween = now.getTime() - dateOfBirth.getTime();
        double yearsBetween = timeBetween / 3.156e+10;
        return (int) Math.floor(yearsBetween);
    }

    public static class NepaliDateConverter {

        private SparseArray<int[]> nepaliCalendarMap;

        /**
         * default values corresponding to
         */
        private int initialEnglishYear = 1943;
        private int initialEnglishMonth = 4;
        private int initialEnglishDay = 14;
        private int initialDay = Calendar.WEDNESDAY;

        private int initialNepaliYear = 2000;
        private int initialNepaliMonth = 1;
        private int initialNepaliDay = 1;

        public NepaliDateConverter() {
            init();
        }

        /**
         *
         */
        private void init() {
            nepaliCalendarMap = new SparseArray<>();
            // entry from years 2000 to 2090
            nepaliCalendarMap.put(2000, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2001, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2002, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2003, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2004, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2005, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2006, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2007, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2008, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 29, 31});
            nepaliCalendarMap.put(2009, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2010, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2011, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2012, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2013, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2014, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2015, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2016, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2017, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2018, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2019, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2020, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2021, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2022, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 30});
            nepaliCalendarMap.put(2023, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2024, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2025, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2026, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2027, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2028, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2029, new int[]{0, 31, 31, 32, 31, 32, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2030, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2031, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2032, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2033, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2034, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2035, new int[]{0, 30, 32, 31, 32, 31, 31, 29, 30, 30,
                    29, 29, 31});
            nepaliCalendarMap.put(2036, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2037, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2038, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2039, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2040, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2041, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2042, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2043, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2044, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2045, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2046, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2047, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2048, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2049, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 30});
            nepaliCalendarMap.put(2050, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2051, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2052, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2053, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 30});
            nepaliCalendarMap.put(2054, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2055, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2056, new int[]{0, 31, 31, 32, 31, 32, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2057, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2058, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2059, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2060, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2061, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2062, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2063, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2064, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2065, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2066, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 29, 31});
            nepaliCalendarMap.put(2067, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2068, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2069, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2070, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2071, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2072, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2073, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 31});
            nepaliCalendarMap.put(2074, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2075, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2076, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 30});
            nepaliCalendarMap.put(2077, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 29, 31});
            nepaliCalendarMap.put(2078, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2079, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30,
                    29, 30, 30});
            nepaliCalendarMap.put(2080, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29,
                    29, 30, 30});
            nepaliCalendarMap.put(2081, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2082, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2083, new int[]{0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2084, new int[]{0, 31, 31, 32, 31, 31, 30, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2085, new int[]{0, 31, 32, 31, 32, 30, 31, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2086, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2087, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2088, new int[]{0, 30, 31, 32, 32, 30, 31, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2089, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 30, 30});
            nepaliCalendarMap.put(2090, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29,
                    30, 30, 30});
            Logger.e(TAG, "--- finish init ---");
        }

        private static final String TAG = "NepaliDateConverter";

        /**
         * @param today calendar value
         * @return string value of current date in nepali in [date month year, day] format
         */
        public String englishToNepali(Calendar today) {

            Calendar currentEnglishDate = new GregorianCalendar();
            int year = today.get(Calendar.YEAR);
            int month = today.get(Calendar.MONTH);
            int day = today.get(Calendar.DAY_OF_MONTH);
            currentEnglishDate.set(year, month, day);

            Logger.e(TAG, String.format("calculation on: %d/%d/%d", year, month, day));

            Calendar baseEnglishDate = new GregorianCalendar();
            baseEnglishDate.set(initialEnglishYear, initialEnglishMonth, initialEnglishDay);

            long difference = daysBetween(baseEnglishDate, currentEnglishDate);

            int nepaliYear = initialNepaliYear;
            int nepaliMonth = initialNepaliMonth;
            int nepaliDay = initialNepaliDay;

            while (difference != 0) {

                int diffDate = nepaliCalendarMap.get(nepaliYear)[nepaliMonth];
                nepaliDay++;

                if (nepaliDay > diffDate) {
                    nepaliMonth++;
                    nepaliDay = 1;
                }
                if (nepaliMonth > 12) {
                    nepaliYear++;
                    nepaliMonth = 1;
                }

                initialDay++; // count the days in terms of 7 days
                if (initialDay > 7) {
                    initialDay = 1;
                }

                difference--;
            }

            Calendar nepaliCal = Calendar.getInstance();
            nepaliCal.set(nepaliYear, nepaliMonth, nepaliDay);

            Logger.e(TAG, String.format("result: %d/%d/%d", nepaliDay, nepaliMonth, nepaliYear));
            return String.format("%s %s %s, %s",
                    getNepaliNumericals(nepaliDay),
                    getNepaliMonth(nepaliMonth == 12 ? 1 : nepaliMonth + 1),
                    getNepaliNumericals(nepaliYear),
                    getNepaliDay(today.get(Calendar.DAY_OF_WEEK)));
        }
    }

    public static String getNepaliMonth(int month) {
        switch (month) {
            case 1:
                return "बैशाख";
            case 2:
                return "जेठ";
            case 3:
                return "असार";
            case 4:
                return "साउन";
            case 5:
                return "भदौ";
            case 6:
                return "असोज";
            case 7:
                return "कार्तिक";
            case 8:
                return "मंसिर";
            case 9:
                return "पुष";
            case 10:
                return "माघ";
            case 11:
                return "फाल्गुन";
            case 12:
                return "चैत्र";
        }
        return "";
    }

    public static String getNepaliDay(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return "आइतवार";
            case Calendar.MONDAY:
                return "सोमवार";
            case Calendar.TUESDAY:
                return "मङ्गलवार";
            case Calendar.WEDNESDAY:
                return "बुधवार";
            case Calendar.THURSDAY:
                return "बिहिवार";
            case Calendar.FRIDAY:
                return "शुक्रवार";
            case Calendar.SATURDAY:
                return "शनिवार";
        }
        return "";
    }

    public static String getEnglishDay(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        return "";
    }

    public static String getNepaliNumericals(int date) {
        StringBuilder builder = new StringBuilder();
        for (char c : String.valueOf(date).toCharArray()) {
            switch (Character.getNumericValue(c)) {
                case 0:
                    builder.append("०");
                    break;
                case 1:
                    builder.append("१");
                    break;
                case 2:
                    builder.append("२");
                    break;
                case 3:
                    builder.append("३");
                    break;
                case 4:
                    builder.append("४");
                    break;
                case 5:
                    builder.append("५");
                    break;
                case 6:
                    builder.append("६");
                    break;
                case 7:
                    builder.append("७");
                    break;
                case 8:
                    builder.append("८");
                    break;
                case 9:
                    builder.append("९");
                    break;
            }
        }
        return builder.toString();
    }

    private static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar refDate = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (refDate.before(endDate)) {
            refDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    /**
     * this default pattern corresponds to 9 October 2016.
     * <p>
     * see other formats <a href="http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html">here</a>
     */
    public static final String DEFAULT_DATE_PATTERN = "dd MMMM yyyy";

    public static String getFormattedDate(String pattern, Date date, TimeZone timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    public static String getFormattedDate(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        return format.format(date);
    }
    public static String getFormattedDate(Long date){
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_PATTERN,Locale.US);
        return format.format(date);
    }
}
