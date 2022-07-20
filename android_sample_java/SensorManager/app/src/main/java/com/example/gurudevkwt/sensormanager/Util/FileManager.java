/*
 * Copyright (c) 2015. Agi
 */

package com.example.gurudevkwt.sensormanager.Util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by hyunjinkang on 15. 3. 10..
 */
public class FileManager {
/* ******************** Variable Set ********************************* */

/* ******************** Variable Set End ********************************* */

/* ******************** Interface Set ********************************* */

/* ******************** Interface Set End ********************************* */

/* ******************** Abstract Set ********************************* */

/* ******************** Abstract Set End ********************************* */

/* ******************** Override Set ********************************* */

/* ******************** Override Set End ********************************* */

/* ******************** Method Set ********************************* */

    /**
     * <pre>
     * 외장메모리 사용가능 여부를 반환한다.
     * </pre>
     *
     * @return <pre>
     * true : 사용가능
     * false : 사용불가
     * </pre>
     * @version v1.0.0
     * @author Calix
     * @since 2014. 5. 29.
     */
    public static boolean isExternalPath() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 특정 디렉토리 경로를 반환
     *
     * @param envirDir : 찾고 싶은 카테고리 값
     * @return <pre>
     *  Environment.DIRECTORY_ALARMS        : 알람으로 사용할 오디오 파일을 저장합니다.
     *  Environment.DIRECTORY_DCIM	        : 카메라로 촬영한 사진이 저장됩니다.
     *  Environment.DIRECTORY_DOWNLOADS	    : 다운로드한 파일이 저장됩니다.
     *  Environment.DIRECTORY_MUSIC	        : 음악 파일이 저장됩니다.
     *  Environment.DIRECTORY_MOVIES	    : 영상 파일이 저장됩니다.
     *  Environment.DIRECTORY_NOTIFICATIONS	: 알림음으로 사용할 오디오 파일을 저장합니다.
     *  Environment.DIRECTORY_PICTURES	    : 그림 파일이 저장됩니다.
     *  Environment.DIRECTORY_PODCASTS	    : 팟캐스트(Poacast) 파일이 저장됩니다.
     * </pre>
     */
    public static String getExternalCategoryDir(String envirDir) {
        return Environment.getExternalStoragePublicDirectory(envirDir).getAbsolutePath() + File.separator;
    }

    /**
     * <pre>
     * 외장메모리 경로 반환
     * </pre>
     *
     * @return <pre></pre>
     * @version v1.0.0
     * @author Calix
     * @since 2014. 5. 29.
     */
    public static String getExternalPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * <pre>
     * 내부 파일 디렉토리 경로를 반환한다.
     * </pre>
     *
     * @param ctx
     * @return <pre></pre>
     * @version v1.0.0
     * @author Calix
     * @since 2014. 5. 29.
     */
    public static String getInternalStoragePath(Context ctx) {
        return ctx.getFilesDir().getAbsolutePath();
    }

    /**
     * 해당 주소의 디렉토리를 생성
     *
     * @param path
     * @return
     */
    public static File makeDirectory(String path) {
        File file = new File(path);
        if (file.exists() == false)
            file.mkdirs();

        return file;
    }

    /**
     * 해당 경로에 파일을 생성한다.
     *
     * @param path     : 디렉토리 경로
     * @param fileName : 파일명
     * @return
     */
    public static File makeFile(String path, String fileName) {
        makeDirectory(path);

        File file = new File(path + File.separator + fileName);

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                file = null;
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 해당 경로의 파일을 전부 삭제 한다.
     *
     * @param path
     */
    public static void removeFile(String path) {
        File file = new File(path);

        File[] files = file.listFiles();
        if (files != null) {
            for (File filename : files) {
                filename.delete();
            }
        }
    }

/* ******************** Method Set End ********************************* */

/* ******************** Server Method Set ********************************* */

/* ******************** Server Method Set End ********************************* */

/* ******************** Listener Set ********************************* */

/* ******************** Listener Set End ********************************* */



    public static void viewFile(Context ctx, String filePath) {
        File file = new File(filePath);
        showFile(ctx , file);
    }

    /**
     * 파일열기
     *
     * @param ctx
     * @param filePath
     * @param fileName
     */
    public static void viewFile(Context ctx, String filePath, String fileName) {

//        Intent fileLinkIntent = new Intent(Intent.ACTION_VIEW);
//        fileLinkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(filePath, fileName);
        showFile(ctx , file);
    }

    /**
     * 확장자에 따른 파일뷰어를 오픈
     *
     * @param ctx
     * @param file
     */
    private static void showFile(Context ctx, File file){

        Intent fileLinkIntent = new Intent(Intent.ACTION_VIEW);
        fileLinkIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Uri uri = Uri.fromFile(file);
        //확장자 구하기
        String fileExtend = getExtension(file.getAbsolutePath());
        // 파일 확장자 별로 mime type 지정해 준다.
        if (fileExtend.equalsIgnoreCase("mp3")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "audio/*");

        } else if (fileExtend.equalsIgnoreCase("mp4")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "vidio/*");

        } else if (fileExtend.equalsIgnoreCase("jpg")
                || fileExtend.equalsIgnoreCase("jpeg")
                || fileExtend.equalsIgnoreCase("gif")
                || fileExtend.equalsIgnoreCase("png")
                || fileExtend.equalsIgnoreCase("bmp")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "image/*");

        } else if (fileExtend.equalsIgnoreCase("txt")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "text/*");

        } else if (fileExtend.equalsIgnoreCase("doc")
                || fileExtend.equalsIgnoreCase("docx")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "application/msword");

        } else if (fileExtend.equalsIgnoreCase("xls")
                || fileExtend.equalsIgnoreCase("xlsx")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.ms-excel");
        } else if (fileExtend.equalsIgnoreCase("ppt")
                || fileExtend.equalsIgnoreCase("pptx")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");

        } else if (fileExtend.equalsIgnoreCase("pdf")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "application/pdf");

        } else if (fileExtend.equalsIgnoreCase("hwp")) {

            fileLinkIntent.setDataAndType(Uri.fromFile(file), "application/haansofthwp");
        }

        PackageManager pm = ctx.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(fileLinkIntent, PackageManager.GET_META_DATA);
        if (list.size() == 0) {
            Toast.makeText(ctx, file.getName() + "을 확인할 수 있는 앱이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show();
        } else {
            ctx.startActivity(fileLinkIntent);
        }
    }

    /**
     * 확장자 가져오기
     *
     * @param fileStr
     * @return
     */
    public static String getExtension(String fileStr) {
        return fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
    }

}
