package com.example.arduinoonoffremout.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.arduinoonoffremout.Network;
import com.example.arduinoonoffremout.OnOffRelayActivity;
import com.example.arduinoonoffremout.R;


public class OnOffButton extends LinearLayout {
    Button mainButton;
    private Network network;
    private Boolean buttonStage;
    private LinearLayout background;
    private TextView textView;
    private String name;
    private String host;
    private TextView nameTextView;
    private String stage;



    @RequiresApi(api = Build.VERSION_CODES.M)
    public OnOffButton(Context context) {
        super(context);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_on_off_button, this, true);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public OnOffButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_on_off_button, this, true);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public OnOffButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_on_off_button, this, true);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public OnOffButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_on_off_button, this, true);
        init();
    }


    private void init(){
        textView = (TextView) findViewById(R.id.onOffRelayTextView);
        background = (LinearLayout) findViewById(R.id.onOffRelayBackground);
        nameTextView = (TextView) findViewById(R.id.relayNameTextView);
        mainButton = (Button) findViewById(R.id.mainButton);

        network = new Network(host, 5045);
        buttonStage = false;

        setNameTextView(name);

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
                    if (flag) {
                        network.sendMessage("1");
                    }
                    else {
                        network.sendMessage("11");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }

    private void openRelayActivity(){
        Intent switchActivityIntent = new Intent(getContext(), OnOffRelayActivity.class);
        switchActivityIntent.putExtra("NAME", name);
        switchActivityIntent.putExtra("HOST", host);
        switchActivityIntent.putExtra("STAGE", buttonStage);
        getContext().startActivity(switchActivityIntent);
    }

    private void setNameTextView(String temp){
        nameTextView.setText(temp);
    }

    public void configNameAndHost(String name, String host){
        this.name = name;
        this.host = host;
        setNameTextView(this.name);
    }



}