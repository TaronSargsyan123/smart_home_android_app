package com.revive.smarthome;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DefaultMainWidget extends LinearLayout {
    private String ID;
    private String type;

    public DefaultMainWidget(Context context) {
        super(context);
    }

    public DefaultMainWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultMainWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DefaultMainWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getID(){
        return ID;
    }

    public void setIDString(String IDString){
        this.ID = IDString;
    }


    public String getIdString(){
        Log.i("ID from MainWidget", getID());
        return String.valueOf(getId());}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfoString(){
        return null;
    }


    public String getName(){
        return null;
    }

    public void on(){}

    public void off(){}

    public void addValue(String value){}




}
