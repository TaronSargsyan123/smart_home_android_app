package com.revive.smarthome.components.CurVOne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.R;
import com.revive.smarthome.components.DefaultDevice.DefaultAnalyticsActivity;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

public class CurVOneActivity extends AppCompatActivity {
    private String name;
    private String host;
    private Boolean stage;
    private TextView buttonOpen;
    private TextView buttonClose;
    private TextView back;
    private TextView timer;
    private TextView analytics;
    private String info;
    private DevicesDefaultLogic defaultLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur_v_one);

        buttonOpen = (TextView) findViewById(R.id.mai_bButton_open_cur_v_one);
        buttonClose = (TextView) findViewById(R.id.mai_bButton_close_cur_v_one);
        back = findViewById(R.id.back_to_main_activity_cur_v_one);
        timer = findViewById(R.id.timer_cur_v_one);
        analytics = findViewById(R.id.analytics_cur_v_one);
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
                finish();
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer();
            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analytics();
            }
        });

    }

    private void timer(){
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Intent intent = new Intent(getApplicationContext(), CurVOneTimer.class);
        intent.putExtra("ARGS", email.split("@")[0] + "/"+name);
        startActivity(intent);
    }

    private void analytics(){
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Intent intent = new Intent(getApplicationContext(), DefaultAnalyticsActivity.class);
        intent.putExtra("DEVICE", name);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }


    private void sendFromMain(String temp){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");
                    String currentTime = defaultLogic.getDate();
                    defaultLogic.updateAnalyticsDataColumns(email, name);
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