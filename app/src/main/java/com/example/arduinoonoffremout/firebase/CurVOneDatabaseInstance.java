package com.example.arduinoonoffremout.firebase;

public class CurVOneDatabaseInstance {
    String userName;
    String deviceName;
    String type;
    String stage;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }



    public CurVOneDatabaseInstance(String userName, String deviceName, String type, String stage) {
        this.userName = userName;
        this.deviceName = deviceName;
        this.type = type;
        this.stage = stage;

    }


}
