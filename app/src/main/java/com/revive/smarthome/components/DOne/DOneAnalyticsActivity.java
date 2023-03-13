package com.revive.smarthome.components.DOne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DOneAnalyticsActivity extends AppCompatActivity {
    private TextView back;
    private TextView history;
    private TextView historyTextView;
    private DevicesDefaultLogic defaultLogic;
    private Boolean stageGlobal = true;
    private String email;
    private String device;
    private LineChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_one_analytics);
        chart = findViewById(R.id.chart_d_one);
        back = findViewById(R.id.back_d_one_analytics);
        history = findViewById(R.id.history_d_one_analytics);
        historyTextView = findViewById(R.id.d_one_analytics_text_view);
        defaultLogic = new DevicesDefaultLogic();
        Intent intent = getIntent();

        Viewport viewport = new Viewport(chart.getMaximumViewport());
        viewport.bottom = 0; // Set the minimum Y value to 0
        chart.setMaximumViewport(viewport);
        chart.setCurrentViewport(viewport);
        chart.setZoomEnabled(true);
        chart.setScrollEnabled(true);

        email = intent.getStringExtra("EMAIL");
        device = intent.getStringExtra("DEVICE");
        defaultLogic.drawAnalyticsDOne(email, device, chart);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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