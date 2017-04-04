package com.edu.uz.currency.currencyapp.atm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.edu.uz.currency.currencyapp.MainActivity;
import com.edu.uz.currency.currencyapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class AtmMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOCATION_PERMISSION_INFO = "Location Permissions denied.";
    private static final String GOOGLE_SERVICE_INFO = "Cant connect to play services.";
    private static final int PERMS_REQUEST_CODE = 11;

    private GoogleMap googleMap;
    private Spinner atmSpinner;
    private MapFragment mapFragment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        final String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};

        if (Build.VERSION.SDK_INT < 23) {
            initialize();
        } else {
            if (hasPermissions(context, PERMISSIONS)) {
                initialize();
            } else {
                requestPerms(PERMISSIONS);
            }
        }
    }

    private void initMap() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initAtmSpinner() {
        atmSpinner = (Spinner) findViewById(R.id.atmSpinner);
        atmSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());
    }

    private void searchNearby(final String atmAndBankName) {
        //TODO: szukanie w pobliżu
        Toast.makeText(this, atmAndBankName, Toast.LENGTH_SHORT).show();
    }

    private boolean googleServiceAvailable() {
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            api.getErrorDialog(this, isAvailable, 0).show();
        } else {
            Toast.makeText(this, GOOGLE_SERVICE_INFO, Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMapReady(final GoogleMap mGoogleMap) {
        googleMap = mGoogleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final String atm = parent.getItemAtPosition(position).toString();
            if (atm.equals(context.getString(R.string.AllATM))) {
                searchNearby("atm");
            } else
                searchNearby("atm " + parent.getItemAtPosition(position).toString());
        }

        public void onNothingSelected(AdapterView parent) {
        }
    }

    private boolean hasPermissions(final Context context, final String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (final String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context,
                        permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPerms(final String... permissions) {
        ActivityCompat.requestPermissions(AtmMapActivity.this, permissions,
                PERMS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PERMS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialize();
                } else {
                    this.finish();
                    Toast.makeText(this, LOCATION_PERMISSION_INFO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initialize(){
        if (googleServiceAvailable()) {
            setContentView(R.layout.activity_atm_maps);
            initMap();
            initAtmSpinner();
        }
    }
}