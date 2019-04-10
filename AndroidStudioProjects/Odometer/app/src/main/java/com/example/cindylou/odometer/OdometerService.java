package com.example.cindylou.odometer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

public class OdometerService extends Service {

    private final IBinder binder = new OdometerBinder();
    private LocationListener listener;
    private LocationManager locManager;
    private static double distanceInMeters;
    private static Location lastLocation = null; //storing distance traveled in meters & last location as private static variables
    public static final String PERMISSION_STRING
            = android.Manifest.permission.ACCESS_FINE_LOCATION;

    public class OdometerBinder extends Binder { //creates a bound service, enables activity to bind to service
        OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    @Override
    public void onCreate() {  //when service is first created
        super.onCreate();
        listener = new LocationListener() { //new location listener used for getting updates on device location has changed.
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation == null) { //gets called whenever the locationlistener is told the device location has changed.
                    lastLocation = location; //if it's our first location, set lastLocation to current location
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location; //add the distance between this location and the last to the distanceInMeters variable
            }  //set lastLocation to the current location

            @Override
            public void onProviderDisabled(String arg0) {
            } //gets called when GSP is disabled or enabled, or if its status has changed; don't need to react to events

            @Override
            public void onProviderEnabled(String arg0) {
            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
            }
        };
                                        //access Android location service
        locManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE); //getSystemService() method earlier to get access
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING) //notification service
                == PackageManager.PERMISSION_GRANTED) {
            String provider = locManager.getBestProvider(new Criteria(), true);
            if (provider != null) {
                locManager.requestLocationUpdates(provider, 1000, 1, listener);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    } //gets called when activity binds to service
    //method must be implemented by returning an IBinder object; return null if I don't want activity binding service
    @Override
    public void onDestroy() {  //service is no longer being used & is about to be destroyed
        super.onDestroy();
        System.out.println("message: service stopped");
        if (locManager != null && listener != null) {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING)
                    == PackageManager.PERMISSION_GRANTED) {
                locManager.removeUpdates(listener);
            }
            locManager = null;
            listener = null;
        }
    }

    public double getDistance() {
        return this.distanceInMeters / 1609.344;
    }
}
//converts distance traveled in meters into miles