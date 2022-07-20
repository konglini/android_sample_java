package com.example.gurudevkwt.speechrecognizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by guru.dev.KWT on 2016-11-15.
 */

public class VoiceCatch {
    String[] ERR_LIST = {
            "ERROR_NETWORK_TIMEOUT",
            "ERROR_NETWORK",
            "ERROR_AUDIO",
            "ERROR_SERVER",
            "ERROR_CLIENT",
            "ERROR_SPEECH_TIMEOUT",
            "ERROR_NO_MATCH",
            "ERROR_RECOGNIZER_BUSY",
            "ERROR_INSUFFICIENT_PERMISSIONS"
    };

    private Context mContext;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;

    private MainActivity mainActivity;

    public VoiceCatch(Context context) {
        mContext = context;
        if (mContext.getClass().getSimpleName().equals("MainActivity")) {
            mainActivity = (MainActivity) context;
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        speechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(mContext);
        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {
            if (mContext.getClass().getSimpleName().equals("MainActivity")) {
                mainActivity.decibel(String.valueOf(rmsdB));
            }
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            Log.i("onError", ERR_LIST[error - 1]);

            if (error == 6 || error == 7) {
                if (mContext.getClass().getSimpleName().equals("MainActivity")) {
                    mainActivity.result("");
                }
            }
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = android.speech.SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            if (mResult.size() > 0) {
                if (mContext.getClass().getSimpleName().equals("MainActivity")) {
                    mainActivity.result(rs[0]);
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    public void isRuning(boolean b) {
        if (b) {
            speechRecognizer.startListening(intent);
        } else {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
    }
}
