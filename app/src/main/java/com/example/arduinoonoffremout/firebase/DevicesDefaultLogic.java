package com.example.arduinoonoffremout.firebase;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.example.arduinoonoffremout.DefaultMainWidget;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneAnalyticsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DevicesDefaultLogic {
    final FirebaseDatabase database = FirebaseDatabase.getInstance("https://revive-smart-home-692c2-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference usersRef = database.getReference("users");
    DatabaseReference analyticsRef = database.getReference("analytics");

    public void insertDataROneVOne(int stage, String email, String deviceName, String deviceType) {

        if (!Objects.equals(email, "")) {
            DefaultDatabaseInstance instance = new DefaultDatabaseInstance(email, deviceName, deviceType, stage);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }

    public void insertDataCROne(int stage, String email, String deviceName, String deviceType, String color) {

        if (!Objects.equals(email, "")) {
            CROneDatabaseInstance instance = new CROneDatabaseInstance(email, deviceName, deviceType, stage, color);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }

    public void insertDataCurVOne(String stage, String email, String deviceName, String deviceType) {

        if (!Objects.equals(email, "")) {
            CurVOneDatabaseInstance instance = new CurVOneDatabaseInstance(email, deviceName, deviceType, stage);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);
        }
    }

    public void insertDataThVOne(String email, String deviceName, String deviceType, String temperature, String humidity){
        if (!Objects.equals(email, "")) {
            ThVOneDatabaseInstance instance = new ThVOneDatabaseInstance(email, deviceName, deviceType, temperature, humidity);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);
        }

    }

    public void insertDataDOne(int stage, String email, String deviceName, String deviceType, int bright) {
        if (!Objects.equals(email, "")) {
            DOneDatabaseInstance instance = new DOneDatabaseInstance(email, deviceName, deviceType, stage, bright);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }

    public String readDataThVOne(String email, String deviceName){
        if (!Objects.equals(email, "")) {
            final String[] temp = new String[1];
            final String[] hum = new String[1];
            final String[][] arrOfStr = {email.split("@")};
            String userName = arrOfStr[0][0];

            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String temperature = snapshot.child(userName).child(deviceName).child("temperature").getValue().toString();
                        String humidity = snapshot.child(userName).child(deviceName).child("humidity").getValue().toString();
                        temp[0] = temperature;
                        hum[0] = humidity;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    temp[0] = null;
                    hum[0] = null;
                }
            });

            return Arrays.toString(temp) + "_" + Arrays.toString(hum);

        }else {
            return "null_null";
        }

    }

    private void insertAnalyticsData(String email, String deviceName, ArrayList<String> data){
        if (!Objects.equals(email, "")) {
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            analyticsRef.child(userName).child(deviceName).setValue(data);
        }
    }

    public void updateAnalyticsData(String email, String deviceName, String newData){
        if (!Objects.equals(email, "")) {

            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];

            analyticsRef.child(userName).child(deviceName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    ArrayList<String> temp;
                    if (!task.isSuccessful()) {
                        Log.e("ARR", "Error getting data", task.getException());
                    }
                    else {
                        try {
                            temp = (ArrayList<String>) task.getResult().getValue();
                            temp.add(newData);
                            insertAnalyticsData(email, deviceName, temp);
                        }catch (Exception ignored){
                            ArrayList<String> tempArrayList = new ArrayList<>();
                            tempArrayList.add(newData);
                            insertAnalyticsData(email, deviceName, tempArrayList);
                        }

                    }
                }
            });
        }
    }

    public void drawAnalytics(String email, String deviceName, TextView textView){
        if (!Objects.equals(email, "")) {

            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];

            analyticsRef.child(userName).child(deviceName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    ArrayList<String> temp;
                    if (!task.isSuccessful()) {
                        Log.e("ARR", "Error getting data", task.getException());
                    }
                    else {
                        temp = (ArrayList<String>) task.getResult().getValue();
                        String finalString = "";
                        for (String s: temp) {
                            finalString = finalString + "\n" + s;
                        }
                        textView.setText(finalString);

                    }
                }
            });
        }
    }



    public void deleteDevice(String userName, String deviceName){
        usersRef.child(userName).child(deviceName).removeValue();
    }

    public void clearData(String email){
        if (!Objects.equals(email, "")) {
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).removeValue();
        }
    }

}
