/**
 * <pre>
 * 출처 : http://arabiannight.tistory.com/64 [아라비안 나이트]
 * 핸드폰 Rooting 여부 체크 로직
 * </pre>
 * <p/>
 * co.kr.common RootingCheck.java
 *
 * @version v1.0.0
 * @author Calix
 * @since 2013.2013. 10. 4.
 */
package com.example.gurudevkwt.sensormanager.Util;

import java.io.File;

public class JRootingCheck {
    /* ******************** 전역 변수 셋팅 시작 ********************************* */

    /** Rooting 여부 체크 PATH */
    private static String[] RootFilesPath = new String[]{
            "system/bin/su", "system/xbin/su", "system/xbin/who", "system/xbin/whoami", "system/app/SuperUser.apk",
            "data/data/com.noshufou.android.su"};

    /* ******************** 전역 변수 셋팅 끝 ********************************* */

    /* ******************** 인터페이스 셋팅 시작 ********************************* */

    /* ******************** 인터페이스 셋팅 끝 ********************************* */

    /* ******************** 메소드 셋팅 시작 ********************************* */

    /**
     * <pre>
     * 루팅 여부를 체크한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @return <pre>
     * [true]   : 루팅한 상태
     * [false]  : 루팅안한 상태
     * </pre>
     */
    public static boolean isRootingCheck() {
        boolean isRootingFlag = false;

        try {
            Runtime.getRuntime().exec("su");
            isRootingFlag = true;
        } catch (Exception e) {
            // Exception 나면 루팅 false;
            isRootingFlag = false;
        }

        if (!isRootingFlag) {
            isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
        }

        return isRootingFlag;
    }

    /**
     * <pre>
     * 루팅파일 의심 Path를 가진 파일들을 생성 한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param sfiles
     * @return <pre></pre>
     */
    private static File[] createFiles(String[] sfiles) {
        File[] rootingFiles = new File[sfiles.length];
        for (int i = 0; i < sfiles.length; i++) {
            rootingFiles[i] = new File(sfiles[i]);
        }
        return rootingFiles;
    }

    /**
     * <pre>
     * 루팅파일 여부를 확인 한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param file
     * @return <pre>
     * [true]   : 루팅한 상태
     * [false]  : 루팅안한 상태
     * </pre>
     */
    private static boolean checkRootingFiles(File... file) {
        boolean isRooting = false;
        for (File f : file) {
            if (f != null && f.isFile()) {
                isRooting = true;
                break;
            } else {
                isRooting = false;
            }
        }
        return isRooting;
    }
    /* ******************** 메소드 셋팅 끝 ********************************* */

    /* ******************** Listener 셋팅 시작 ********************************* */

    /* ******************** Listener 셋팅 끝 ********************************* */
}
