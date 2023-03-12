package com.revive.smarthome.components.DOne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DOneAnalyticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_one_analytics);
        LineChartView chart = findViewById(R.id.chart_d_one);

        DevicesDefaultLogic defaultLogic = new DevicesDefaultLogic();
        Intent intent = getIntent();

        Viewport viewport = new Viewport(chart.getMaximumViewport());
        viewport.bottom = 0; // Set the minimum Y value to 0
        chart.setMaximumViewport(viewport);
        chart.setCurrentViewport(viewport);
        chart.setZoomEnabled(true);
        chart.setScrollEnabled(true);

        String email = intent.getStringExtra("EMAIL");
        String device = intent.getStringExtra("DEVICE");
        defaultLogic.drawAnalyticsDOne(email, device, chart);
    }
}