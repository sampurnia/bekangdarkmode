package com.example.bekang.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bekang.calonfragment.HALAMANUTAMA;
import com.example.bekang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*HALAMAN UNTUK LOGIN
NOTE : SEMUA JENIS DATA DI FIREBASE BERUPA STRING*/

public class HALAMANLOGIN extends AppCompatActivity {

    private EditText Name;
    private EditText Pasword;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanlogin);
        Name =(EditText)findViewById(R.id.etName);
        Pasword =(EditText)findViewById(R.id.etPasword);
        Info =(TextView)findViewById(R.id.tvInfo);
        Login =(Button)findViewById(R.id.btnlogin);
        Info.setText("NO OF ATTEMP");

        /*mendeklarasikan yang ada di dalam program termasuk progress dialog dan firebase*/
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user =firebaseAuth.getCurrentUser();
        progressDialog= new ProgressDialog(this);


        if(user !=null) {
            /*ketika sudah login sebelumnya dan belum di log out dalam artian user nya tidak = 0 alias ada*/
            finish();
            startActivity(new Intent(HALAMANLOGIN.this, HALAMANUTAMA.class));
        }

        userRegistration = (TextView)findViewById(R.id.tvRegister);
        forgotPassword = (TextView)findViewById(R.id.tvForgotPassword);

        /*memvalidasi apakah email dan pasword nya berbentuk string*/
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    validate(Name.getText().toString(), Pasword.getText().toString());


            }
        });

        /*apabila belum memiliki akun maka klik regist dulu ke maka akan menuju ke halaman daftar*/
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HALAMANLOGIN.this, HALAMANDAFTAR.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        /*apabila klik lupa password maka akan menuju ke halaman lupa password*/
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HALAMANLOGIN.this, HALAMANPASSWORD.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


    }
    private void validate(String userName, String userPassword){
        /*progress dialog adalah dialog yang di tampilkan ketika login*/

        final String name = Name.getText().toString();
        String password = Pasword.getText().toString();
        if(name.isEmpty() || password.isEmpty())
        {
            /*apabila beberapa bagian ada yang kosong maka akan memunculkan tulisan*/
            Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("You Must Be Cute to Verifed");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
               progressDialog.dismiss();
               checkEmailVerification();
               /*ketika email dan password sesuai aplikasi akan mengecek apakah email nya sudah terverif*/
                }
                else{
               /*ketika email dan password tidak sesuai maka counter akan menghitung mundur untuk login*/
               Toast.makeText(HALAMANLOGIN.this, "Login Failed", Toast.LENGTH_SHORT).show();
               counter--;
               Info.setText("NO OF ATTEMP " + counter);
               progressDialog.dismiss();
                     if (counter == 0){ Login.setEnabled(false); }
                 }
            }
        });
        }
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser= firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        /*ketika email sudah di verifikasi/verifed*/
        if(emailflag){
            finish();
            startActivity(new Intent(HALAMANLOGIN.this , HALAMANUTAMA.class));
        }
        else {
            Toast.makeText(HALAMANLOGIN.this, "Verify your Email please", Toast.LENGTH_SHORT);
            firebaseAuth.signOut();

        }
    }

}
