package com.example.gurudevkwt.folderopen;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * 미디어 스캐너 클래스
 * http://egloos.zum.com/dalgopower/v/4380780
 */

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient{

    private MediaScannerConnection mScanner;
    private String[] mFilePaths = null;

    public MediaScanner(Context ctx) {
        mScanner = new MediaScannerConnection(ctx, this);
    }

    public void startScan(String[] filePaths){
        if(filePaths == null){
            return ;
        }
        mFilePaths = filePaths;
        mScanner.connect(); // onMediaScannerConnected()는 connect() 이후 호출됨
    }

    @Override
    public void onMediaScannerConnected() {
        if(mFilePaths != null){
            for(String filePath : mFilePaths) {
                mScanner.scanFile(filePath, null); // MediaStore 정보 업데이트
            }
        }else{
            mScanner.disconnect(); //스캔이 안된경우 연결 해제
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mScanner.disconnect();  // 스캔이 완료되면 연결 해제
    }
}
