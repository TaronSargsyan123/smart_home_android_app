package com.example.arduinoonoffremout;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class MainWidgetsSerializer implements Serializable, SerializerInterface {
    FileOutputStream fos;
    ObjectOutputStream os;
    public transient Context myContext;

    public void save(ArrayList object, String fileName, Context context){


        //try {
//
        //    FileOutputStream fileOut = new FileOutputStream(fileName);
        //    ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        //    objectOut.writeObject(object);
        //    objectOut.close();
        //    System.out.println("The Object  was succesfully written to a file");
//
        //} catch (Exception ex) {
        //    ex.printStackTrace();
        //    Log.i("exxxxxxxxxxxx", ex.toString());
        //}
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            Log.i("Save", "Save start");
            os.writeObject(object);
            Log.i("Save", "Write");
            os.close();
            fos.close();
            Log.i("Save", "File is Created/Opened");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<DefaultMainWidget> load(String fileName, Context context){
        try {

            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<DefaultMainWidget> arrayList = (ArrayList<DefaultMainWidget>) is.readObject();
            is.close();
            fis.close();
            Log.i("Load", "File is Loaded");
            return arrayList;

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("EXXXXXXXXX", e.toString());


            return null;
        }


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



}
