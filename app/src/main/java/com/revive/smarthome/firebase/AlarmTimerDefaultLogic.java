package com.revive.smarthome.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class AlarmTimerDefaultLogic  extends BroadcastReceiver {
    private String email;
    private String deviceName;
    private int stage;
    private String type;

    @Override
    public void onReceive(Context context, Intent intent) {
        // email/deviceName/stage/type
        String inputData = intent.getStringExtra("message");
        String[] splitData = inputData.split("/");



        email = splitData[1];
        deviceName = splitData[2];
        type = splitData[3];

        if (Objects.equals(type, "ROneVOne")){
            stage = Integer.parseInt(splitData[0]);
            setROneVOne(deviceName, email, type, stage);

        }else if (Objects.equals(type, "DOne")){
            try {
                Log.i("ARRBT", "here");
                stage = Integer.parseInt(splitData[0]);
                int dimming = Integer.parseInt(splitData[4]);
                setDOne(deviceName, email, type, stage, dimming);
            }catch (Exception ignored){}

        }else if (Objects.equals(type, "CROne")){
            try {
                stage = Integer.parseInt(splitData[0]);
                String color = splitData[4];
                setCROne(deviceName, email, type, stage, color);
            }catch (Exception ignored){}

        }else if (Objects.equals(type, "CurVOne")){
            stage = Integer.parseInt(splitData[0]);
            setCurVOne(deviceName, email, type, stage);
        }

    }



    private void setROneVOne(String deviceName, String email, String type, int stage){
        DevicesDefaultLogic devicesDefaultLogic = new DevicesDefaultLogic();
        devicesDefaultLogic.insertDataROneVOne(stage, email, deviceName, type);
    }

    private void setCurVOne(String deviceName, String email, String type, int stage){
        DevicesDefaultLogic devicesDefaultLogic = new DevicesDefaultLogic();
        if (stage == 1){
            devicesDefaultLogic.insertDataCurVOne("open", email, deviceName, type);
        }else {
            devicesDefaultLogic.insertDataCurVOne("close", email, deviceName, type);
        }
    }

    private void setDOne(String deviceName, String email, String type, int stage, int dimming){
        DevicesDefaultLogic devicesDefaultLogic = new DevicesDefaultLogic();
        devicesDefaultLogic.insertDataDOne(stage, email, deviceName, type , dimming);

    }

    private void setCROne(String deviceName, String email, String type, int stage, String color){
        DevicesDefaultLogic devicesDefaultLogic = new DevicesDefaultLogic();
        devicesDefaultLogic.insertDataCROne(stage, email, deviceName, type, color);
    }

}
