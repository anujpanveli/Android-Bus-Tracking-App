package com.example.trackingapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsService2 extends Service {

    private static final String TAG = TrackerService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("---ALERT---","Before requestLocationUpdates() of MapService2");
        requestLocationUpdates();

//        String driverId1 = PreferenceManager.getDefaultSharedPreferences(this).getString("driverId1", "");
//        String driverId2 = PreferenceManager.getDefaultSharedPreferences(this).getString("driverId2", "");
       // Log.d("driverId1",driverId1);
        Intent dialogIntent = new Intent(this,MapsActivity.class);
       // dialogIntent.putExtra("driverId1",driverId1);
       // dialogIntent.putExtra("driverId2",driverId2);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void requestLocationUpdates() {

       // String driverId1 = PreferenceManager.getDefaultSharedPreferences(this).getString("driverId1", "");
        String parentUID = PreferenceManager.getDefaultSharedPreferences(this).getString("parentUID", "");

        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "users" + "/" + parentUID + "/" + getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            Log.d(TAG, "before location update ");
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    Log.d(TAG, "after location update ");
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        ref.setValue(location);
                    }
                }
            }, null);
        }

    }

}
