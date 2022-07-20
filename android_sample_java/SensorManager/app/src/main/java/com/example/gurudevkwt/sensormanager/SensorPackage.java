package com.example.gurudevkwt.sensormanager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorPackage {

    static SensorPackage sensorPackage;
    SensorManager sensorManager;
    Sensor sensor1;
    Sensor sensor2;
    Sensor sensor3;
    private float[] accelerometer;
    private float[] magnetic_field;
    private float light;

    CompassListener compassListener;
    LightListener lightListener;

    public interface CompassListener {
        void compassData(int[] data);
    }

    public interface LightListener {
        void lightData(int data);
    }

    public static SensorPackage getInstance(Context context) {
        if (sensorPackage == null) {
            sensorPackage = new SensorPackage(context);
        }
        return sensorPackage;
    }

    private SensorPackage(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void setCompassListener(CompassListener compassListener) {
        this.compassListener = compassListener;
    }

    public void setLightListener(LightListener lightListener) {
        this.lightListener = lightListener;
    }

    public void startSensor() {
        sensorManager.registerListener(sensorEventListener, sensor1, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener, sensor2, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener, sensor3, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER: {
                    accelerometer = sensorEvent.values.clone();
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD: {
                    magnetic_field = sensorEvent.values.clone();
                    break;
                }
                case Sensor.TYPE_LIGHT: {
                    light = sensorEvent.values[0];
                    lightListener.lightData((int) light);
                    break;
                }
            }

            if (accelerometer != null && magnetic_field != null) {
                float[] R = new float[16];
                float[] values = new float[3];
                SensorManager.getRotationMatrix(R, null, accelerometer, magnetic_field);
                SensorManager.getOrientation(R, values);

                if (compassListener != null) {
                    int[] data = new int[3];
                    data[0] = (int) Radian2Degree(values[0]);
                    data[1] = (int) Radian2Degree(values[1]);
                    data[2] = (int) Radian2Degree(values[2]);

                    compassListener.compassData(data);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    float Radian2Degree(float radian) {
        return radian * 180 / (float) Math.PI;
    }
}
