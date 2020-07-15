package com.example.bekang.calonfragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.bekang.R;
import com.example.bekang.firebaseauth.HALAMANLOGIN;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

/*HALAMAN UTAMA HANYA BERISIKAN LOKASI UTAMA SI TRUCK/MOBIL UNTUK METRACKING SEMUA PERGERAKAN
NOTE : SEMUA JENIS DATA DI FIREBASE BERUPA STRING*/

public class HALAMANUTAMA extends AppCompatActivity implements PermissionsListener{

    private FirebaseAuth firebaseAuth;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private BottomNavigationView menu_bawah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*mendeklarasikan API Key dari Mapbox yang di dapatkan dari mapbox studio*/
        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtcHVybmlhIiwiYSI6ImNrYTZjcGFpZjA2dzUzMG13NGNxdHVldGEifQ.hJkKxmZcvSZisv4N4PvHLg");
        setContentView(R.layout.activity_halamanutama);

        /*mendeklarasikan firebase untuk melakukan logout*/
        firebaseAuth = FirebaseAuth.getInstance();

        /*mendapatkan access token untuk mengakses mapbox*/
        Mapbox.getInstance(this, getString(R.string.access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        /*Mendapatkan halaman maps di halaman utama*/
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                HALAMANUTAMA.this.mapboxMap = mapboxMap;

                /*mendapatkan syle mapbox, untuk melihat style2 nya buka aja mapbox studio*/
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                enableLocationComponent(HALAMANUTAMA.this, style);
                                /*meminta/merequest ijin untuk mengaktifkan lokasi/gps pada hp untuk
                                 mengetahui lokasi saat ini */
                            }
                        });
            }
        });
    }

    /*penjelasan terlampir*/
    @SuppressWarnings( {"MissingPermission"})
    private static void enableLocationComponent(HALAMANUTAMA mainActivity, @NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(mainActivity)) {
// Get an instance of the component
            LocationComponent locationComponent = mainActivity.mapboxMap.getLocationComponent();
// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(mainActivity, loadedMapStyle).build());
// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            mainActivity.permissionsManager = new PermissionsManager(mainActivity);
            mainActivity.permissionsManager.requestLocationPermissions(mainActivity);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    /*melakukan get style dari mapbox*/
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(HALAMANUTAMA.this, style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HALAMANUTAMA.this, HALAMANLOGIN.class));
    }

    private void Refresh() {
        Intent intent = new Intent(HALAMANUTAMA.this, HALAMANUTAMA.class);
        startActivity(intent);
        finish();
    }

    private void List() {
        Intent intent = new Intent(HALAMANUTAMA.this, HALAMANSTATUS.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void Info() {
        Intent intent = new Intent(HALAMANUTAMA.this, HALAMANINFO.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogoutMenu:
                Logout();
                break;
            case R.id.RefreshMenu:
                Refresh();
                break;
            case R.id.ListMenu:
                List();
                break;
            case R.id.InfoMenu:
                Info();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
