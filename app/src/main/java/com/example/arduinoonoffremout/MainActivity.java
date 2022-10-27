package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.arduinoonoffremout.components.OnOffButton;

public class MainActivity extends AppCompatActivity {
    private Button addDevice;
    public LinearLayout layout;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addDevice = (Button) findViewById(R.id.addDeviceMain);
        layout  = (LinearLayout) findViewById(R.id.mainActivityLayout);

        addRelay("Kitchen", "192.168.1.12");
        addRelay("Main hall", "192.168.1.12");

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), CreateDeviceActivity.class);
                startActivity(switchActivityIntent);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addRelay(String name, String host){
        OnOffButton onOffButton = new OnOffButton(this);
        onOffButton.configNameAndHost(name, host);
        layout.addView(onOffButton);
    }


}