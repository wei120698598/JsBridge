package com.shuxin.bridge.utils;

import android.util.Log;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @description
 * @date 2019-05-23
 * @email weishuxin@icourt.cc
 */
public class Logger {

    public static final boolean isDebug = true;
    private static final String TAG = "JSBridge";

    public static void logI(String tag, Object content) {
        if (isDebug && content != null) {
            Log.i(tag, String.valueOf(content));
        }
    }

    public static void logI(Object content) {
        logI(TAG, content);
    }

    public static void logD(String tag, Object content) {
        if (isDebug && content != null) {
            Log.d(tag, String.valueOf(content));
        }
    }

    public static void logD(Object content) {
        logD(TAG, content);
    }

    public static void logW(String tag, Object content) {
        if (isDebug && content != null) {
            Log.w(tag, String.valueOf(content));
        }
    }

    public static void logW(Object content) {
        logW(TAG, content);
    }

    public static void logE(String tag, Object content) {
        if (isDebug && content != null) {
            Log.e(tag, String.valueOf(content));
        }
    }

    public static void logE(Object content) {
        logE(TAG, content);
    }


    public static void sout(Object content) {
        if (isDebug && content != null) {
            System.out.println("shuxin.wei: " + content);
        }
    }

}
