package com.example.gurudevkwt.analogsticksample;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtLog, txtAngle;
//    FrameLayout controlPad;
//    AnalogStick js;

    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Rect rect2;
    Rect rect3;
    Rect rect4;
    Rect rect5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        controlPad = (FrameLayout) findViewById(R.id.controlPad);
//        controlPad.setOnTouchListener(touchListener);
        txtLog = findViewById(R.id.txt_log);
        txtAngle = findViewById(R.id.txt_angle);

        txtLog.setText("NONE");

//        js = new AnalogStick(getApplicationContext(), controlPad, R.drawable.ci_guru_enable);
//        js.setStickSize(100, 100);
////        js.setLayoutSize(500, 500);
//        js.setOffset(50);
//        js.setMinimumDistance(50); //참고_ NONE의 범위
//        js.setLayoutBackground(R.drawable.shape); //참고_ controlpad의 배경
//        js.setOnDirectionListener(directionListener); //참고_ 방향설정 리스너
//        js.drawCenter();

        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);


    }

//    View.OnTouchListener touchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            js.drawStick(motionEvent);
//            return true;
//        }
//    };
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int)event.getX();
        int y = (int)event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){

            Log.e("touch", "touch x : " + x + "  touch y : " + y);
            if(rect2.contains(x, y)){
                Log.e("touch", "rect2 contained !!!!");
                btn2.setPressed(true);
                btn3.setPressed(false);
                btn4.setPressed(false);
                btn5.setPressed(false);

            }else if(rect3.contains(x, y)){
                Log.e("touch", "rect3 contained !!!!");
                btn2.setPressed(false);
                btn3.setPressed(true);
                btn4.setPressed(false);
                btn5.setPressed(false);

            }else if(rect4.contains(x, y)){
                Log.e("touch", "touch contained !!!!");
                btn2.setPressed(false);
                btn3.setPressed(false);
                btn4.setPressed(true);
                btn5.setPressed(false);

            }else if(rect5.contains(x, y)){
                Log.e("touch", "rect5 contained !!!!");
                btn2.setPressed(false);
                btn3.setPressed(false);
                btn4.setPressed(false);
                btn5.setPressed(true);
            }else{

                btn2.setPressed(false);
                btn3.setPressed(false);
                btn4.setPressed(false);
                btn5.setPressed(false);
            }

        }else if (event.getAction() == MotionEvent.ACTION_UP){

            btn2.setPressed(false);
            btn3.setPressed(false);
            btn4.setPressed(false);
            btn5.setPressed(false);
        }

        return super.onTouchEvent(event);
    }
//    AnalogStick.OnDirectionListener directionListener = new AnalogStick.OnDirectionListener() {
//        int tmpDirection = 0;
//
//        @Override
//        public void onDirection(int direction) {
//
//            // 방향이 바뀔때만 적용되도록...
//            if (tmpDirection != direction) {
//                tmpDirection = direction;
//                switch (direction) {
//                    case AnalogStick.STICK_NONE:
//                        txtLog.setText("NONE");
//                        break;
//                    case AnalogStick.STICK_UP_2:
//                        txtLog.setText("UP_2");
//                        break;
//                    case AnalogStick.STICK_UP_1:
//                        txtLog.setText("UP_1");
//                        break;
//                    case AnalogStick.STICK_UPRIGHT_2:
//                        txtLog.setText("UPRIGHT_2");
//                        break;
//                    case AnalogStick.STICK_UPRIGHT_1:
//                        txtLog.setText("UPRIGHT_1");
//                        break;
//                    case AnalogStick.STICK_RIGHT_2:
//                        txtLog.setText("RIGHT_2");
//                        break;
//                    case AnalogStick.STICK_RIGHT_1:
//                        txtLog.setText("RIGHT_1");
//                        break;
//                    case AnalogStick.STICK_DOWNRIGHT_2:
//                        txtLog.setText("DOWNRIGHT_2");
//                        break;
//                    case AnalogStick.STICK_DOWNRIGHT_1:
//                        txtLog.setText("DOWNRIGHT_1");
//                        break;
//                    case AnalogStick.STICK_DOWN_2:
//                        txtLog.setText("DOWN_2");
//                        break;
//                    case AnalogStick.STICK_DOWN_1:
//                        txtLog.setText("DOWN_1");
//                        break;
//                    case AnalogStick.STICK_DOWNLEFT_2:
//                        txtLog.setText("DOWNLEFT_2");
//                        break;
//                    case AnalogStick.STICK_DOWNLEFT_1:
//                        txtLog.setText("DOWNLEFT_1");
//                        break;
//                    case AnalogStick.STICK_LEFT_2:
//                        txtLog.setText("LEFT_2");
//                        break;
//                    case AnalogStick.STICK_LEFT_1:
//                        txtLog.setText("LEFT_1");
//                        break;
//                    case AnalogStick.STICK_UPLEFT_2:
//                        txtLog.setText("UPLEFT_2");
//                        break;
//                    case AnalogStick.STICK_UPLEFT_1:
//                        txtLog.setText("UPLEFT_1");
//                        break;
//
//                }
//            }
//        }
//    };
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        Rect rect = new Rect();
        Window win = getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBar = rect.top;

        rect2 = new Rect((int)btn2.getLeft(), (int)btn2.getTop() + statusBar, (int)btn2.getRight(), (int)btn2.getBottom() + statusBar);
        rect3 = new Rect((int)btn3.getLeft(), (int)btn3.getTop() + statusBar, (int)btn3.getRight(), (int)btn3.getBottom() + statusBar);
        rect4 = new Rect((int)btn4.getLeft(), (int)btn4.getTop() + statusBar, (int)btn4.getRight(), (int)btn4.getBottom() + statusBar);
        rect5 = new Rect((int)btn5.getLeft(), (int)btn5.getTop() + statusBar, (int)btn5.getRight(), (int)btn5.getBottom() + statusBar);

        super.onWindowFocusChanged(hasFocus);
    }


//    AnalogStick.OnDirectionListener directionListener = new AnalogStick.OnDirectionListener() {
//        int tmpDirection = 0;
//
//        @Override
//        public void onDirection(int direction) {
//            // 방향이 바뀔때만 적용되도록...
//            if (tmpDirection != direction) {
//                tmpDirection = direction;
//                Log.e("MainActivity", "direction : " + direction);
//
//                switch (direction){
//                    case AnalogStick.STICK_NONE:
//
//                        break;
//                    case AnalogStick.STICK_UP:
//
//                        break;
//                    case AnalogStick.STICK_UPRIGHT:
//
//                        break;
//                    case AnalogStick.STICK_RIGHT:
//
//                        break;
//                    case AnalogStick.STICK_DOWNRIGHT:
//
//                        break;
//                    case AnalogStick.STICK_DOWN:
//
//                        break;
//                    case AnalogStick.STICK_DOWNLEFT:
//
//                        break;
//                    case AnalogStick.STICK_LEFT:
//
//                        break;
//                    case AnalogStick.STICK_UPLEFT:
//
//                        break;
//                }
//            }
//        }
//    };
}
