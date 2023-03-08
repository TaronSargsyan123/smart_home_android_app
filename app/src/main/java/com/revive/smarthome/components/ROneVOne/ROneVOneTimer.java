package com.revive.smarthome.components.ROneVOne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.revive.smarthome.R;
import com.revive.smarthome.StartActivity;

public class ROneVOneTimer extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView playButton;
    private TextView backButton;
    private TextView deviceList;
    private EditText timeEditText;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one_timer);

        playButton = findViewById(R.id.play_button_timer_r_one_v_one);
        backButton = findViewById(R.id.back_timer_r_one_v_one);
        deviceList = findViewById(R.id.device_list_timer_r_one_v_one);
        progressBar = findViewById(R.id.progress_bar_timer_r_one_v_one);
        timeEditText = findViewById(R.id.time_text_r_one_v_one);
        flag = true;




        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag){
                    timeEditText.setEnabled(false);
                    flag = false;
                }else {
                    timeEditText.setEnabled(true);
                    flag = true;
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDeviceList();
            }
        });

    }

    private void goToDeviceList(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    private void timeFormatter(String time){
        switch (time.length()){
            case 6:

                break;
            case 4:

                break;
            case 2:

                break;
        }

    }


}