package com.example.arduinoonoffremout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OnOffRelayActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String name;
    private String host;
    private TextView nameTextView;
    private Boolean stage;
    private Button button;
    private Network network;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_off_relay);
        button = (Button) findViewById(R.id.mainButton);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        nameTextView = (TextView) findViewById(R.id.relayNameTextViewFromSelfActivity);

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





        init();
        bottomNavigationView.setOnItemSelectedListener(item -> {

         switch (item.getItemId()){
             case R.id.backToMainActivity:
                 back();
             case R.id.timer:
                 timer();
             case R.id.loopTimer:
                 loopTimer();
         }
            return false;
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
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

    private void timer(){}

    private void loopTimer(){}

    private void sendFromMain(Boolean flag){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    if (flag) {
                        network.sendMessage("1");
                    }
                    else {
                        network.sendMessage("11");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }

}