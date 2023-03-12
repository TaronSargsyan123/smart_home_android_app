package com.revive.smarthome.components.ROneVOne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

public class ROneVOneActivity extends AppCompatActivity {
    private String name;
    private String host;
    private TextView nameTextView;
    private Boolean stage;
    private TextView button;
    private TextView back;
    private TextView analytics;
    private TextView timer;
    private TextView loopTimer;
    private String info;
    private DevicesDefaultLogic defaultLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one);
        button = findViewById(R.id.r_one_v_one_main_button);
        back = findViewById(R.id.back_to_main_activity_r_one_v_one);
        nameTextView = (TextView) findViewById(R.id.r_one_v_one_name_text_view_activity);
        timer = findViewById(R.id.timer_r_one_v_one);
        loopTimer = findViewById(R.id.loop_timer_r_one_v_one);
        analytics = findViewById(R.id.analytics_r_one_v_one);
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
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analytics();
            }
        });



    }


    private void init(){
        nameTextView.setText(name);
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
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Intent intent = new Intent(getApplicationContext(), ROneVOneTimer.class);
        intent.putExtra("ARGS", email.split("@")[0] + "/"+name);
        startActivity(intent);
    }

    private void loopTimer(){
    }

    private void analytics(){
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Intent intent = new Intent(getApplicationContext(), ROneVOneAnalyticsActivity.class);
        intent.putExtra("DEVICE", name);
        intent.putExtra("EMAIL", email);
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
                        defaultLogic.insertDataROneVOne(0, email, name, "ROneVOne");
                        defaultLogic.updateAnalyticsData(email, name, currentTime  + "on");
                    }
                    else {
                        defaultLogic.insertDataROneVOne(1, email, name, "ROneVOne");
                        defaultLogic.updateAnalyticsData(email, name, currentTime  + "off");
                    }
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