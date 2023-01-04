package com.example.arduinoonoffremout.components.CurVOne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.arduinoonoffremout.Network;
import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.components.CROne.CROneActivity;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneTimer;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.larswerkman.holocolorpicker.ColorPicker;

public class CurVOneActivity extends AppCompatActivity {
    private String name;
    private String host;
    private Boolean stage;
    private TextView buttonOpen;
    private TextView buttonClose;
    private TextView back;
    private FloatingActionButton settings;
    private TextView timer;
    private TextView loopTimer;
    private String info;
    private DevicesDefaultLogic defaultLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur_v_one);

        buttonOpen = (TextView) findViewById(R.id.mainButtonOpenCurVOne);
        buttonClose = (TextView) findViewById(R.id.mainButtonCloseCurVOne);
        back = findViewById(R.id.backToMainActivityCurVOne);
        settings = findViewById(R.id.settingsCurVOne);
        timer = findViewById(R.id.timerCurVOne);
        loopTimer = findViewById(R.id.loopTimerCurVOne);
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


        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFromMain("open");
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFromMain("close");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//        timer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        loopTimer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(CurVOneActivity.this, CROneActivity.class);
//                i.putExtra("INFO", info);
//
//                startActivity(i);
//
//            }
//        });

    }






    private void sendFromMain(String temp){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    defaultLogic.insertDataCurVOne(temp, email, name, "CurVOne");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }

    public void setName(String name){
        this.name = name;
    }
}