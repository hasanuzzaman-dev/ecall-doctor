package com.hasan.uberclone.views.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hasan.uberclone.R;
import com.hasan.uberclone.databinding.FragmentCustomerMapsBinding;
import com.hasan.uberclone.myConstants.MyConstants;

import java.util.HashMap;
import java.util.List;

public class CustomerMapsFragment extends Fragment implements OnMapReadyCallback{


    private static final String TAG = "DriverMapsFragment";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int LOCATION_REQUEST_CODE = 1001;
    private Context context;
    private Geocoder geocoder;
    private GoogleMap mMap;
    private String currentUserId;
    private DatabaseReference customerRequestRef;
    private LocationRequest locationRequest;
    private Marker userLocationMarker;
    private FragmentCustomerMapsBinding binding;
    private Location lastLocation;
    private LatLng pickupLatLng;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_customer_maps, container, false);
        binding = FragmentCustomerMapsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        customerRequestRef= MyConstants.DB_REF.child("customerRequest");

        geocoder = new Geocoder(context);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        binding.requestUberBtn.setOnClickListener(view1 -> {
            GeoFire geoFire = new GeoFire(customerRequestRef);
            geoFire.setLocation(
                    currentUserId,
                    new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            Log.d(TAG, "lastLocation: latitude "+lastLocation.getLatitude()+" longitude "+lastLocation.getLongitude());
                        }
                    }
            );

            pickupLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pickupLatLng).title("Pickup here"));
            binding.requestUberBtn.setText("Getting your Driver.........");

            getClosestDriver();
        });


    }

    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundId;

    private void getClosestDriver() {
        DatabaseReference driverAvailableRef = MyConstants.DB_REF.child("driverAvailable");
        GeoFire geoFire = new GeoFire(driverAvailableRef);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()),radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound){
                    driverFound = true;
                    driverFoundId = key;

                    // Tell the driver which customer you must pickup
                    DatabaseReference driverFoundRef = MyConstants.DB_REF.child("RegisteredUserId").child("driver").child(driverFoundId);
                    HashMap map = new HashMap();
                    map.put("customerRideId",currentUserId);
                    driverFoundRef.updateChildren(map);

                    showDriverLocation();
                    binding.requestUberBtn.setText("Looking for driver location........");
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound){
                    radius++;
                    getClosestDriver();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private Marker driverMarker;

    //Show driver location customer map
    private void showDriverLocation() {

        DatabaseReference driverLocationRef = MyConstants.DB_REF.child("driverWorking").child(driverFoundId).child("l");
        driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double driverLat = 0.0;
                    double driverLng = 0.0;
                    binding.requestUberBtn.setText("Driver Found");

                    if (map.get(0) != null){
                        driverLat = Double.parseDouble(map.get(0).toString());
                    }if (map.get(1) != null){
                        driverLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng = new LatLng(driverLat,driverLng);

                    if (driverMarker != null){
                        driverMarker.remove();
                    }

                    driverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your Driver"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            //zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: You should show an alert dialog........");
                new AlertDialog.Builder(context)
                        .setMessage("We need to permission for location!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(
                                        requireActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_REQUEST_CODE
                                );
                            }
                        }).show();

            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (mMap != null){
                lastLocation = locationResult.getLastLocation();
                //setUserLocationMarker(lastLocation);
                setCameraToUser(lastLocation);
            }
        }
    };

    private void setCameraToUser(Location lastLocation) {
        LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
    }

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        /*GeoFire geoFire = new GeoFire(customerRequestRef);
        geoFire.setLocation(
                currentUserId,
                new GeoLocation(location.getLatitude(), location.getLongitude()),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Log.d(TAG, "onComplete: complete");
                    }
                }
        );*/

        if (userLocationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
           // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));

            // direction of image
            //markerOptions.rotation(location.getBearing());

            //location sign middle of the image
            // markerOptions.anchor((float) 0.5, (float) 0.5);

            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));

        }else {
            userLocationMarker.setPosition(latLng);

            // direction of image
            //userLocationMarker.setRotation(location.getBearing());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        startLocationUpdate();
    }



    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdate();


        GeoFire geoFire = new GeoFire(customerRequestRef);
        geoFire.removeLocation(currentUserId, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Log.d(TAG, "onComplete: Stopped");
            }
        });
    }



    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                //zoomToUserLocation();
            } else {
                //Permission is not Granted
                if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //This block here means PERMANENTLY DENIED PERMISSION
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setMessage("You have permanently denied this permission, go to settings to enable this permission")
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    gotoApplicationSettings();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .setCancelable(false)
                            .show();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }


    private void gotoApplicationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}