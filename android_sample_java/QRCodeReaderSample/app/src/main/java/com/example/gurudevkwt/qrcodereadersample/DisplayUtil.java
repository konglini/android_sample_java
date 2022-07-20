package com.example.gurudevkwt.qrcodereadersample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by ComAg on 16. 3. 16..
 */
public class DisplayUtil {

    /* Device 해상도를 가지고 온다(px) */
    @SuppressLint("NewApi")
    public static int[] getDisplaySize(Context context) {

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics realMetrics = new DisplayMetrics();
        display.getRealMetrics(realMetrics);

        return new int[]{realMetrics.widthPixels, realMetrics.heightPixels};
    }

    public static int getStatusBarHeight(Context context){

        int statusHeight = 0;
        int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

        if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusHeight;
    }

    /**
     * 키보드를 보여준다.
     *
     * @param editText
     * @param context
     */
    public static void showKeyboard(EditText editText, Context context) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 키보드를 숨긴다.
     *
     * @param _view
     * @param context
     */
    public static void hideKeyboard(View _view, Context context) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

//    public static boolean hideKeyboard(AppCompatActivity _activity) {
//
//        View view = _activity.getCurrentFocus();
//
//        if (view instanceof EditText) {
//
//            DisplayUtil.hideKeyboard((EditText) view, _activity);
//
//            return true;
//        }
//
//        return false;
//    }
}
