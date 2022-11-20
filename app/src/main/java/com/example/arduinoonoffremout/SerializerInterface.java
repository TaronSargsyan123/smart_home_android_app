package com.example.arduinoonoffremout;

import android.content.Context;

import java.util.ArrayList;

public interface SerializerInterface {
    public void save(String object, String fileName, Context context);
    public String load(String fileName, Context context);
    public void clear(String fileName);
}
