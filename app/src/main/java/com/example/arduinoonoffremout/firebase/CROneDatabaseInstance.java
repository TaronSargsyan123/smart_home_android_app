package com.example.arduinoonoffremout.firebase;

public class CROneDatabaseInstance {
    String userName;
    String deviceName;
    String type;
    int stage;
    String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CROneDatabaseInstance(String userName, String deviceName, String type, int stage, String color) {
        this.userName = userName;
        this.deviceName = deviceName;
        this.type = type;
        this.stage = stage;
        this.color = color;
    }
}
