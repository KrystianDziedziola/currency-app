package com.edu.uz.currency.currencyapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.rest.atm.GoogleClient;
import com.edu.uz.currency.currencyapp.rest.atm.model.MainResponse;
import com.edu.uz.currency.currencyapp.rest.atm.model.Result;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edu.uz.currency.currencyapp.R.string.atm;

public class AtmMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String TAG = "AtmMapActivity";
    private static final String LOCATION_PERMISSION_INFO = "Location permission denied. " +
            "Please turn ON permission and come back.";
    private static final int PERMS_REQUEST_CODE = 11;

    private double latitude;
    private double longitude;
    private GoogleMap googleMap;
    private Spinner atmSpinner;
    private Spinner radiusSpinner;
    private GoogleApiClient googleApiClient;
    private SupportMapFragment mapFragment;
    private Context context;

    private Marker mLocationMarker;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private String actualAtm;
    private String actualRadius;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_maps);
        context = getApplicationContext();

        setTitle(getString(R.string.atms_title));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        buildGoogleApiClient();
        createLocationRequest();
        initMap();
        initAtmSpinner();
        initRadiusSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGPSEnabled();
        setLocation();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    private void openLocationSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        AtmMapActivity.this.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private boolean isGooglePlayServicesAvailable() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    private void searchNearby(final String atmName, final int radius) {
        final GoogleClient googleClient = GoogleClient.FactoryGoogleClient.getGoogleClient();
        final Call<MainResponse> call = googleClient.getNearbyPlaces(
                context.getString(atm), atmName, latitude + "," + longitude,
                radius);

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(final Call<MainResponse> call,
                                   final Response<MainResponse> response) {
                try {
                    Log.d("URL", String.format("%s", response.raw().request().url()));

                    for (Result result : response.body().getResults()) {
                        final Double lat = result.getGeometry().getLocation().getLat();
                        final Double lng = result.getGeometry().getLocation().getLng();
                        final String placeName = result.getName();
                        final MarkerOptions markerOptions = new MarkerOptions();
                        final LatLng latLng = new LatLng(lat, lng);

                        markerOptions.position(latLng);
                        markerOptions.title(placeName);
                        mLocationMarker = googleMap.addMarker(markerOptions);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(final Call<MainResponse> call, final Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    @Override
    public void onLocationChanged(final Location location) {
        mLastLocation = location;
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void setLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient);
        }

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnected(final Bundle bundle) {
        setLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(final int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please reconnect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please reconnect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String permissions[],
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PERMS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, LOCATION_PERMISSION_INFO, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMS_REQUEST_CODE);

            return false;
        } else {
            return true;
        }
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initAtmSpinner() {
        atmSpinner = (Spinner) findViewById(R.id.atmSpinner);
        atmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent,
                                       final View view,
                                       final int position,
                                       final long id) {
                actualAtm = parent.getItemAtPosition(position).toString();
                if (actualAtm.equals(context.getString(R.string.wybierz))) {
                    googleMap.clear();
                    return;
                }
                if (actualAtm.equals(context.getString(R.string.AllATM))) {
                    googleMap.clear();
                    final String[] allNames = context.getResources().getStringArray(R.array.ATMs);
                    for (final String atmName : allNames) {
                        searchNearby(atmName, Integer.parseInt(actualRadius));
                    }
                    searchNearby(context.getString(atm), Integer.parseInt(actualRadius));
                } else {
                    googleMap.clear();
                    searchNearby(actualAtm, Integer.parseInt(actualRadius));
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    private void initRadiusSpinner() {
        radiusSpinner = (Spinner) findViewById(R.id.radiusSpinner);
        radiusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent,
                                       final View view,
                                       final int position,
                                       final long id) {
                actualRadius = parent.getItemAtPosition(position).toString();
                actualRadius = actualRadius.substring(0, actualRadius.length() - 2);
                actualRadius += "000";
                googleMap.clear();
                searchNearby(actualAtm, Integer.parseInt(actualRadius));
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
        }
    }

    private void checkGPSEnabled() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gps_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Lokalizacja jest wyłączona");
            dialog.setPositiveButton("Włącz",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openLocationSettings();
                        }
                    });
            dialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.atm_main_layout),
                                    "Bez lokalizacji funkcja może nie działać poprawnie",
                                    Snackbar.LENGTH_LONG);
                            snackbar.setAction("Włącz", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openLocationSettings();
                                }
                            });
                            snackbar.show();
                        }
                    });
            dialog.show();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10);
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    public static void start(final Activity activity) {
        Intent intent = new Intent(activity, AtmMapActivity.class);
        activity.startActivity(intent);
    }
}