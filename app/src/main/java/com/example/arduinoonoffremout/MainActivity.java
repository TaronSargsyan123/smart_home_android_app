package com.example.arduinoonoffremout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.arduinoonoffremout.firebase.LoginActivity;
import com.example.arduinoonoffremout.firebase.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    TextView startRegister;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView startLogin = findViewById(R.id.startLoginActivityFromMainActivity);
        startRegister = findViewById(R.id.startRegisterActivityFromMainActivity);

        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });

        startRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });

    }

    private void startLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void startRegisterActivity(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);


    }

}