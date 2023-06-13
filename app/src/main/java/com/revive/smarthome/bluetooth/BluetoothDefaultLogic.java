package com.revive.smarthome.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothDefaultLogic {

    private Context context;
    private OutputStream outputStream;
    private InputStream inStream;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice device;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private AppCompatActivity activityParent;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private BluetoothSocket socket;
    Boolean bool = false;


    public BluetoothDefaultLogic(Context context, AppCompatActivity activity) {
        this.context = context;
        this.activityParent = activity;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<>();

    }

    // disable device bluetooth
    public void disableBluetooth() {
        if (btAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                btAdapter.disable();
            }

        }
    }

    // enable device bluetooth
    public void enableBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activityResultLauncher.launch(intent);
    }


    //check if bluetooth disabled then enable bluetooth and send data to bluetooth device
    public void send(BluetoothDevice tempDevice, String s, ProgressBar progressBar) {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}

        ParcelUuid[] uuids = tempDevice.getUuids();

        for (ParcelUuid uuid : uuids) {
            Log.d("BLUE", "UUID: " + uuid.getUuid().toString());
        }
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}
                    try {

                        socket = tempDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        socket.connect();
                        outputStream = socket.getOutputStream();
                        inStream = socket.getInputStream();
                        outputStream.write(s.getBytes());
                        Log.i("SEND", "Successfully data send");
                        bool = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            });
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void setActivityParent(AppCompatActivity activity) {
        activityParent = activity;

        activityResultLauncher = activityParent.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && btAdapter.isEnabled()) {

                        } else {
                            enableBluetooth();
                        }
                    }
                });
    }


    //refresh devices list
    public ArrayList<BluetoothDevice> refresh() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            Log.i("BLUE", "here1");
            bluetoothDevices.add(device);
        }
        return bluetoothDevices;
    }






    //return bluetooth devices list
    public ArrayList<BluetoothDevice> getBluetoothDevicesArray(){
        return bluetoothDevices;
    }

    //check if bluetooth enabled return true else return false
    public boolean isBluetoothEnable(){
        if (btAdapter.isEnabled()){
            return true;
        }else {
            return false;
        }
    }











}
