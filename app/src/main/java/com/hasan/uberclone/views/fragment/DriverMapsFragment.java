package com.hasan.uberclone.views.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hasan.uberclone.R;
import com.hasan.uberclone.myConstants.MyConstants;

public class DriverMapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "DriverMapsFragment";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int LOCATION_REQUEST_CODE = 1001;
    private Context context;
    private Geocoder geocoder;
    private GoogleMap mMap;
    private String currentUserId;
    private  DatabaseReference driverRef;
    private LocationRequest locationRequest;
    private Marker userLocationMarker;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        driverRef= MyConstants.DB_REF.child("driverAvailable");

        geocoder = new Geocoder(context);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


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
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        GeoFire geoFire = new GeoFire(driverRef);
        geoFire.setLocation(
                currentUserId,
                new GeoLocation(location.getLatitude(), location.getLongitude()),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Log.d(TAG, "onComplete: complete");
                    }
                }
        );

        if (userLocationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));

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

/*    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.d(TAG, "onLocationResult: " + location.toString());

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Driver"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));


                GeoFire geoFire = new GeoFire(driverRef);
                geoFire.setLocation(
                        currentUserId,
                        new GeoLocation(location.getLatitude(), location.getLongitude()),
                        new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                Log.d(TAG, "onComplete: complete");
                            }
                        }
                );
            }
        }
    };*/


    @Override
    public void onStart() {
        super.onStart();
        startLocationUpdate();
    }



    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdate();


        GeoFire geoFire = new GeoFire(driverRef);
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