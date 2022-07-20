package com.example.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;


public class SensorPackage {

    static SensorPackage sensorPackage;
    SensorManager sensorManager;
    Sensor sensor_heart_rate;

    sensorListener sensorListener;

    FusedLocationProviderClient fusedLocationProviderClient;

    public interface sensorListener {
        void onHeartRateData(int data);

        void onLocationData(double[] data);
    }

    public static SensorPackage getInstance(Context context) {
        if (sensorPackage == null) {
            sensorPackage = new SensorPackage(context);
        }
        return sensorPackage;
    }

    private SensorPackage(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor_heart_rate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (sensor_heart_rate != null) {
            sensorManager.registerListener(sensorEventListener, sensor_heart_rate, SensorManager.SENSOR_DELAY_GAME);
        }

        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000L)
                    .setFastestInterval(500L);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }

    public void setSensorListener(sensorListener listener) {
        sensorListener = listener;
    }

    public void stopSensor() {
        sensorManager.unregisterListener(sensorEventListener);

        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_HEART_RATE: {
                    float rate_data = sensorEvent.values[0];
                    sensorListener.onHeartRateData((int) rate_data);
                    break;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                double latitude = locationList.get(locationList.size() - 1).getLatitude();
                double longitude = locationList.get(locationList.size() - 1).getLongitude();
                double[] location_data = new double[]{latitude, longitude};
                sensorListener.onLocationData(location_data);
            }
        }
    };
}
