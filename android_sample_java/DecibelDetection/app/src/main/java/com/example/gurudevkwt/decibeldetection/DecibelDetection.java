package com.example.gurudevkwt.decibeldetection;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.gurudevkwt.decibeldetection.FFT.RealDoubleFFT;

/**
 * Created by GURU on 2017-01-15.
 */

public class DecibelDetection extends AsyncTask<Void, double[], Void> {

    int db = 0;
    int count = 0;
    int frequency = 8000;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private RealDoubleFFT transformer;
    int blockSize = 256;
    boolean started = false;
    CountDownTimer count_start;
    CountDownTimer sub_count;

    public void startDecibelDetection(DecibelDetection decibelDetection) {
        if (!started) {
            transformer = new RealDoubleFFT(blockSize);
            started = true;
            decibelDetection.execute();
        }
    }

    public void stopDecibelDetection(DecibelDetection decibelDetection) {
        if (started) {
            started = false;
            decibelDetection.cancel(true);
        }
    }

    protected void onProgressUpdate(double[]... toTransform) {

        for (int i = 0; i < toTransform[0].length; i++) {

            int x = i;
            int downy = (int) (100 - (toTransform[0][i] * 10));
            int upy = 100;
            // 헤르쯔
            int hz = (int) (toTransform[0][i] * 100);

            if (hz > 2000) {
                Log.i("RecordAudio", "주파수 : " + hz + "HZ");

                // 데시벨
                db = (int) (10f * Math.log10(hz));
                if (db > 30) {
                    if (sub_count == null) {
                        subCount();
                    }
                    if (count_start == null) {
                        Log.i("!---2", "start");
                        countStart();
                    }
                }
                Log.i("RecordAudio", "HZ : " + hz + "  ,  DB : " + db);
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.VOICE_RECOGNITION, frequency, channelConfiguration, audioEncoding, bufferSize);

            short[] buffer = new short[blockSize];
            double[] toTransform = new double[blockSize];

            audioRecord.startRecording();

            while (started) {
                int bufferReadResult = audioRecord.read(buffer, 0, blockSize);

                for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                    toTransform[i] = (double) buffer[i] / Short.MAX_VALUE;
                }
                transformer.ft(toTransform);
                publishProgress(toTransform);
            }
            audioRecord.stop();
        } catch (Throwable t) {
            Log.i("AudioRecord", "Recording Failed");
        }
        return null;
    }

    private void countStart() {
        count_start = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (count >= 2) {
                    wakeApp();
                    Log.i("!---2", "finish");
                } else {
                    Log.i("!---2", "reset");
                }
                count = 0;
                count_start = null;
            }
        }.start();
    }

    private void subCount() {
        sub_count = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                count += 1;
                sub_count = null;
                Log.i("!---1", count + "");
            }
        }.start();
    }

    private void wakeApp() {
//        LibWakeLock.wakeLock(getApplicationContext());
//        LockKeyGuard guard = new LockKeyGuard(getApplicationContext());
//        guard.disable();
//
//        // 메인 엑티비티 호출
//        Intent intent = new Intent(getApplicationContext(), SlaveActivity.class);
//        intent.putExtra("msg", "1");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//
//        if (SimplePreferences.getBooleanPreference(getApplicationContext(), Define.PRKEY_APP_LIFE)) {
//            PrintLog.print("wakeApp", "Define.PRKEY_APP_LIFE : " + SimplePreferences.getBooleanPreference(getApplicationContext(), Define.PRKEY_APP_LIFE));
//        }

    }
}
