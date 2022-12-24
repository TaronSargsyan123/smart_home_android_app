package com.example.arduinoonoffremout.firebase;

import com.example.arduinoonoffremout.DefaultDatabaseInstance;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DevicesDefaultLogic {
    public void insertData(int stage, String email, String deviceName, String deviceType) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://revive-smart-home-692c2-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = database.getReference("");

        DatabaseReference usersRef = database.getReference("users");
        if (!Objects.equals(email, "")) {
            DefaultDatabaseInstance instance = new DefaultDatabaseInstance(email, deviceName, deviceType, stage);
            String[] arrOfStr = email.split("@");
            String userName = arrOfStr[0];
            usersRef.child(userName).child(deviceName).setValue(instance);

        }
    }
}
