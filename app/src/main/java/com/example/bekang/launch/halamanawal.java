package com.example.bekang.launch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.bekang.R;
import com.example.bekang.firebaseauth.HALAMANLOGIN;

public class halamanawal extends AppCompatActivity {
Button mulai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_halamanawal);
        getSupportActionBar().hide();
        halamanawal.LogoLauncherr logoLauncher = new LogoLauncherr();
        logoLauncher.start();
//        mulai = (Button) findViewById(R.id.Mulai);
//
//        mulai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i= new Intent(getApplicationContext(), HALAMANKEDUA.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }
 class LogoLauncherr extends Thread {
        public void run (){
            try {
                int SLEEP_TIMER = 2;
                sleep(500* SLEEP_TIMER);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(halamanawal.this, HALAMANKEDUA.class);
            startActivity(intent);
            halamanawal.this.finish();


        }
    }
}