package com.example.arduinoonoffremout;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class MainWidgetsSerializer implements Serializable, SerializerInterface {
    FileOutputStream fos;
    ObjectOutputStream os;
    public transient Context myContext;

    public void save(String object, String fileName, Context context) {

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(object.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String load(String fileName, Context context){
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder("");

        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }

        }
        return sb.toString();


    }
    public void clear(String fileName){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveWidgets(ArrayList<ROneVOneMainWidget> arrayList, String FILE_NAME, Context context){
        StringBuilder sb = new StringBuilder();
        for(ROneVOneMainWidget widget : arrayList){
            String temp = widget.getInfoString();
            sb.append(temp).append("\n");
        }
        save(sb.toString(), FILE_NAME, context);
        Log.i("SAVE", sb.toString());
    }





}
