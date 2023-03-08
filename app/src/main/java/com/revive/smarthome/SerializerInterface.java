package com.revive.smarthome;

import android.content.Context;

public interface SerializerInterface {
    public void save(String object, String fileName, Context context);
    public String load(String fileName, Context context);
    public void clear(String fileName);
}
