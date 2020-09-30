package com.example.p_1internshala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText editText;
    Button button;
    FirebaseAuth mAuth;
    TextView business;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        business = findViewById(R.id.business);
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( MainActivity.this,businessLogin.class));
            }
        });
        spinner = findViewById(R.id.countyName);
        spinner.setAdapter( new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryCode.countryNames));

        editText =findViewById(R.id.number);
        button=findViewById(R.id.sentotp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, ""+String.valueOf(spinner.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
                String code=CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number=editText.getText().toString().trim();
                if(number.length() < 10 || number.isEmpty())
                {
                    editText.setError("Number is required");
                    editText.requestFocus();
                    return;
                }
                String PhoneNumber = "+"+code+number;
                Intent intent = new Intent(MainActivity.this,VarifyPhoneNumber.class);
                intent.putExtra("PhoneNumber",PhoneNumber);
                intent.putExtra("From","CustomerLogin");
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected  void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user= mAuth.getCurrentUser();
//        if(user !=null)
//        {
//            startActivity(new Intent( MainActivity.this,login.class));
//        }
//    }
}