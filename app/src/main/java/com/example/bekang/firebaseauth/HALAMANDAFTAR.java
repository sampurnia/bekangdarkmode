package com.example.bekang.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bekang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*HALAMAN INI adalah halaman registrasi untuk melakukan registrasi ke aplikasi untuk
disimpan menggunakan firebase
NOTE : SEMUA JENIS DATA DI FIREBASE BERUPA STRING*/

public class HALAMANDAFTAR extends AppCompatActivity {
    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin, forgotPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamandaftar);
        setupUIViews(); //deklarasi segala jenis button, text dkk yang ada di program

        /*mendeklarikan firebase auth untuk entry data ke firebase*/
        firebaseAuth= FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) /*untuk menvalidasi bahwa semua berbentuk string, program dibawah*/
                {
                    /*ini di dalam database nya, mesend "plain text"/"edit text" untuk diatur sebagai
                    string kemudian masuk ke firebase auth*/
                    String user_email = userEmail.getText().toString().trim();
                    String user_password =userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(HALAMANDAFTAR.this, "Successfully Registered, Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                                sendEmailVerification(); // firebase mengirimkan email verifikasi, untuk lebih jelasnya cari email verification

                            }
                            else {
                                Toast.makeText(HALAMANDAFTAR.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HALAMANDAFTAR.this, HALAMANLOGIN.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                /*saat daftar sudah berhasil dan ingin kembali ke halaman login*/
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HALAMANDAFTAR.this, HALAMANPASSWORD.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

    }
    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUserName);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        forgotPassword = (TextView)findViewById(R.id.tvUserPass);
    }

    private Boolean validate(){
        /*untuk memvalidasi segala jenis bentuk input adalah string*/
      Boolean result = false;
      String name = userName.getText().toString();
      String password = userPassword.getText().toString();
      String email = userEmail.getText().toString();

      if(name.isEmpty() || password.isEmpty() || email.isEmpty())
      {
          /*apabila beberapa bagian ada yang kosong maka akan memunculkan tulisan*/
          Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
      }
      else {
          result = true ;
      }
      return result;
    }

    private void sendEmailVerification(){
        /*program agar si firebase mengirimkan email verifikasi ke dalam email yang di daftarkan*/
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                 Toast.makeText(HALAMANDAFTAR.this, "Successfully Registered, Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                 firebaseAuth.signOut();
                 finish();
                 startActivity(new Intent(HALAMANDAFTAR.this, HALAMANLOGIN.class));
               }
               else {
                   Toast.makeText(HALAMANDAFTAR.this, "Verification email hasn't been sent!", Toast.LENGTH_SHORT).show();

               }
                }
            });
        }

    }
}
