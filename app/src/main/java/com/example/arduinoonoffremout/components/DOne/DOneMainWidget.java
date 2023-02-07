package com.example.arduinoonoffremout.components.DOne;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.arduinoonoffremout.DefaultMainWidget;
import com.example.arduinoonoffremout.Network;
import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.components.CROne.CROneActivity;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;
import com.google.android.material.slider.Slider;

import java.io.Serializable;

public class DOneMainWidget   extends DefaultMainWidget implements Serializable {
    private TextView deviceName;
    private TextView mainButton;
    private LinearLayout background;
    private Slider slider;
    private String name;
    private String host;
    private String[] info;
    private Boolean stage = false;
    private DevicesDefaultLogic defaultLogic;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public DOneMainWidget(Context context) {
        super(context);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_d_one, this, true);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public DOneMainWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_d_one, this, true);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public DOneMainWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_d_one, this, true);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public DOneMainWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_d_one, this, true);
        init();
    }
    private void init(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        //defaultLogic.insertDataThVOne(email, getName(), getType(), "default", "default");
        background = findViewById(R.id.d_one_background_main_widget);
        deviceName = (TextView) findViewById(R.id.d_one_name_text_view_main_widget);
        mainButton = findViewById(R.id.d_one_button_main_widget);
        slider = findViewById(R.id.d_one_slider_main_widget);
        defaultLogic = new DevicesDefaultLogic();
        setName(name);

        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

        mainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMainButton();
            }
        });
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                defaultLogic.insertDataDOne(1, email, getName(), getType(), getBright());
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                defaultLogic.insertDataDOne(1, email, getName(), getType(), getBright());
            }
        });


    }
    private void openActivity(){
        Intent switchActivityIntent = new Intent(getContext(), DOneActivity.class);
        switchActivityIntent.putExtra("NAME", name);
        switchActivityIntent.putExtra("STAGE", stage);
        getContext().startActivity(switchActivityIntent);
    }

    private void clickMainButton(){
        if (stage == true){
            stage = false;
            sendFromMain(stage);
            slider.setEnabled(stage);
        }else {
            stage = true;
            sendFromMain(stage);
            slider.setEnabled(stage);

        }
    }

    private int getBright(){
        float temp = slider.getValue();
        return (int) (temp * 100);
    }


    private void sendFromMain(Boolean flag){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");

                    if (flag) {

                        defaultLogic.insertDataDOne(1, email, getName(), getType(), getBright());
                    }
                    else {
                        defaultLogic.insertDataDOne(0, email, getName(), getType(), getBright());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }



    public void setName(String temp){
        this.name = temp;
        deviceName.setText(temp);
    }

    public void configNameAndHost(String name, String host){
        this.name = name;
        this.host = host;
        setName(this.name);
    }


    public String getName(){
        return name;
    }

    private String getHost(){
        return host;
    }


    public String getInfoString(){
        info = new String[]{getType(), getIdString(), getName(), getHost()};
        String teb = "@";
        String temp = "DOne" + teb + info[1] + teb + info[2] + teb + info[3];
        return temp;
    }

    @Override
    public void on() {
        stage = true;
        sendFromMain(stage);
        slider.setEnabled(stage);
    }

    @Override
    public void off() {
        stage = false;
        sendFromMain(stage);
        slider.setEnabled(stage);
    }

}
