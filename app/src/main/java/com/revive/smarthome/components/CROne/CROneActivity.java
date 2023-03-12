package com.revive.smarthome.components.CROne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.revive.smarthome.Network;
import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

public class CROneActivity extends AppCompatActivity {
    private String name;
    private String host;
    private Boolean stage;
    private TextView button;
    private TextView back;
    private TextView timer;
    private String info;
    private Network network;
    private DevicesDefaultLogic defaultLogic;
    private String color;
    private int stageForSendColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crone);
        button = (TextView) findViewById(R.id.c_r_one_main_button);
        back = findViewById(R.id.back_to_main_activity_c_r_one);
        timer = findViewById(R.id.timer_c_r_one);
        button = (TextView) findViewById(R.id.c_r_one_main_button);
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

    }

    private void timer(){
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Intent intent = new Intent(getApplicationContext(), CROneTimer.class);
        intent.putExtra("ARGS", email.split("@")[0] + "/"+name);
        startActivity(intent);
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
                    String currentTime = defaultLogic.getDate();
                    if (flag) {
                        defaultLogic.insertDataCROne(0, email, name, "CROne", getColor());
                        defaultLogic.updateAnalyticsData(email, name, currentTime  + "/off");
                        stageForSendColor = 0;
                    }
                    else {
                        defaultLogic.insertDataCROne(1, email, name, "CROne", getColor());
                        defaultLogic.updateAnalyticsData(email, name, currentTime  + "/on");
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
                    String currentTime = defaultLogic.getDate();
                    defaultLogic.updateAnalyticsData(email, name, currentTime  + "/"+ String.valueOf(color));
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