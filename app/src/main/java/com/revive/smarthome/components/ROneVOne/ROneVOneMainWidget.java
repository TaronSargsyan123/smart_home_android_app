package com.revive.smarthome.components.ROneVOne;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.revive.smarthome.DefaultMainWidget;
import com.revive.smarthome.R;
import com.revive.smarthome.firebase.DevicesDefaultLogic;

import java.io.Serializable;


public class ROneVOneMainWidget extends DefaultMainWidget implements  Serializable {
    TextView mainButton;
    private Boolean buttonStage;
    private LinearLayout background;
    private TextView textView;
    private String name;
    private String host;
    private TextView nameTextView;
    private String stage;
    private String[] info;
    private DevicesDefaultLogic defaultLogic;
    private LinearLayout parentLayout;
    private String email;



    @RequiresApi(api = Build.VERSION_CODES.M)
    public ROneVOneMainWidget(Context context) {
        super(context);


        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public ROneVOneMainWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public ROneVOneMainWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public ROneVOneMainWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        LayoutInflater inflater = context.getSystemService(LayoutInflater.class);
        View v = inflater.inflate(R.layout.view_r_one_v_one_button, this, true);
        init();
    }


    private void init(){

        textView = (TextView) findViewById(R.id.r_one_v_one_stage_text_view_main_widget);
        background = (LinearLayout) findViewById(R.id.r_one_v_one_relay_background_main_widget);
        nameTextView = (TextView) findViewById(R.id.r_one_v_one_name_text_view_main_widget);
        mainButton = (TextView) findViewById(R.id.r_one_v_one_main_button_main_widget);
        defaultLogic = new DevicesDefaultLogic();
        buttonStage = false;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");

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

//        background.setOnLongClickListener(new View.OnLongClickListener(){
//            @Override
//            public boolean onLongClick(View view) {
//                Log.i("LONGCLICK", "LONGCLICK");
//                deleteFromFirebase();
//                deleteUi();
//                return false;
//            }
//        });

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
                    String currentTime = defaultLogic.getDate();
                    if (flag) {
                        defaultLogic.insertDataROneVOne(1, email, getName(), getType());
                        defaultLogic.updateAnalyticsDataROneVOne(email, name);
                    }
                    else {
                        defaultLogic.insertDataROneVOne(0, email, getName(), getType());
                        defaultLogic.updateAnalyticsDataROneVOne(email, name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        threadOn.start();
    }

    private void openRelayActivity(){
        Intent switchActivityIntent = new Intent(getContext(), ROneVOneActivity.class);
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
        String temp = "ROneVOne" + teb + info[1] + teb + info[2] + teb + info[3];
        return temp;
    }

    private void setParentList(LinearLayout layout){
        parentLayout = layout;
    }

    private void deleteUi(){
        parentLayout.removeView(this);
    }

    private void deleteFromFirebase(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        String []splitString = email.split("@");
        String name = splitString[0];
        defaultLogic.deleteDevice(name, getName());
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
}