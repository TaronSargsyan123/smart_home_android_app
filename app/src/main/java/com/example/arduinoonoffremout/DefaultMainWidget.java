package com.example.arduinoonoffremout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DefaultMainWidget extends LinearLayout {
    private int ID;



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

    public int getID(){
        return ID;
    }

    public void setIDString(String IDString){
        this.ID = Integer.parseInt(IDString);
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public String getIdString(){return String.valueOf(getId());}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
