package com.example.arduinoonoffremout.firebase;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class AnalyticsDefaultLogic {
    public void saveDay(){
        Long currentTime = Calendar.getInstance().getTime().getTime();
        Log.i("DAtE", currentTime.toString());
    }
}
