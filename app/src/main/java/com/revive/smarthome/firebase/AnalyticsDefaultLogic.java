package com.revive.smarthome.firebase;

import android.util.Log;

import java.util.Calendar;

public class AnalyticsDefaultLogic {
    public void saveDay(){
        Long currentTime = Calendar.getInstance().getTime().getTime();
        Log.i("DAtE", currentTime.toString());
    }
}
