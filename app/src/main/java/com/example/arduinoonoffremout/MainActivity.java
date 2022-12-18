package com.example.arduinoonoffremout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.arduinoonoffremout.firebase.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView startCloudVersion;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView startLocalVersion = findViewById(R.id.startAppLocalVersion);
        startCloudVersion = findViewById(R.id.startAppCloudVersion);

        startLocalVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocalVersion();
            }
        });

        startCloudVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCloudVersion();
            }
        });

    }

    private void startLocalVersion(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    private void startCloudVersion(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

}