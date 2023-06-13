package com.revive.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.firebase.LoginActivity;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(this::checkUser, 2000);
    }



    private void checkUser() {
        // check if user already have account go to start activity
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
        String emailLocal = sharedPreferences.getString("email", null);
        String passwordLocal = sharedPreferences.getString("password", null);

        if(emailLocal != null && passwordLocal != null){
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}