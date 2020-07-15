package com.example.bekang.calonfragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bekang.R;
import com.example.bekang.firebaseauth.HALAMANLOGIN;
import com.google.firebase.auth.FirebaseAuth;


/*Berisikan semua info dari bekang hanya berupa gambar gambar atau kalo gak males bikin aja di pdf ya put ehehe*/

public class HALAMANINFO extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamaninfo);
        firebaseAuth=FirebaseAuth.getInstance(); //untuk melakukan logout
    }

    private void Logout(){ //menu logout kalo di pencet
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HALAMANINFO.this, HALAMANLOGIN.class));
        //membuka class halaman login ketika sudah logout
    }
    private void Refresh(){
        Intent intent = new Intent(HALAMANINFO.this, HALAMANINFO.class);
        startActivity(intent);
        finish();
    }
    private void List(){
        Intent intent = new Intent(HALAMANINFO.this, HALAMANSTATUS.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    private void Navigation(){
        Intent intent = new Intent(HALAMANINFO.this, HALAMANUTAMA.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
            case R.id.ListMenu :
                List();
                break;
            case R.id.NavigationMenu :
                Navigation();
                break;
        }
        return super.onOptionsItemSelected(item); }
}
