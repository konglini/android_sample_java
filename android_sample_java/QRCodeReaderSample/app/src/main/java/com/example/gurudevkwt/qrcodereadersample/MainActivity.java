package com.example.gurudevkwt.qrcodereadersample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // QR코드로 만들 텍스트
        String text = "test";
        // QR코드의 한변의 크기 - 정사각형이므로 한변을 가지고 가로세로값으로 사용
        int qrWidth = DisplayUtil.getDisplaySize(this)[0] - sp2px(this, 100);

        // QR코드 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Bitmap bitmap = null;
        try {
            String encText = encrypt(text, "testKey");
            Toast.makeText(MainActivity.this, encText, Toast.LENGTH_LONG).show();
            BitMatrix bitMatrix = qrCodeWriter.encode(encText, BarcodeFormat.QR_CODE, qrWidth, qrWidth);
            bitmap = toBitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ImageView 에 세팅
        ImageView view = (ImageView) findViewById(R.id.qrView);
        view.setImageBitmap(bitmap);
    }


    public void btnClick(View view){
        Intent intent = new Intent(this, ActivityQRReader.class);
        startActivity(intent);

        ActivityQRReader.setOnSaveEvent(new ActivityQRReader.OnBarcodeSaveEventListener() {
            @Override
            public void onSaveEvent(String _scanBarcode) {

                //QR스캔한 값이 리턴되는부분
                if (_scanBarcode != null) {
                    try {
                        String decText = decrypt(_scanBarcode, "testKey");
                        Toast.makeText(MainActivity.this, decText, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //복호화
    public static String decrypt(String text, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;

        if (len > keyBytes.length) len = keyBytes.length;

        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        byte [] results = cipher.doFinal(Base64.decode(text, 0));

        return new String(results,"UTF-8");
    }

    //암호화
    public static String encrypt(String text, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;

        if (len > keyBytes.length) len = keyBytes.length;

        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

        return Base64.encodeToString(results, 0);
    }
}
