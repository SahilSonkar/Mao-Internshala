package com.example.p_1internshala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.channels.Selector;

public class SetupYourLocation extends AppCompatActivity {

    EditText firstName , LastName;
    TextView SelectLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_your_location);

        firstName = findViewById(R.id.firstName);
        LastName  = findViewById(R.id.lastName);
        SelectLocation = findViewById(R.id.setuplocation);
        SelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("first",firstName.getText().toString().trim());
                bundle.putString("last",LastName.getText().toString().trim());
                Intent intent = new Intent( SetupYourLocation.this,SelectYourLocation.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}