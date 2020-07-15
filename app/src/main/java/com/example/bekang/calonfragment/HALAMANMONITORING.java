package com.example.bekang.calonfragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bekang.HALAMANPERCOBAAN;
import com.example.bekang.R;
import com.example.bekang.firebaseauth.HALAMANLOGIN;
import com.google.firebase.auth.FirebaseAuth;
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

/*
INI SEHARUSNYA DIJADIIN FRAGMENT TIAP TRUCK
PADA HALAMAN INI BERISIKAN SEMUA DATA SENSOR
GPS : LOKASI MOBIL
DATA : TEMPERATURE, FUEL LEVEL, WATER LEVEL, PREASSURE(SEMENTARA)
GRAFIK : (WAKTU, ARUS)
NOTE : SEMUA JENIS DATA DI FIREBASE BERUPA STRING

PERCOBAAN GPS =
[MASALAHNYA TERDAPAT PADA PLATFORM PENYEDIA MAPS NYA, DI FIREBASE BISA MENAMBAHKAN NILAI GEOPOINT TETEAPI
UNTUK GET DATANYA DI TAMPILKAN KEDALAM MAPBOX BELUM DI COBA KARENA GEOPOINT TIDAK DIDAPATKAN DARI STRING(REALTIME
DATABASE) MELAINKAN DARI CLOUD FIRESTORE](SOLUSI SEMENTARA : membuat maps custom dari mapbox jadi mengharuskan
si satelit mengirim data ke mapbox dalam bentuk json yang berisikan data mobil serta geopointnya contoh
{
  "features": [
    {
      "type": "Feature",
      "properties": {
        "description": "3998 Gramercy St, Columbus, Ohio 43219, United States",
        "name": "Easton",
        "hours": "11 a.m. to 11 p.m.",
        "phone": "614-476-5364"
      },
      "geometry": {
        "coordinates": [
          -82.917665,
          40.051791
        ],
        "type": "Point"
      },
      "id": "address.11780104298749790"
    },
    {
      "type": "Feature",
      "properties": {
        "description": "714 N High St, Columbus, Ohio 43215, United States",
        "name": "North Market",
        "hours": "10 a.m. to 5 p.m.",
        "phone": "614-228-9960"
      },
      "geometry": {
        "coordinates": [
          -83.00418,
          39.971888
        ],
        "type": "Point"
      },
      "id": "address.1504101080777130"
    }
  "type": "FeatureCollection"
})
scr : https://docs.mapbox.com/help/tutorials/android-store-locator/#mapactivity

PERCOBAAN GRAFIK =
PERCOBAAN PERTAMA : MEMBUAT GRAFIK ARUS DENGAN CARA MEMASUKKAN NILAI Y NYA SAJA MELALUI EDITTEXT(BERHASIL)
PERCOBAAN KEDUA : MEMBUAT GRAFIK ARUS DENGAN CARA GET DATA SAJA DARI FIREBASE(GAGAL)
[MASALAHNYA ADA DI RULES FIREBASE, NILAI X DIDAPATKAN DARI NILAI WAKTU, MAKA MAU TIDAK MAU DARI SISI
HARDWARE/SOFTWARE HARUS MEMBUAT DATA WAKTU(X) KETIKA NILAI Y(ARUS) DI DAPATKAN, KELEMAHAN KEDUA YAITU
PENYIMPANAN FIREBASE TERBATAS, SEHINGGA DATA DIBAWAH 10 DETIK DIHARUSKAN UNTUK DIHAPUS AGAR TIDAK TERJADI
PENUMPUKAN DATA DI FIREBASE(SUDAH DICOBA DENGAN MENGGANTI RULES MENJADI HAPUS SETIAP 10000MS)]


*/


public class HALAMANMONITORING extends AppCompatActivity implements PermissionsListener {

    public MapboxMap mapboxMap;
    DatabaseReference reff, reffdate; //reffdate sebagai uji get & push data pada grafik firebase
    TextView a,b,c,d;
    int FUEL, TEMP, WATER, PRESS;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    FirebaseDatabase database;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss"); //untuk nilai x perbandingan waktu terhadapa arus
    GraphView graphView;
    LineGraphSeries series;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2FtcHVybmlhIiwiYSI6ImNrYTZjcGFpZjA2dzUzMG13NGNxdHVldGEifQ.hJkKxmZcvSZisv4N4PvHLg");
        setContentView(R.layout.activity_halamanmonitoring);

        firebaseAuth = FirebaseAuth.getInstance();
        Mapbox.getInstance(this, getString(R.string.access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                HALAMANMONITORING.this.mapboxMap = mapboxMap;

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(HALAMANMONITORING.this, style);
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
    private static void enableLocationComponent(HALAMANMONITORING mainActivity, @NonNull Style loadedMapStyle) {
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
                    enableLocationComponent(HALAMANMONITORING.this, style);
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
        /* PERCOBAAN PERTAMA KETIKA DI START AUTO MEMUNCULKAN NILAI GRAFIK
            reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int)dataSnapshot.getChildrenCount()];
                int index = 0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
                    Log.d("X Val",String.valueOf(pointValue.getxValue()));
                    Log.d("Y Val",String.valueOf(pointValue.getyValue()));
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    */
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



    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HALAMANMONITORING.this, HALAMANLOGIN.class));
    }

    private void Refresh() {
        refff();
        reffdate();
    }
    public void reffdate() {
        /* PERCOBAAN PERTAMA MENGGUNAKAN EDIT TEXT
        yValue = (EditText) findViewById(R.id.y_value); // belum ada di xml filenya, karena memang dihapus hanya
                                                           sebagai percobaan
        btn_insert = (Button) findViewById(R.id.btn_insert);// belum ada di xml filenya, karena memang dihapus hanya
                                                           sebagai percobaan
        graphView = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries();
        graphView.addSeries(series);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("chartTable");

        setListeners();

        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }

            }
        });
*/
        // PERCOBAAN KEDUA HANYA GET DATA TANPA HARUS INPUT DATA
        //        GraphView graphView = (GraphView) findViewById(R.id.graph);
//        series = new LineGraphSeries();
//        graphView.addSeries(series);
//        reffdate= FirebaseDatabase.getInstance().getReference().child("current").child("arus");
//        reffdate.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 DataPoint[] dp = new DataPoint[(int)dataSnapshot.getChildrenCount()];
//                  int index = 0;
//            for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
//                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
//                    dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
//                    Log.d("X Val",String.valueOf(pointValue.getxValue()));
//                    Log.d("Y Val",String.valueOf(pointValue.getyValue()));
//                    index++;
//                }
//                series.resetData(dp);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        //GRAFIK SEMENTARA KARENA GRAFIK YANG BERDASARKAN WAKTU MASIH BELUM BISA SET DI RULES NYA
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 2),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 4),
                new DataPoint(5, 3),
                new DataPoint(6, 4),
                new DataPoint(7, 3)
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

  /*  PERCOBAAN KEDUA MENGGUNAKAN EDIT TEXT(INPUT DATA KE GRAFIK HANYA NILAI Y)
  private void setListeners() {
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = reference.push().getKey(); // ini untuk masuk ke firebasenya

                long x = new Date().getTime(); //memasukkan nilai x secara otomatis ke dalam firebase
                int y = Integer.parseInt(yValue.getText().toString());

                PointValue pointValue = new PointValue(x, y);

                reference.child(id).setValue(pointValue);
            }
        });
    } */

    private void List() {
        Intent intent = new Intent(HALAMANMONITORING.this, HALAMANSTATUS.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void Info() {
        Intent intent = new Intent(HALAMANMONITORING.this, HALAMANINFO.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void Navigation() {
        Intent intent = new Intent(HALAMANMONITORING.this, HALAMANUTAMA.class);
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
            case R.id.NavigationMenu:
                Navigation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
