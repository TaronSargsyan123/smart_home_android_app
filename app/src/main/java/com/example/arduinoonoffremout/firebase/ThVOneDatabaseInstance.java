package com.example.arduinoonoffremout.firebase;

public class ThVOneDatabaseInstance {
    private String userName;
    private String deviceName;
    private String type;
    private String temperature;
    private String humidity;

    public ThVOneDatabaseInstance(String userName, String deviceName, String type, String temperature, String humidity) {
        this.userName = userName;
        this.deviceName = deviceName;
        this.type = type;
        this.temperature = temperature;
        this.humidity = humidity;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


}
