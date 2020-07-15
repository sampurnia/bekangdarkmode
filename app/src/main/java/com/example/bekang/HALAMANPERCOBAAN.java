package com.example.bekang;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bekang.calonfragment.HALAMANMONITORING;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HALAMANPERCOBAAN extends AppCompatActivity implements PermissionsListener{

    public PermissionsManager permissionsManager;
    DatabaseReference reff, reffdate;
    TextView a,b,c,d;
    int FUEL, TEMP, WATER, PRESS;
    private MapView mapView;
    private MapboxMap mapboxMap;
    FirebaseDatabase database;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    GraphView graphView;
    LineGraphSeries series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtcHVybmlhIiwiYSI6ImNrYTZjcGFpZjA2dzUzMG13NGNxdHVldGEifQ.hJkKxmZcvSZisv4N4PvHLg");
        setContentView(R.layout.activity_halamanpercobaan);
        Mapbox.getInstance(this, getString(R.string.access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                HALAMANPERCOBAAN.this.mapboxMap = mapboxMap;

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(HALAMANPERCOBAAN.this, style);
                    }
                });
            }
        });
        refff();
        reffdate();
    }



    public void refff(){
        a=(TextView)findViewById(R.id.tvFuel3);
        b=(TextView)findViewById(R.id.tvTemp3);
        c=(TextView)findViewById(R.id.tvWater3);
        d=(TextView)findViewById(R.id.tvPress2);
        reff= FirebaseDatabase.getInstance().getReference().child("mobil1").child("sensor");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fuel = dataSnapshot.child("fuel").getValue().toString();
                String temp = dataSnapshot.child("temp").getValue().toString();
                String water = dataSnapshot.child("water").getValue().toString();
                String preasure = dataSnapshot.child("preasure").getValue().toString();
                a.setText(fuel);
                b.setText(temp);
                c.setText(water);
                d.setText(preasure);
                WATER = Integer.parseInt(c.getText().toString());
                ProgressBar progressBar1 = findViewById(R.id.progressBar);
                progressBar1.setProgress(WATER); //untuk mendapatkan nilainya
                FUEL = Integer.parseInt(a.getText().toString());
                ProgressBar progressBar2 = findViewById(R.id.progressBarFuel);
                progressBar2.setProgress(FUEL); //untuk mendapatkan nilainya
                TEMP = Integer.parseInt(b.getText().toString());
                ProgressBar progressBar3 = findViewById(R.id.progressBarTemp);
                progressBar3.setProgress(TEMP); //untuk mendapatkan nilainya
                PRESS = Integer.parseInt(d.getText().toString());
                ProgressBar progressBar4 = findViewById(R.id.progressBarPress);
                progressBar4.setProgress(PRESS); //untuk mendapatkan nilainya
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @SuppressWarnings( {"MissingPermission"})
    private static void enableLocationComponent(HALAMANPERCOBAAN mainActivity, @NonNull Style loadedMapStyle) {
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

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(HALAMANPERCOBAAN.this, style);
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


    public void reffdate() {
//        GraphView graphView = (GraphView) findViewById(R.id.graph);
//        series = new LineGraphSeries();
//        graphView.addSeries(series);
//        reffdate= FirebaseDatabase.getInstance().getReference().child("current").child("arus");
//        reffdate.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String y = dataSnapshot.child("arus").getValue().toString();
//                y = Integer.parseInt(getText().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        long x = new Date().getTime();
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
        series.setDrawBackground(true);
        graph.setBackgroundColor(0x713F51B5);//merubah warna background grafik
        graph.setTitle("GENSET");
        series.setBackgroundColor(0x70F44336);//merubah background series
        graph.setCursorMode(true);//agar bisa dipencet2
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);//ketebalan garis grafik
        paint.setColor(0xFFC90000);//merubah warna garis
        series.setCustomPaint(paint);
        graph.setTitleColor(0xFFC90000);//merubah warna judulnya gaes
    }
}
