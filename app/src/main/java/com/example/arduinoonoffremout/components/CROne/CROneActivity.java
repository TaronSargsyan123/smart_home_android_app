package com.example.arduinoonoffremout.components.CROne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.arduinoonoffremout.R;
import com.larswerkman.holocolorpicker.ColorPicker;

public class CROneActivity extends AppCompatActivity {
    private TextView button;
    private Boolean stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crone);
        stage = true;

        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);

        button = (TextView) findViewById(R.id.CROneMainButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!stage){
                    button.setText("ON");
                    stage = true;
                }else {
                    button.setText("OFF");
                    stage = false;
                }
            }
        });
    }
}