package com.example.gurudevkwt.qrcodereadersample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.zxing.client.android.CaptureView;

public class ActivityQRReader extends Activity implements CaptureView.CaptureActivityListener {


    public static String TAG = ActivityQRReader.class.getSimpleName();

    RelativeLayout mRootView;

    // body
    RelativeLayout mCaptureBody;
    CaptureView mCaptureView;

    public static OnBarcodeSaveEventListener onBarcodeSaveEventListener;

    public static interface OnBarcodeSaveEventListener {
        void onSaveEvent(String _scanBarcode);
    }

    public static void setOnSaveEvent(OnBarcodeSaveEventListener _onBarcodeSaveEventListener) {
        onBarcodeSaveEventListener = _onBarcodeSaveEventListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.lay_qr_reader);
        init();
    }

    public void init() {

        mRootView = (RelativeLayout) findViewById(R.id.content_capture);

        mCaptureBody = (RelativeLayout) findViewById(R.id.capture_body);

        int[] displaySize = DisplayUtil.getDisplaySize(this);
        mCaptureView = new CaptureView(this, displaySize, 0);
        mCaptureView.setOnCaptureActivityListener(this);
        mCaptureBody.addView(mCaptureView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCaptureView != null)
            mCaptureView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCaptureView != null)
            mCaptureView.onPause();
    }

    @Override
    public void onCloseActivity(boolean _close) {

    }

    // 바코드스캔에 성공했을 경우
    @Override
    public void onResultBarcode(String _barcode) {

        if (onBarcodeSaveEventListener != null) {
            onBarcodeSaveEventListener.onSaveEvent(_barcode);
        }
        finish();

    }

}
