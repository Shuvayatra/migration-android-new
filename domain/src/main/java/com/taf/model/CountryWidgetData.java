package com.taf.model;

import java.util.Calendar;

/**
 * Created by rakeeb on 10/20/16.
 */

public class CountryWidgetData extends BaseModel {

    public static final int COMPONENT_CALENDAR = 1;
    public static final int COMPONENT_FOREX = 2;
    public static final int COMPONENT_WEATHER = 3;

    /**
     * Calendar instance for today
     */
    CalendarComponent mCalendar;

    // TODO: 10/20/16 Add exchange rate model
    // TODO: 10/20/16 Add weather model

    public interface Component {
        int componentType();
    }

    public static class CalendarComponent implements Component {

        Calendar mToday;
        String mNepaliDate;

        public void setToday(Calendar today) {
            mToday = today;
        }

        public void setNepaliDate(String nepaliDate) {
            mNepaliDate = nepaliDate;
        }

        public Calendar getToday() {
            return mToday;
        }

        public String getNepaliDate() {
            return mNepaliDate;
        }

        @Override
        public int componentType() {
            return COMPONENT_CALENDAR;
        }
    }

    public static class ForexComponent implements Component {

        @Override
        public int componentType() {
            return COMPONENT_FOREX;
        }
    }

    public static class WeatherComponent implements Component {
        String temperature;
        String weatherInfo;

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWeatherInfo() {
            return weatherInfo;
        }

        public void setWeatherInfo(String weatherInfo) {
            this.weatherInfo = weatherInfo;
        }

        @Override
        public int componentType() {
            return COMPONENT_WEATHER;
        }
    }
}
