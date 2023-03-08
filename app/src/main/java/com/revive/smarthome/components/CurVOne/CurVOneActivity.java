package com.revive.smarthome.components.CurVOne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        buttonOpen = (TextView) findViewById(R.id.mai_bButton_open_cur_v_one);
        buttonClose = (TextView) findViewById(R.id.mai_bButton_close_cur_v_one);
        back = findViewById(R.id.back_to_main_activity_cur_v_one);
        settings = findViewById(R.id.settings_cur_v_one);
        timer = findViewById(R.id.timer_cur_v_one);
        loopTimer = findViewById(R.id.loop_timer_cur_v_one);
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