package com.revive.smarthome.components.ROneVOne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

public class ROneVOneAnalyticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one_analytics);
        TextView textView = findViewById(R.id.r_one_v_one_analytics_text_view);
        DevicesDefaultLogic defaultLogic = new DevicesDefaultLogic();
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        String device = intent.getStringExtra("DEVICE");
        Log.i("ARR", email + " " + device);
        defaultLogic.drawAnalytics(email, device, textView);
    }
}