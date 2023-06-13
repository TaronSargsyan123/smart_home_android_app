package com.revive.smarthome.firebase;

// standard widget database instance
public class DefaultDatabaseInstance {
    String userName;
    String deviceName;
    String type;
    int stage;

    public DefaultDatabaseInstance(String userName, String deviceName, String type, int stage) {
        this.userName = userName;
        this.deviceName = deviceName;
        this.type = type;
        this.stage = stage;

    }

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


}
