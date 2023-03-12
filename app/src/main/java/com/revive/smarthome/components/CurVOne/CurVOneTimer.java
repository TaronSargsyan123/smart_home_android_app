package com.revive.smarthome.components.CurVOne;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.revive.smarthome.R;
import com.revive.smarthome.StartActivity;
import com.revive.smarthome.firebase.AlarmTimerDefaultLogic;

import java.util.Calendar;

public class CurVOneTimer extends AppCompatActivity {
    private TextView createButton;
    private TextView backButton;
    private TextView deviceList;
    private Button selectTime;
    private Spinner spinner;
    private CheckBox checkBox;
    private EditText frequency;
    private TextInputLayout textInputLayout;
    private boolean flag;
    private int hours;
    private int minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur_v_one_timer);

        createButton = findViewById(R.id.create_time_cur_v_one);
        selectTime = findViewById(R.id.select_time_cur_v_one);
        checkBox = findViewById(R.id.check_box_frequency_cur_v_one);
        frequency = findViewById(R.id.edit_text_timer_frequency_cur_v_one);
        backButton = findViewById(R.id.back_timer_cur_v_one);
        deviceList = findViewById(R.id.device_list_timer_cur_v_one);
        spinner = findViewById(R.id.spinner_timer_cur_v_one);
        textInputLayout = findViewById(R.id.enter_timer_frequency_cur_v_one);
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12).setMinute(0).setTheme(R.style.CustomTimePicker).build();
        flag = true;
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.cur_v_one_commands, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        frequency.setEnabled(false);
        textInputLayout.setEnabled(false);

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getSupportFragmentManager(), "tag");
                timePicker.addOnPositiveButtonClickListener(dialog -> {
                    hours = timePicker.getHour();
                    minutes = timePicker.getMinute();
                    selectTime.setText(hours+":"+minutes);
                });
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    frequency.setEnabled(true);
                    textInputLayout.setEnabled(true);
                } else {
                    frequency.setEnabled(false);
                    textInputLayout.setEnabled(false);
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(minutes != 0 && hours != 0){
                    if (checkBox.isChecked()){
                        int frequencyHours = Integer.parseInt(frequency.getText().toString());
                        long millis = (long) frequencyHours * 3600 * 1000;
                        for (int i = 0; i < 5; i++) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hours);
                            calendar.set(Calendar.MINUTE, minutes);
                            long timeInMillis = calendar.getTimeInMillis() + i * millis;
                            startAlert(timeInMillis, getCommand());
                        }
                    }else {
                        setTime();
                        finish();
                    }
                }else {
                    Toast.makeText(CurVOneTimer.this, "Please select time", Toast.LENGTH_SHORT).show();
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


    private void setTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        long timeInMillis = calendar.getTimeInMillis();
        startAlert(timeInMillis, getCommand());
    }

    private int getCommand(){
        if (spinner.getSelectedItem().toString().equals("Open")){
            return 1;
        }else if (spinner.getSelectedItem().toString().equals("Close")){
            return 0;
        }else {
            return 2;
        }
    }

    private void goToDeviceList(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    public void startAlert(long millis, int command){
        Intent inputIntent = getIntent();
        String inputData =  inputIntent.getStringExtra("ARGS");
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmTimerDefaultLogic.class);
        String massage = command + "/" + inputData + "/CurVOne";
        intent.putExtra("message", massage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1234567891, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
    }
}