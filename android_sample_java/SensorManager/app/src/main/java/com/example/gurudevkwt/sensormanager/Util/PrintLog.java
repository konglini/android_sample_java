
package com.example.gurudevkwt.sensormanager.Util;

import android.util.Log;

/**
 * Created by ComAg on 16. 3. 14..
 */
public class PrintLog {

    // 현재 클래스명, 펑션명, 라인넘버를 얻기위한 값으로 5로 셋팅해야 함.
    private static final int STACK_TRACE_LEVELS = 5;

    public static void print(String tag, String message) {
        Log.e(tag, getClassNameMethodNameAndLineNumber() + message);
    }

    /**
     * Func Desc    :로그를 찍을 라인 넘버를 반환한다.
     */
    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS].getLineNumber();
    }

    /**
     * Func Desc    :로그를 찍을 클래스명을 반환한다.
     */
    private static String getClassName() {
        String fileName = Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS].getFileName();

        if (fileName != null && fileName.length() > 5) {
            return fileName.substring(0, fileName.length() - 5);
        } else {
            return "";
        }
    }

    /**
     * Func Desc    :로그를 찍을 펑션명을 반환한다.
     */
    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS].getMethodName();
    }

    /**
     * Func Desc    : 클래스명, 펑션명, 라인넘버를 반환한다.
     */
    private static String getClassNameMethodNameAndLineNumber() {
        return "[" + getClassName() + ":" + getMethodName() + ":" + getLineNumber() + "]: ";
    }

}
