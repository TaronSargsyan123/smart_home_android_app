package com.example.arduinoonoffremout.components.ROneVOne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arduinoonoffremout.R;
import com.google.android.material.slider.Slider;

public class ROneVOneTimer extends AppCompatActivity {

    Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rone_vone_timer);
        slider = findViewById(R.id.ROneVOneTimerSlider);
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.i("Slider", String.valueOf(slider.getValue()));
            }
        });
    }
}