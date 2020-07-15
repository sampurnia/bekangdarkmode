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
import com.google.firebase.auth.FirebaseAuth;

public class HALAMANPASSWORD extends AppCompatActivity {
    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;
    private TextView userRegistration, userLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanpassword);
        passwordEmail =(EditText)findViewById(R.id.etPasswordEmail);
        resetPassword = (Button)findViewById(R.id.btnPasswordReset);
        userLogin = (TextView)findViewById(R.id.tvUserMasuk);
        userRegistration = (TextView)findViewById(R.id.tvUserReg);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usermail = passwordEmail.getText().toString().trim();
                if (usermail.equals("")){
                    Toast.makeText(HALAMANPASSWORD.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HALAMANPASSWORD.this, "Password Reset Sent to Email", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(HALAMANPASSWORD.this , HALAMANLOGIN.class));
                            }else{
                                Toast.makeText(HALAMANPASSWORD.this, "Failed to Send Reset Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        /*apabila belum memiliki akun maka klik regist dulu ke maka akan menuju ke halaman daftar*/
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HALAMANPASSWORD.this, HALAMANDAFTAR.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        /*apabila klik lupa password maka akan menuju ke halaman lupa password*/
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HALAMANPASSWORD.this, HALAMANLOGIN.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }
}
