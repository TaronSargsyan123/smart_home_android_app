package com.example.arduinoonoffremout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

//        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
//        String email = sharedPreferences.getString("email", null);
//        String password = sharedPreferences.getString("password", null);
//
//        if (email != null && password != null){
//            Log.i("AAAAAA", email);
//            Log.i("AAAAAA", password);
//            startStartActivity();
//        }

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

    private void startStartActivity(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

}