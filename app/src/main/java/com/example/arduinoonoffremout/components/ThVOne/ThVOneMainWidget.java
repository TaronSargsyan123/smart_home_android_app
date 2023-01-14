package com.example.arduinoonoffremout.components.ThVOne;

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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.arduinoonoffremout.DefaultMainWidget;
import com.example.arduinoonoffremout.Network;
import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.components.CurVOne.CurVOneActivity;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneActivity;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;

import java.io.Serializable;

public class ThVOneMainWidget  extends DefaultMainWidget implements Serializable {
    private TextView refreshButton;
    private LinearLayout background;
    private String name;
    private String host;
    private String[] info;
    private DevicesDefaultLogic defaultLogic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ThVOneMainWidget(Context context) {
        super(context);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_th_v_one, this, true);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ThVOneMainWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_th_v_one, this, true);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ThVOneMainWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_th_v_one, this, true);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ThVOneMainWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_th_v_one, this, true);
        init();
    }
    private void init(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        //defaultLogic.insertDataThVOne(email, getName(), getType(), "default", "default");
        background = findViewById(R.id.backThVOneMainWidget);
        refreshButton = (TextView) findViewById(R.id.ThTest);
        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });



    }

    private void openActivity(){
        Intent switchActivityIntent = new Intent(getContext(), ThVOne.class);
        switchActivityIntent.putExtra("NAME", name);
        switchActivityIntent.putExtra("HOST", host);
        //switchActivityIntent.putExtra("STAGE", buttonStage);
        getContext().startActivity(switchActivityIntent);
    }

    private void refresh(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        defaultLogic.insertDataThVOne(email, getName(), getType(), "default", "default");
        Toast toast = Toast.makeText(getContext(), defaultLogic.readDataThVOne(email, getName()), Toast.LENGTH_SHORT);
        toast.show();
    }



    public void setName(String temp){
        this.name = temp;
        //nameTextView.setText(temp);
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
        String temp = "ThVOne" + teb + info[1] + teb + info[2] + teb + info[3];
        return temp;
    }

}
