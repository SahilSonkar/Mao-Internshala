package com.example.p_1internshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VarifyPhoneNumber extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private String VarificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varify_phone_number);



        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressr);
        progressBar.setVisibility(View.INVISIBLE);
        editText = findViewById(R.id.codeEdit);


        String phoneNumber = getIntent().getStringExtra("PhoneNumber");
        sendVerificationCode(phoneNumber);
        findViewById(R.id.codeEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VarificationId, code);
            signInWithCredential(credential);
    }

    private void sendVerificationCode(String number) {
//        progressBar.setVisibility(View.VISIBLE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,
                    60,
                    TimeUnit.SECONDS,
                    TaskExecutors.MAIN_THREAD,
                    mCallBack
            );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VarificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed (FirebaseException e){

            Toast.makeText(VarifyPhoneNumber.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void signInWithCredential(PhoneAuthCredential credential) {



        
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String From = getIntent().getStringExtra("From").trim();
                            if(From.equals("BusinessRegd"))
                            {
                                Intent intent = new Intent(VarifyPhoneNumber.this, ConfirmPasswordB.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if(From.equals("CustomerLogin"))
                            {
                                Intent intent = new Intent(VarifyPhoneNumber.this, SetupYourLocation.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(VarifyPhoneNumber.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}