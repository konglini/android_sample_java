package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


@SuppressLint("MissingPermission")
public class SensorPackage {
    static SensorPackage sensorPackage;

    LocationManager locationManager;

    Listener Listener;

    public interface Listener {
        void onLocationData(String info, double[] data);
    }

    public static SensorPackage getInstance(Context context) {
        if (sensorPackage == null) {
            sensorPackage = new SensorPackage(context);
        }
        return sensorPackage;
    }

    private SensorPackage(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.i("!---3", location.getProvider() + "/" + location.getLongitude() + "/" + location.getLatitude() + "/" + location.getAltitude());
        }
    }

    public void setListener(Listener Listener) {
        this.Listener = Listener;
    }

    public int startSensor() {
        int check_sensor = 0;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            check_sensor += 1;
        }

        return check_sensor;
    }

    public void stopSensor() {
        locationManager.removeUpdates(locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("!---2", location.getProvider() + "/" + location.getLongitude() + "/" + location.getLatitude() + "/" + location.getAltitude());

            double[] location_data = new double[3];
            location_data[0] = location.getLongitude();
            location_data[1] = location.getLatitude();
            location_data[2] = location.getAltitude();

            Listener.onLocationData(location.getProvider(), location_data);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
