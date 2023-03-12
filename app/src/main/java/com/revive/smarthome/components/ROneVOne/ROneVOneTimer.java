package com.revive.smarthome.components.ROneVOne;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.revive.smarthome.R;
import com.revive.smarthome.StartActivity;
import com.revive.smarthome.firebase.AlarmTimerDefaultLogic;

import java.util.Calendar;

public class ROneVOneTimer extends AppCompatActivity {
    private TextView playButton;
    private TextView backButton;
    private TextView deviceList;
    private Spinner spinner;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one_timer);

        playButton = findViewById(R.id.play_button_timer_r_one_v_one);
        backButton = findViewById(R.id.back_timer_r_one_v_one);
        deviceList = findViewById(R.id.device_list_timer_r_one_v_one);
        spinner = findViewById(R.id.spinner_timer_r_one_v_one);
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12).setMinute(0).setTheme(R.style.CustomTimePicker).build();
        flag = true;
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.r_one_v_one_commands, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getSupportFragmentManager(), "tag");
                timePicker.addOnPositiveButtonClickListener(dialog -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                    long timeInMillis = calendar.getTimeInMillis();
                    startAlert(timeInMillis, getCommand());
                });
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

    private String getCommand(){
        if (spinner.getSelectedItem().toString().equals("On")){
            return "1";
        }else if (spinner.getSelectedItem().toString().equals("Off")){
            return "0";
        }else {
            return null;
        }
    }

    private void goToDeviceList(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    public void startAlert(long millis, String command){
        Intent inputIntent = getIntent();
        String inputData =  inputIntent.getStringExtra("ARGS");
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmTimerDefaultLogic.class);
        String massage = command + "/" + inputData + "/ROneVOne";
        intent.putExtra("message", massage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1234567891, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
    }




}