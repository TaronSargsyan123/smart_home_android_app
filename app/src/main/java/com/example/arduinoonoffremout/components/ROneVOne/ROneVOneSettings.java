package com.example.arduinoonoffremout.components.ROneVOne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.arduinoonoffremout.MainActivity;
import com.example.arduinoonoffremout.R;

public class ROneVOneSettings extends AppCompatActivity {
    private Button saveChangesButton;
    private Button deleteDeviceButton;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one_settings);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                info= null;
            } else {
                info= extras.getString("INFO");
            }
        } else {
            info= (String) savedInstanceState.getSerializable("INFO");

        }


        deleteDeviceButton = findViewById(R.id.deleteOnOffRelayButton);

        saveChangesButton = findViewById(R.id.ROneVOneSettingsSaveChangesButton);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        deleteDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Info from Settings", info);
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                //Intent data = new Intent();
                //data.putExtra(DevicesFragment.ACCESS_MESSAGE, message);
                //setResult(RESULT_OK, data);
                //finish();
            }
        });
    }
}