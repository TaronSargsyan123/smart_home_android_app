package com.revive.smarthome.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        stage = Integer.parseInt(splitData[0]);
        email = splitData[1];
        deviceName = splitData[2];
        type = splitData[3];
        if (Objects.equals(type, "ROneVOne")){
            setROneVOne(deviceName, email, type, stage);
        }
    }

    private void setROneVOne(String deviceName, String email, String type, int stage){
        DevicesDefaultLogic devicesDefaultLogic = new DevicesDefaultLogic();
        devicesDefaultLogic.insertDataROneVOne(stage, email, deviceName, type);
    }
}
