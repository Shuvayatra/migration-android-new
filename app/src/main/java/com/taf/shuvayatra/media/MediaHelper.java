package com.taf.shuvayatra.media;

import java.text.DecimalFormat;

public class MediaHelper {
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = 0.0;
        double currentSeconds = (double) (currentDuration / 1000);
        double totalSeconds = (double) (totalDuration / 1000);
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);
        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static String getFormattedTime(long timeMillis) {
        int min = (int) (timeMillis / 60000);
        int secs = (int) ((timeMillis / 1000) % 60);

        DecimalFormat formatter = new DecimalFormat("00");

        return formatter.format(min) + ":" + formatter.format(secs);
    }
}
