package com.taf.model;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by rakeeb on 10/20/16.
 */

public class CountryWidgetData extends BaseModel {

    public static final int COMPONENT_CALENDAR = 1;
    public static final int COMPONENT_FOREX = 2;
    public static final int COMPONENT_WEATHER = 3;

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


        /**
         * saved as
         * key: "country currency"
         * value: buying value
         */
        HashMap<String, String> currencyMap;

        /**
         * forex value for calendar
         */
        Calendar today;

        public Calendar getToday() {
            return today;
        }

        public HashMap<String, String> getCurrencyMap() {
            return currencyMap;
        }

        public void setCurrencyMap(HashMap<String, String> currencyMap) {
            this.currencyMap = currencyMap;
        }

        public void setToday(Calendar today) {
            this.today = today;
        }

        @Override
        public int componentType() {
            return COMPONENT_FOREX;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (String key : currencyMap.keySet()) {
                builder.append(String.format("key: %s, value: %s", key, currencyMap.get(key)));
                builder.append("\n");
            }
            return builder.toString();
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
