package com.google.zxing;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by 77100658 on 2017-07-21.
 */

public class EditTextUtil {

    public static InputFilter filterAlpha = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Pattern ps = Pattern.compile("^[a-zA-Z]+$");
            if (!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };

    public static InputFilter filterAlphaNum = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };

    public static InputFilter filterKor = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Pattern ps = Pattern.compile("^[ㄱ-가-힣]+$");
            if (!ps.matcher(source).matches()){
                return "";
            }
            return null;
        }
    };
}