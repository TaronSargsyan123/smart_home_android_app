package com.example.arduinoonoffremout;

import android.content.Context;

import java.util.ArrayList;

public interface SerializerInterface {
    public void save(ArrayList object, String fileName, Context context);
    public ArrayList<DefaultMainWidget> load(String fileName, Context context);
    public void clear(String fileName);
}
