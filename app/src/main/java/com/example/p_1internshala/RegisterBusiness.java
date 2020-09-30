package com.example.p_1internshala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.SigningInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterBusiness extends AppCompatActivity {


    FirebaseAuth mAuth;
    EditText EmailOrPhoneNo;
    Button sentOTP;
    TextView SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);

        mAuth = FirebaseAuth.getInstance();
        EmailOrPhoneNo = findViewById(R.id.EmailOrPhoneR);
        sentOTP = findViewById(R.id.SendOTP);
        SignIn = findViewById(R.id.SignIn);
        String EmailPhone = EmailOrPhoneNo.getText().toString().trim();
        if(!isEmailValid(EmailPhone) && isValidPhoneNo(EmailPhone))
        {
            EmailOrPhoneNo.setError("Check Format");
            EmailOrPhoneNo.requestFocus();
            return ;
        }
        sentOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = EmailOrPhoneNo.getText().toString().trim();
                if(isEmailValid(phoneNo))
                {

                }
                else if(isValidPhoneNo(phoneNo))
                {
                    String code=CountryCode.countryAreaCodes[79];
                    String PhoneNumber="+"+code+phoneNo;
                    Intent intent = new Intent(RegisterBusiness.this,VarifyPhoneNumber.class);
                    intent.putExtra("PhoneNumber",PhoneNumber);
                    intent.putExtra("From","BusinessRegd");
                    startActivity(intent);
                }

            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(RegisterBusiness.this,businessLogin.class));
            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isValidPhoneNo(CharSequence iPhoneNo) {
        return !TextUtils.isEmpty(iPhoneNo) &&
                Patterns.PHONE.matcher(iPhoneNo).matches();
    }
//    private void SignUpNewUser(String email,String password)
//    {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }



}