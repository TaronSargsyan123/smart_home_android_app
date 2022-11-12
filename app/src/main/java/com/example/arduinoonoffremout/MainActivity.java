package com.example.arduinoonoffremout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import com.example.arduinoonoffremout.components.ROneVOneMainWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addDevice;
    public LinearLayout layout;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    private Animation animShake;
    private int deviceID;
    private SharedPreferences.Editor sharedPrefEdit;
    private Set<String> widgetsSet;




    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(ActivityResult result) {


                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        try {
                            //TODO add device type choosing
                            String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                            String[] arrOfStr = accessMessage.split("/%");
                            deviceID++;
                            addROneVOne(arrOfStr[1], arrOfStr[0], "ROneVOne", String.valueOf(deviceID));
                        }catch (Exception e){
                            toastPrint("Fail");
                        }
                    }
                }
            });


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        animShake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        addDevice = (FloatingActionButton) findViewById(R.id.addDeviceMain);
        layout  = (LinearLayout) findViewById(R.id.mainActivityLayout);
        deviceID = 0;

        widgetsSet = new HashSet<String>();
        addROneVOne("Kitchen", "192.168.1.12", "ROneVOne", String.valueOf(deviceID));

        drawDevicesFromPrefs();

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), CreateDeviceActivity.class);
                mStartForResult.launch(switchActivityIntent);

            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addROneVOne(String name, String host, String type, String id){
        ROneVOneMainWidget onOffButton = new ROneVOneMainWidget(this);
        onOffButton.configNameAndHost(name, host);
        onOffButton.setType(type);
        onOffButton.setIDString(id);
        widgetsSet.add(onOffButton.getInfoString());
        layout.addView(onOffButton);
        shakeView(onOffButton);
        saveDeviceListInPrefs();

    }

    private void removeDeviceByID(int ID){
        //for (int i = 0; i < widgetsArray.size(); i++) {
        //    DefaultMainWidget temp = widgetsArray.get(i);
        //    if (temp.getID() == ID){
        //        removeDevice(temp);
        //        //TODO remove from widgetsArray
        //        saveDeviceListInPrefs();
        //    }
        //}


    }

    public void removeDevice(View view){
        layout.post(new Runnable() {
            public void run() {
                layout.removeView(view);
                saveDeviceListInPrefs();
            }
        });
    }

    private void toastPrint(CharSequence s){
        Context context = getApplicationContext();
        CharSequence text = s;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void shakeView(View view){
        view.setAnimation(animShake);
    }

    private void saveDeviceListInPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences("DevicesPref",MODE_PRIVATE);
        sharedPrefEdit = sharedPreferences.edit();
        sharedPrefEdit.putStringSet("Devices", widgetsSet);
        sharedPrefEdit.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawDevicesFromPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences("DevicesPref",MODE_PRIVATE);


        Set<String> tempSet = new HashSet<>();
        tempSet = sharedPreferences.getStringSet("Devices", null);




        for (Iterator<String> iterator = tempSet.iterator(); iterator.hasNext();) {
            String s = iterator.next();
            String[] words = s.split("@#*%");
            String type = words[0];
            if (type == "ROneVOne"){
                String id = words[1];
                String name = words[2];
                String host = words[3];
                addROneVOne(name, host, type, id);

                deviceID =Integer.parseInt(id);

            }
        }





    }


}