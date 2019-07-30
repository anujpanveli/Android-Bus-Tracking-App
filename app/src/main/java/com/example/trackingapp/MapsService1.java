package com.example.trackingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapsService1 extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_service1);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startParentTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

    }

    private void startParentTrackerService() {

        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();

            String driverId1 = (String) bundle.get("driverId1");
            String parentUID = (String) bundle.get("parentUID");

//            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//            editor.putString("driverId1", driverId1);
//            editor.putString("parentUID", parentUID);
//            editor.apply();

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("driverId1", driverId1).putString("parentUID", parentUID).apply();

            Intent intent = new Intent(this,MapsService2.class);
            startService(intent);

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startParentTrackerService();
        } else {
            finish();
        }
    }

}