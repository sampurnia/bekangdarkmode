package com.example.bekang.calonfragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bekang.R;
import com.example.bekang.firebaseauth.HALAMANLOGIN;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.intellij.lang.annotations.JdkConstants;

/* HALAMAN INI BERISIKAN STATUS MOBIL, APAKAH MOBIL TERSEBUT ON/OFF/MAINTENANCE
NOTE : SEMUA JENIS DATA DI FIREBASE BERUPA STRING
MEMBUAT VERSI DARK MODE NYA
*/

public class HALAMANSTATUS extends AppCompatActivity {
    /*deklarasi*/
    private FirebaseAuth firebaseAuth;
    private Button durlapOn;
    private Button durlapMain;
    private Button durlapOFF;
    DatabaseReference refstatus;

    private Toolbar mTopToolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanstatus);
        firebaseAuth=FirebaseAuth.getInstance();

        /*deklarasi interface yang ada di halaman ini*/
        durlapOn = (Button) findViewById(R.id.btnDurlap);
        durlapMain = (Button) findViewById(R.id.btnDurlap2);
        durlapOFF = (Button) findViewById(R.id.btnDurlap3);

//        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" +"STATUS"+ "</font>"));
//        getSupportActionBar().setTitle(Html.fromHtml("<font style = \"@font/orbitron \">" + "STATUS" + "</font>"));
        // nyoba ganti warna Title di atas gaesss uwuwuuwuwu

        /*program untuk menretrieve data berisikan data di firebase, mobil1 adalah child pertama,
        status adalah child kedua
        child terakhir "stat" berisikan status si mobil*/
        refstatus= FirebaseDatabase.getInstance().getReference().child("mobil1").child("status");
        refstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status;
                if(dataSnapshot.child("stat").getValue() != null) {
                    status = dataSnapshot.child("stat").getValue().toString();

                    switch (status){
                        case "1":
                            StatusOn(); /*apabila di database nilainya 1 maka dianggap on*/
                            break;
                        case "2":
                            StatusMain();
                            break;
                        case "3":
                            StatusOFF();
                            break;
                        default:
                            Toast.makeText(HALAMANSTATUS.this, "error", Toast.LENGTH_LONG).show();
                            break; /*apabila nilai di firebase tidak 1/2/3*/
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    public void StatusOn(){
        /*program untuk menyalakan animasi online*/
        durlapOn.setBackgroundResource(R.drawable.bggreen);
        AnimationDrawable animationON = (AnimationDrawable) durlapOn.getBackground();
        //Tombolnya ada 3 di tumpuk gitu, hanya di set visibility nya saja
        durlapOFF.setVisibility(View.GONE);
        durlapMain.setVisibility(View.GONE);
        durlapOn.setVisibility(View.VISIBLE);
        animationON.start();
        Toast.makeText(HALAMANSTATUS.this, "ONLINE", Toast.LENGTH_SHORT).show();

        durlapOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), HALAMANMONITORING.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void StatusMain(){
        /*program untuk menyalakan animasi maintenance*/
        durlapMain.setBackgroundResource(R.drawable.bgyellow);
        AnimationDrawable animationMAIN = (AnimationDrawable) durlapMain.getBackground();
        durlapMain.setVisibility(View.VISIBLE);
        durlapOFF.setVisibility(View.GONE);
        durlapOn.setVisibility(View.GONE);
        animationMAIN.start();
        Toast.makeText(HALAMANSTATUS.this, "MAINTENANCE", Toast.LENGTH_SHORT).show();

        durlapMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), HALAMANMONITORING.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void StatusOFF(){

        /*program untuk menyalakan animasi offline*/
        durlapOFF.setBackgroundResource(R.drawable.bgred);
        AnimationDrawable animationOFF = (AnimationDrawable) durlapOFF.getBackground();
        durlapOFF.setVisibility(View.VISIBLE);
        durlapMain.setVisibility(View.GONE);
        durlapOn.setVisibility(View.GONE);
        animationOFF.start();
        Toast.makeText(HALAMANSTATUS.this, "OFFLINE", Toast.LENGTH_SHORT).show();

        durlapOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), HALAMANMONITORING.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HALAMANSTATUS.this, HALAMANLOGIN.class));
    }
    private void Refresh(){
        Intent intent = new Intent(HALAMANSTATUS.this, HALAMANSTATUS.class);
        startActivity(intent);
        finish();
    }
    private void Info(){
        Intent intent = new Intent(HALAMANSTATUS.this, HALAMANINFO.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
    private void Navigation(){
        Intent intent = new Intent(HALAMANSTATUS.this, HALAMANUTAMA.class);
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
        switch (item.getItemId()){
            case R.id.LogoutMenu :
                Logout();
                break;
            case R.id.RefreshMenu :
                Refresh();
                break;
            case R.id.InfoMenu :
                Info();
                break;
            case R.id.NavigationMenu :
                Navigation();
                break;
        }
        return super.onOptionsItemSelected(item); }

}
