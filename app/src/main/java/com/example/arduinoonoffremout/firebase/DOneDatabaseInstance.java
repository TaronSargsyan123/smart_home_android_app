package com.example.arduinoonoffremout.firebase;

public class DOneDatabaseInstance {
    String userName;
    String deviceName;
    String type;
    int stage;
    int bright;

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

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getBright() {
        return bright;
    }

    public void setBright(int bright) {
        this.bright = bright;
    }

    public DOneDatabaseInstance(String userName, String deviceName, String type, int stage, int bright) {
        this.userName = userName;
        this.deviceName = deviceName;
        this.type = type;
        this.stage = stage;
        this.bright = bright;
    }
}
