package com.example.p_1internshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class businessLogin extends AppCompatActivity {

    private static final String TAG = "businessLogin";

    FirebaseAuth mAuth;
    EditText EmailOrPhoneNO , password;
    Button login;
    TextView signIp;
    String emailOrPhoneNo="",Password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);
        EmailOrPhoneNO = findViewById(R.id.EmailOrPhoneNo);
        emailOrPhoneNo = EmailOrPhoneNO.getText().toString();
        emailOrPhoneNo.trim();
        password = findViewById(R.id.password);
        Password = password.getText().toString();
        login = findViewById(R.id.login);
        signIp = findViewById(R.id.signUpB);
        signIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(businessLogin.this,RegisterBusiness.class));
            }
        });
        if(isEmailValid(emailOrPhoneNo))
        {
            mAuth.signInWithEmailAndPassword(emailOrPhoneNo, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(businessLogin.this, ""+"loged In", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent( businessLogin.this,Dashboard.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(businessLogin.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//
                            }
                        }
                    });
        }
        else if(isValidPhoneNo(emailOrPhoneNo))
        {

        }
        else {
            EmailOrPhoneNO.setError("Check Email");
            EmailOrPhoneNO.requestFocus();
            return;
        }

    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isValidPhoneNo(CharSequence iPhoneNo) {
        return !TextUtils.isEmpty(iPhoneNo) &&
                Patterns.PHONE.matcher(iPhoneNo).matches();
    }
}