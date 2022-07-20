package com.example.gurudevkwt.analogsticksample;

/**
 * Created by 77100658 on 2018-01-09.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class AnalogStick {

    public static final int STICK_NONE = 0;
    public static final int STICK_UP_2 = 1;
    public static final int STICK_UP_1 = 2;
    public static final int STICK_UPRIGHT_2 = 3;
    public static final int STICK_UPRIGHT_1 = 4;
    public static final int STICK_RIGHT_2 = 5;
    public static final int STICK_RIGHT_1 = 6;
    public static final int STICK_DOWNRIGHT_2 = 7;
    public static final int STICK_DOWNRIGHT_1 = 8;
    public static final int STICK_DOWN_2 = 9;
    public static final int STICK_DOWN_1 = 10;
    public static final int STICK_DOWNLEFT_2 = 11;
    public static final int STICK_DOWNLEFT_1 = 12;
    public static final int STICK_LEFT_2 = 13;
    public static final int STICK_LEFT_1 = 14;
    public static final int STICK_UPLEFT_2 = 15;
    public static final int STICK_UPLEFT_1 = 16;

    private static int OFFSET = 0;

    private Context mContext;
    private ViewGroup mLayout;
    private static LayoutParams params;
    private int stick_width, stick_height;

    private int position_x = 0, position_y = 0, min_distance = 0;
    private float distance = 0, angle = 0;
    float argX, argY;
    float mX, mY;
    float x, y;

    static int mRadius;
    private DrawCanvas draw;
    private Paint paint;
    private Bitmap stick;

    private boolean touch_state = false;

    private OnDirectionListener onDirectionListener;

    void setOnDirectionListener(OnDirectionListener listener) {
        onDirectionListener = listener;
    }

    interface OnDirectionListener {
        void onDirection(int direction);
    }

    AnalogStick(Context context, ViewGroup layout, int stick_res_id) {
        mContext = context;
        stick = BitmapFactory.decodeResource(mContext.getResources(), stick_res_id);
        Log.i("가로 길이", "" + stick.getWidth());

        stick_width = stick.getWidth();
        stick_height = stick.getHeight();

        draw = new DrawCanvas(mContext);
        paint = new Paint();
        mLayout = layout;
        params = mLayout.getLayoutParams();
    }

    //설명_ 드래그하던 스틱을 놓으면 뷰를 remove하고 다시 add 함 (가운데로 오게끔)
    void drawCenter() {
        draw.position(mLayout.getLayoutParams().width / 2, mLayout.getLayoutParams().height / 2);
        draw();
    }

    void drawStick(MotionEvent arg1) {
        mRadius = (params.width / 2 - OFFSET);

        //설명_ x와 y 값을 0으로 맞춤
        position_x = (int) (arg1.getX() - (params.width / 2));
        position_y = (int) (arg1.getY() - (params.height / 2));
        distance = (float) Math.sqrt(Math.pow(position_x, 2) + Math.pow(position_y, 2));
        angle = (float) cal_angle(position_x, position_y);

        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            if (distance <= (params.width)) {
                draw.position(arg1.getX(), arg1.getY());
                draw();
                touch_state = true;
            }
        } else if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
            if (distance <= mRadius) { //참고_ 손가락이 원안에 있을 때
                argX = arg1.getX();
                argY = arg1.getY();
                draw.position(arg1.getX(), arg1.getY());
                draw();
            } else if (distance > mRadius) { //참고_ 손가락이 원밖으로 나갔을 때
                float x = (float) (Math.cos(Math.toRadians(cal_angle(position_x, position_y))) * ((params.width / 2) - OFFSET));
                float y = (float) (Math.sin(Math.toRadians(cal_angle(position_x, position_y))) * ((params.height / 2) - OFFSET));
                x += (params.width / 2);
                y += (params.height / 2);
                draw.position(x, y);
                draw();
            } else {
                mLayout.removeView(draw);
            }
        } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
            mLayout.removeView(draw);
            drawCenter();
            touch_state = false;
        }

        // 8 방향이 필요한경우
        onDirectionListener.onDirection(get8Direction());
    }

    public int[] getPosition() {
        if (distance > min_distance && touch_state) {
            return new int[]{position_x, position_y};
        }
        return new int[]{0, 0};
    }

    public int getX() {
        if (distance > min_distance && touch_state) {
            return position_x;
        }
        return 0;
    }

    public int getY() {
        if (distance > min_distance && touch_state) {
            return position_y;
        }
        return 0;
    }

    //설명_ 원의 각도
    public float getAngle() {
        if (distance > min_distance && touch_state) {
            return angle;
        }
        return 0;
    }

    //설명_ 화면의 전체 해상도
    public float getDistance() {
        if (distance > min_distance && touch_state) {
            return distance;
        }
        return 0;
    }

    public float getDrawX() {
        mX = (x + (stick_width / 2)) - (params.width / 2);
        return mX;
    }

    public float getDrawY() {
        mY = -((y + (stick_height / 2)) - (params.height / 2));
        mY = mY == 0.0 ? Math.abs(mY) : mY;
        return mY;
    }

    public void setMinimumDistance(int minDistance) {
        min_distance = minDistance;
    }

    public int getMinimumDistance() {
        return min_distance;
    }

    public int get8Direction() {
        if (distance > min_distance && touch_state) {
            if (angle >= 247.5 && angle < 292.5) {
                if (distance < mRadius) {
                    return STICK_UP_1;
                } else if (distance >= mRadius) {
                    return STICK_UP_2;
                }
            } else if (angle >= 292.5 && angle < 337.5) {
                if (distance < mRadius) {
                    return STICK_UPRIGHT_1;
                } else if (distance >= mRadius) {
                    return STICK_UPRIGHT_2;
                }
            } else if (angle >= 337.5 || angle < 22.5) {
                if (distance < mRadius) {
                    return STICK_RIGHT_1;
                } else if (distance >= mRadius) {
                    return STICK_RIGHT_2;
                }
            } else if (angle >= 22.5 && angle < 67.5) {
                if (distance < mRadius) {
                    return STICK_DOWNRIGHT_1;
                } else if (distance >= mRadius) {
                    return STICK_DOWNRIGHT_2;
                }
            } else if (angle >= 67.5 && angle < 112.5) {
                if (distance < mRadius) {
                    return STICK_DOWN_1;
                } else if (distance >= mRadius) {
                    return STICK_DOWN_2;
                }
            } else if (angle >= 112.5 && angle < 157.5) {
                if (distance < mRadius) {
                    return STICK_DOWNLEFT_1;
                } else if (distance >= mRadius) {
                    return STICK_DOWNLEFT_2;
                }
            } else if (angle >= 157.5 && angle < 202.5) {
                if (distance < mRadius) {
                    return STICK_LEFT_1;
                } else if (distance >= mRadius) {
                    return STICK_LEFT_2;
                }
            } else if (angle >= 202.5 && angle < 247.5) {
                if (distance < mRadius) {
                    return STICK_UPLEFT_1;
                } else if (distance >= mRadius) {
                    return STICK_UPLEFT_2;
                }
            }
        } else if (distance <= min_distance && touch_state) {
            return STICK_NONE;
        } else {
            return STICK_NONE;
        }
        return 0;
    }

    public void setOffset(int offset) {
        OFFSET = offset;
    }

    public int getOffset() {
        return OFFSET;
    }

    public void setLayoutColor(int resId) {
        mLayout.setBackgroundColor(mContext.getResources().getColor(resId));
    }

    public void setLayoutBackground(int resId) {
        mLayout.setBackgroundResource(resId);
    }

    public void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stick_width = stick.getWidth();
        stick_height = stick.getHeight();
    }

    public void setStickWidth(int width) {
        stick = Bitmap.createScaledBitmap(stick, width, stick_height, false);
        stick_width = stick.getWidth();
    }

    public void setStickHeight(int height) {
        stick = Bitmap.createScaledBitmap(stick, stick_width, height, false);
        stick_height = stick.getHeight();
    }

    public int getStickWidth() {
        return stick_width;
    }

    public int getStickHeight() {
        return stick_height;
    }

    public void setLayoutSize(int width, int height) {
        params.width = width;
        params.height = height;
    }

    public int getLayoutWidth() {
        return params.width;
    }

    public int getLayoutHeight() {
        return params.height;
    }

    private double cal_angle(float x, float y) {
         /* 참고_ atan = num의 arc tangent를 반환한다.
          * 참고_ toDegrees = 라디안 -> 각도로 변환 */
        if (x >= 0 && y >= 0) {
            return Math.toDegrees(Math.atan(y / x));
        } else if (x < 0 && y >= 0) {
            return Math.toDegrees(Math.atan(y / x)) + 180;
        } else if (x < 0 && y < 0) {
            return Math.toDegrees(Math.atan(y / x)) + 180;
        } else if (x >= 0 && y < 0) {
            return Math.toDegrees(Math.atan(y / x)) + 360;
        }
        return 0;
    }

    private void draw() {
        try {
            mLayout.removeView(draw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLayout.addView(draw);
    }

    private class DrawCanvas extends View {
        private DrawCanvas(Context mContext) {
            super(mContext);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(stick, x, y, paint);
        }

        private void position(float pos_x, float pos_y) {
            x = pos_x - (stick_width / 2);
            y = pos_y - (stick_height / 2);
        }
    }
}