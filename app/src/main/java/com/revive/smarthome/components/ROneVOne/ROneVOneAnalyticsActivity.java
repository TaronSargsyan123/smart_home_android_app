package com.revive.smarthome.components.ROneVOne;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ROneVOneAnalyticsActivity extends AppCompatActivity {
    private TextView back;
    private TextView history;
    private TextView historyTextView;
    private DevicesDefaultLogic defaultLogic;
    private String email;
    private String device;
    private Boolean stageGlobal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_one_v_one_analytics);
        back = findViewById(R.id.back_r_one_v_one_analytics);
        history = findViewById(R.id.history_r_one_v_one_analytics);
        historyTextView = findViewById(R.id.r_one_v_one_analytics_text_view);

        defaultLogic = new DevicesDefaultLogic();
        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");
        device = intent.getStringExtra("DEVICE");

        LineChartView chart = findViewById(R.id.chart);

        LineChartData data = new LineChartData();

        Line line1 = new Line(generateDataForLine1()).setColor(Color.BLUE).setCubic(false);

        List<Line> lines = new ArrayList<>();
        lines.add(line1);
        data.setLines(lines);

        List<AxisValue> axisValues = new ArrayList<>();
        axisValues.add(new AxisValue(0).setLabel("Jan"));
        axisValues.add(new AxisValue(1).setLabel("Feb"));
        axisValues.add(new AxisValue(2).setLabel("Mar"));
        axisValues.add(new AxisValue(3).setLabel("Apr"));
        axisValues.add(new AxisValue(5).setLabel("Jun"));
        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));

        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("Values");
        data.setAxisYLeft(axisY);

        chart.setLineChartData(data);



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

    private List<PointValue> generateDataForLine1() {
        List<PointValue> values = new ArrayList<>();
        values.add(new PointValue(0, 20));
        values.add(new PointValue(1, 10));
        values.add(new PointValue(2, 15));
        values.add(new PointValue(3, 30));
        values.add(new PointValue(4, 25));
        values.add(new PointValue(5, 20));
        return values;
    }


    private void back(){
        finish();
    }

    private void drawHistory(Boolean stage){
        if(stage){
            historyTextView.setVisibility(View.VISIBLE);
            defaultLogic.drawAnalytics(email, device, historyTextView);
        }else {
            historyTextView.setVisibility(View.INVISIBLE);
        }
    }
}