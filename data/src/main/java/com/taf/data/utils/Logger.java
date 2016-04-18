package com.taf.data.utils;

import android.util.Log;

public class Logger {
    public static final boolean writeLog = true;
    public static final String TAG = "myLog-";

    public static void e(String tag, String msg) {
        writeLog('e', TAG + tag, msg);
    }

    public static void e(String msg) {
        writeLog('e', TAG, msg);
    }

    public static void i(String tag, String msg) {
        writeLog('i', TAG + tag, msg);
    }

    public static void i(String msg) {
        writeLog('i', TAG, msg);
    }

    public static void d(String tag, String msg) {
        writeLog('d', TAG + tag, msg);
    }

    public static void d(String msg) {
        writeLog('d', TAG, msg);
    }

    public static void w(String tag, String msg) {
        writeLog('w', TAG + tag, msg);
    }

    public static void w(String msg) {
        writeLog('w', TAG, msg);
    }

    public static void v(String tag, String msg) {
        writeLog('v', TAG + tag, msg);
    }

    public static void v(String msg) {
        writeLog('v', TAG, msg);
    }

    private static void writeLog(char flag, String tag, String msg) {
        if (writeLog) {
            switch (flag) {
                case 'd':
                    Log.d(tag, msg);
                    break;
                case 'e':
                    Log.e(tag, msg);
                    break;
                case 'i':
                    Log.i(tag, msg);
                    break;
                case 'w':
                    Log.w(tag, msg);
                    break;
                case 'v':
                    Log.v(tag, msg);
                    break;
                default:
                    break;
            }
        }
    }
}
