package com.example.arduinoonoffremout.components.ROneVOne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.StartActivity;
import com.google.android.material.slider.Slider;

public class ROneVOneTimer extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView playButton;
    private TextView backButton;
    private TextView deviceList;
    private EditText timeEditText;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one_timer);

        playButton = findViewById(R.id.playButtonTimerROneVOne);
        backButton = findViewById(R.id.backTimerROneVOne);
        deviceList = findViewById(R.id.deviceListTimerROneVOne);
        progressBar = findViewById(R.id.progressBarTimerROneVOne);
        timeEditText = findViewById(R.id.timeTextROneVOne);
        flag = true;




        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag){
                    timeEditText.setEnabled(false);
                    flag = false;
                }else {
                    timeEditText.setEnabled(true);
                    flag = true;
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDeviceList();
            }
        });

    }

    private void goToDeviceList(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    private void timeFormatter(String time){
        switch (time.length()){
            case 6:

                break;
            case 4:

                break;
            case 2:

                break;
        }

    }


}