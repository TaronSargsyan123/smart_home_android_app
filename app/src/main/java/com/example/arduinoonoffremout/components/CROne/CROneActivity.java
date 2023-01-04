package com.example.arduinoonoffremout.components.CROne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.arduinoonoffremout.Network;
import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneActivity;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneSettings;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneTimer;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larswerkman.holocolorpicker.ColorPicker;

public class CROneActivity extends AppCompatActivity {
    private String name;
    private String host;
    private Boolean stage;
    private TextView button;
    private TextView back;
    private FloatingActionButton settings;
    private TextView timer;
    private TextView loopTimer;
    private String info;
    private Network network;
    private DevicesDefaultLogic defaultLogic;
    private String color;
    private int stageForSendColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crone);
        button = (TextView) findViewById(R.id.CROneMainButton);
        back = findViewById(R.id.backToMainActivityCROne);
        settings = findViewById(R.id.settingsCROne);
        timer = findViewById(R.id.timerCROne);
        loopTimer = findViewById(R.id.loopTimerCROne);
        defaultLogic = new DevicesDefaultLogic();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                name= null;
                host= null;
                stage = null;
            } else {
                name= extras.getString("NAME");
                host= extras.getString("HOST");
                stage= extras.getBoolean("STAGE");
            }
        } else {
            name= (String) savedInstanceState.getSerializable("NAME");
            host= (String) savedInstanceState.getSerializable("HOST");
            stage= (Boolean) savedInstanceState.getSerializable("STAGE");
        }

        info = name + "#$%" + host;





        init();

        ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        setColor(String.valueOf(picker.getColor()));


        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                sendColor(color);
            }
        });

        button = (TextView) findViewById(R.id.CROneMainButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer();
            }
        });
        loopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopTimer();
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CROneActivity.this, CROneActivity.class);
                i.putExtra("INFO", info);

                startActivity(i);

            }
        });
    }

    private void init(){
        if (stage.equals(true)){
            button.setText(getResources().getString(R.string.off));
        }else if (stage.equals(false)){
            button.setText(getResources().getString(R.string.on));
        }

    }

    private void buttonClick(){
        try {
            // if buttons text = on
            if (stage){
                button.setText(getResources().getString(R.string.on));
                sendFromMain(stage);
                stage = false;
            }
            // if buttons text = off
            else {
                button.setText(getResources().getString(R.string.off));
                sendFromMain(stage);
                stage = true;
            }
        }catch (Exception e){
            Log.i("Network exception", String.valueOf(e));
        }
    }

    private void back(){
        finish();
    }

    private void timer(){
        Intent intent = new Intent(getApplicationContext(), ROneVOneTimer.class);
        startActivity(intent);
    }

    private void loopTimer(){
        Intent intent = new Intent(getApplicationContext(), CROneActivity.class);
        startActivity(intent);
    }



    private void sendFromMain(Boolean flag){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    if (flag) {
                        defaultLogic.insertDataCROne(0, email, name, "CROne", getColor());
                        stageForSendColor = 0;
                    }
                    else {
                        defaultLogic.insertDataCROne(1, email, name, "CROne", getColor());
                        stageForSendColor = 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }

    private String getColor(){
        return String.valueOf(color);
    }

    private void setColor(String color){
        this.color = color;
    }

    private void sendColor(int color){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    setColor(String.valueOf(color));

                    defaultLogic.insertDataCROne(stageForSendColor, email, name, "CROne", String.valueOf(color));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();

    }

    public void setName(String name){
        this.name = name;
        init();
    }

    public void setHost(String host){
        this.host = host;
        init();
    }
}