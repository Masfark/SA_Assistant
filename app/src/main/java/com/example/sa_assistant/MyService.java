package com.example.sa_assistant;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.example.sa_assistant.ui.utils.Utils;

import java.text.DecimalFormat;

public class MyService extends Service {

    private LocationManager locationManager;
    private Location loc;
    private final double lat = 55.9977;
    private final double lon = 37.8649;
    Intent intent;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String s1 = new DecimalFormat("##.####").format(location.getLatitude());
            double s2 = Double.parseDouble(s1);
            String d1 = new DecimalFormat("##.####").format(location.getLongitude());
            double d2 = Double.parseDouble(d1);
            double test1 = lat - 0.0002;
            double test2 = lon - 0.0002;
//            textView.setText("Широта - " + s2 + "\nДолгота - " + d2);
            if (location.distanceTo(loc) <= 25) {
                Utils.Notify(getApplicationContext(), "Геолокация", "Координаты " + s2 + " " + d2);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}