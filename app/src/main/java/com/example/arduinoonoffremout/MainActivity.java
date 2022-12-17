package com.example.arduinoonoffremout;

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
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                startCloudVersion.setAlpha(0.5F);
                startCloudVersion.setText(getString(R.string.cloud));

                handler.postDelayed(this, 5000);
            }
        };
        runnable.run();



        startCloudVersion.setAlpha(1);
        startCloudVersion.setText(getString(R.string.coming_soon));
    }

}