package com.revive.smarthome.components.DefaultDevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

import lecho.lib.hellocharts.view.ColumnChartView;

public class DefaultAnalyticsActivity extends AppCompatActivity {
    private TextView back;
    private TextView history;
    private DevicesDefaultLogic defaultLogic;
    private String email;
    private String device;
    private TextView historyTextView;
    private Boolean stageGlobal = true;
    private ColumnChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_analytics);
        back = findViewById(R.id.back_default_analytics);
        history = findViewById(R.id.history_default_analytics);
        historyTextView = findViewById(R.id.default_analytics_text_view);
        chart = findViewById(R.id.column_chart_default);
        defaultLogic = new DevicesDefaultLogic();
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");
        device = intent.getStringExtra("DEVICE");
        defaultLogic.drawAnalyticColumns(email, device, chart);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stageGlobal){
                    drawHistory(stageGlobal);
                    stageGlobal = false;
                } else {
                    drawHistory(stageGlobal);
                    stageGlobal = true;
                }

            }
        });

    }

    private void back(){
        finish();
    }

    //draw histogram to show device use history

    private void drawHistory(Boolean stage){
        if(stage){
            chart.setVisibility(View.INVISIBLE);
            historyTextView.setVisibility(View.VISIBLE);
            defaultLogic.drawAnalytics(email, device, historyTextView);
        }else {
            chart.setVisibility(View.VISIBLE);
            historyTextView.setVisibility(View.INVISIBLE);
        }
    }


}