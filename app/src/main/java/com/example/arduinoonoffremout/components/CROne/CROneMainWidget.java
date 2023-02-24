package com.example.arduinoonoffremout.components.CROne;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.arduinoonoffremout.DefaultMainWidget;
import com.example.arduinoonoffremout.Network;
import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;

import java.io.Serializable;

public class CROneMainWidget extends DefaultMainWidget implements Serializable {

    TextView mainButton;
    private Network network;
    private Boolean buttonStage;
    private LinearLayout background;
    private TextView textView;
    private String name;
    private String host;
    private TextView nameTextView;
    private String stage;
    private String[] info;
    private DevicesDefaultLogic defaultLogic;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public CROneMainWidget(Context context) {
        super(context);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_cr_one_button, this, true);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public CROneMainWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public CROneMainWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public CROneMainWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }

    private void init(){

        textView = (TextView) findViewById(R.id.c_r_one_stage_text_view_main_widget);
        background = (LinearLayout) findViewById(R.id.c_r_one_background_main_widget);
        nameTextView = (TextView) findViewById(R.id.c_r_one_name_text_view_main_widget);
        mainButton = (TextView) findViewById(R.id.c_r_one_main_button_main_Widget);
        defaultLogic = new DevicesDefaultLogic();

        network = new Network(host, 5045);
        buttonStage = false;

        setName(name);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openRelayActivity();
            }
        });

        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openRelayActivity();
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonStage){
                    Log.i("TEST", "on");
                    textView.setText(getResources().getString(R.string.on));
                    sendFromMain(buttonStage);
                    buttonStage = false;
                }
                else {
                    Log.i("TEST", "off");
                    textView.setText(getResources().getString(R.string.off));
                    sendFromMain(buttonStage);
                    buttonStage = true;
                }
            }
        });
    }

    private void sendFromMain(Boolean flag){
        Thread threadOn = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
                    String email = sharedPreferences.getString("email", "");

                    if (flag) {
                        defaultLogic.insertDataCROne(1, email, getName(), getType(), "color");
                        stage = "1";
                    }
                    else {
                        defaultLogic.insertDataCROne(0, email, getName(), getType(), "color");
                        stage = "0";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }

    private void openRelayActivity(){
        Intent switchActivityIntent = new Intent(getContext(), CROneActivity.class);
        switchActivityIntent.putExtra("NAME", name);
        switchActivityIntent.putExtra("HOST", host);
        switchActivityIntent.putExtra("STAGE", buttonStage);
        getContext().startActivity(switchActivityIntent);
    }

    public void setName(String temp){
        nameTextView.setText(temp);
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
        String temp = "CROne" + teb + info[1] + teb + info[2] + teb + info[3];
        return temp;
    }

    @Override
    public void on() {
        textView.setText(getResources().getString(R.string.on));
        sendFromMain(buttonStage);
        buttonStage = false;
    }

    @Override
    public void off() {
        textView.setText(getResources().getString(R.string.off));
        sendFromMain(buttonStage);
        buttonStage = true;
    }

    @Override
    public void addValue(String value) {
        super.addValue(value);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
        try {
            int color = Color.parseColor(value);
            defaultLogic.insertDataCROne(0, email, getName(), getType(), String.valueOf(color));
        }catch (Exception ignored){
            Toast.makeText(getContext(), "Color not detected", Toast.LENGTH_SHORT).show();
        }
    }


}
