package com.example.trackingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.trackingapp.directionhelpers.FetchURL;
import com.example.trackingapp.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    private static final int PERMISSIONS_REQUEST = 1;
    private GoogleMap mMap;
    private Polyline currentPolyline;
    private MarkerOptions options;
    private ArrayList<LatLng> arrlatlngs;
    private android.graphics.Color Color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMaxZoomPreference(16);

        subscribeToDriverUpdates();
       // subscribeToParentUpdates();

//        final Button button = (Button) findViewById(R.id.checkout_button);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                subscribeToUpdates();
//            }
//        });
//
//        button.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        mMap.setPadding(0, button.getHeight(), 0, 0);
//                    }
//                }
//        );
//
//        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
//            finish();
    }

    // Check location permission is granted - if it is, start
    // the service, otherwise request the permission
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//
//            LocationRequest request = new LocationRequest();
//            request.setInterval(10000);
//            request.setFastestInterval(5000);
//            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
//            client.requestLocationUpdates(request, new LocationCallback() {
//                        @Override
//                        public void onLocationResult(LocationResult locationResult) {
//                            Location location = locationResult.getLastLocation();
//                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//
//
//                            mMap.addMarker(new MarkerOptions().title("Parent").position(ll));
//
////                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
////                            for (Marker marker : mMarkers.values()) {
////                                builder.include(marker.getPosition());
////                            }
////                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
//
//                            mBounds.include(ll);
//
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
//                                    findViewById(R.id.checkout_button).getHeight()));
//
//
//                            // addPointToViewPort(ll);
//                        }
//            },null);
////            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
////                @Override
////                public void onMyLocationChange(Location location) {
////                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
////                    addPointToViewPort(ll);
////                    // we only want to grab the location once, to allow the user to pan and zoom freely.
////                    mMap.setOnMyLocationChangeListener(null);
////                }
////            });
//
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST);
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
//            grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // Start the service when the permission is granted
//
//            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                @Override
//                public void onMyLocationChange(Location location) {
//                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                    addPointToViewPort(ll);
//                    // we only want to grab the location once, to allow the user to pan and zoom freely.
//                    mMap.setOnMyLocationChangeListener(null);
//                }
//            });
//
//        } else {
//            finish();
//        }
//    }
    // mMap.setMyLocationEnabled(true);

//    private void addPointToViewPort(LatLng newPoint) {
//        mBounds.include(newPoint);
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
//                findViewById(R.id.checkout_button).getHeight()));


    private void subscribeToParentUpdates() {

        String parentUID = PreferenceManager.getDefaultSharedPreferences(this).getString("parentUID", "");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users" + "/" + parentUID + "/" + "locations");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot2, String previousChildName) {
                fetchParent(dataSnapshot2);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot2, String previousChildName) {
                fetchParent(dataSnapshot2);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot2, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot2) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void subscribeToDriverUpdates() {

//            Intent intent = getIntent();
//            Bundle bundle = intent.getExtras();

//            if (bundle != null && bundle.containsKey("driverId2")) {
//                String driverId1 = (String) bundle.get("driverId1");
//                String driverId2 = (String) bundle.get("driverId2");

      //  String parentUID = PreferenceManager.getDefaultSharedPreferences(this).getString("parentUID", "");
        String driverId1 = PreferenceManager.getDefaultSharedPreferences(this).getString("driverId1", "");
        String driverId2 = PreferenceManager.getDefaultSharedPreferences(this).getString("driverId2", "");

        Log.d("---ALERT---","Inside subscribeToDriverUpdates()");

        if (driverId1.equals(driverId2)) {

            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("users" + "/" + driverId1 + "/" + "locations");
            ref1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot1, String previousChildName) {
                    fetchDriver(dataSnapshot1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot1, String previousChildName) {
                    fetchDriver(dataSnapshot1);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot1, String previousChildName) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot1) {
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.d(TAG, "Failed to read value.", error.toException());
                }
            });
        }
        //else()
        // }
        else {
//                String driverId1 = (String) bundle.get("driverId1");
//
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users" + "/" + driverId1 + "/" + "locations");
//                ref.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                        setMarker(dataSnapshot);
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                        setMarker(dataSnapshot);
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        Log.d(TAG, "Failed to read value.", error.toException());
//                    }
//                });
            Log.d("__ALERT__", "DriverId1 and DriverId2 are different");
        }

    }

    private void fetchDriver(DataSnapshot dataSnapshot1) {
       // String key1 = dataSnapshot1.getKey();

        HashMap<String, Object> value1 = (HashMap<String, Object>) dataSnapshot1.getValue();

       // double lat1 = Double.parseDouble(value1.get("latitude").toString());
       // double lng1 = Double.parseDouble(value1.get("longitude").toString());

       String driverlat = value1.get("latitude").toString();
       String driverlng = value1.get("longitude").toString();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("driverlat", driverlat).putString("driverlng", driverlng).apply();

      //  LatLng location = new LatLng(lat, lng);
     //   LatLng location2 = null;

//        if (!mMarkers.containsKey(key)) {
//            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
//        } else {
//            mMarkers.get(key).setPosition(location);
//        }

     //   zoomMapInitial(location1,location2);

//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (Marker marker : mMarkers.values()) {
//                builder.include(marker.getPosition());
//            }
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
        subscribeToParentUpdates();

    }

    private void fetchParent(DataSnapshot dataSnapshot2) {
        //   String key2 = dataSnapshot2.getKey();

        HashMap<String, Object> value2 = (HashMap<String, Object>) dataSnapshot2.getValue();

//        double lat2 = Double.parseDouble(value2.get("latitude").toString());
//        double lng2 = Double.parseDouble(value2.get("longitude").toString());

        //  LatLng location2 = new LatLng(lat2, lng2);
        //   LatLng location1 = null;

        String parentlat = value2.get("latitude").toString();
        String parentlng = value2.get("longitude").toString();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("parentlat", parentlat).putString("parentlng", parentlng).apply();

        zoomMapInitial();

    }

    private BitmapDescriptor bitmapDescriptorFromVector1(Context context, @DrawableRes  int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_bus);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector2(Context context, @DrawableRes  int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_person);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    protected void zoomMapInitial() {
        try {
            int padding = 200;// Space (in px) between bounding box edges and view edges (applied to all four sides of the bounding box)
            LatLngBounds.Builder bc = new LatLngBounds.Builder();

          String driverlat =  PreferenceManager.getDefaultSharedPreferences(this).getString("driverlat", "");
          String driverlng =  PreferenceManager.getDefaultSharedPreferences(this).getString("driverlng", "");
          String parentlat =  PreferenceManager.getDefaultSharedPreferences(this).getString("parentlat", "");
          String parentlng =  PreferenceManager.getDefaultSharedPreferences(this).getString("parentlng", "");

          Double driverlat1 = Double.parseDouble(driverlat);
          Double driverlng1 = Double.parseDouble(driverlng);
          Double parentlat1 = Double.parseDouble(parentlat);
          Double parentlng1 = Double.parseDouble(parentlng);



            LatLng driverlocation = new LatLng(driverlat1, driverlng1);
            LatLng parentlocation = new LatLng(parentlat1, parentlng1);

//             options = new MarkerOptions();
//
//             arrlatlngs = new ArrayList<>();
//
//            arrlatlngs.add(driverlocation);
//            arrlatlngs.add(parentlocation);
//
//            for (LatLng point : arrlatlngs) {
//
//                options.position(point);
//                options.title("Title");
//               // options.icon(bitmapDescriptorFromVector(this, R.drawable.ic_bus));
//
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//                mMap.addMarker(options);
//            }

            bc.include(driverlocation);
            bc.include(parentlocation);

            mMap.clear();
          //  mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), padding));
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), padding));
//             LatLngBounds latLngBounds = new LatLngBounds(
//                    driverlocation, parentlocation);
//            mMap.setLatLngBoundsForCameraTarget(latLngBounds);

            mMap.addMarker(new MarkerOptions()
                    .position(driverlocation)
                    .title("Driver")
                    .snippet("This is the driver")
                    .draggable(true)

                 //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus)));
                  //  .rotation((float) 33.5)
                    .icon(bitmapDescriptorFromVector1(this, R.drawable.ic_bus)));
                 //   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

            mMap.addMarker(new MarkerOptions()
                    .position(parentlocation)
                    .title("Parent")
                    .snippet("This is you")
                    .draggable(true)
                  //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person)));
                    //  .rotation((float) 33.5)
                    .icon(bitmapDescriptorFromVector2(this, R.drawable.ic_person)));

           // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            String url = getUrl(driverlocation,parentlocation,"driving");

            new FetchURL(this).execute(url, "driving");



        } catch (Exception e) {
            e.printStackTrace();
        }



//        if (!mMarkers.containsKey(key2)) {
//            mMarkers.put(key2, mMap.addMarker(new MarkerOptions().title(key2).position(location2)));
//        } else {
//            mMarkers.get(key2).setPosition(location2);
//        }

     //   zoomMapInitial(location1,location2);

//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (Marker marker : mMarkers.values()) {
//                builder.include(marker.getPosition());
//            }
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
   // }


//    protected void zoomMapInitial(LatLng location1, LatLng location2) {
//        try {
//            int padding = 200; // Space (in px) between bounding box edges and view edges (applied to all four sides of the bounding box)
//            LatLngBounds.Builder bc = new LatLngBounds.Builder();
//
//            bc.include(location1);
//            bc.include(location2);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), padding));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}