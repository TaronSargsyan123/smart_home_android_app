package com.revive.smarthome.bluetooth;

import static android.widget.Toast.LENGTH_LONG;

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
import android.widget.Toast;

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
import java.util.Arrays;

public class BluetoothDefaultLogic {

    private Context context;
    private OutputStream outputStream;
    private InputStream inStream;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice device;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private AppCompatActivity activityParent;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    public BluetoothDefaultLogic(Context context, AppCompatActivity activity) {
        this.context = context;
        this.activityParent = activity;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<>();

    }

    public void disableBluetooth() {
        if (btAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                btAdapter.disable();
            }

        }
    }

    public void enableBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activityResultLauncher.launch(intent);
    }


    public void send(BluetoothDevice tempDevice, String s){
        Log.i("SEND", "Send starting");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ParcelUuid[] uuids = tempDevice.getUuids();
            Log.i("SEND", "In the if");

            try (BluetoothSocket socket = tempDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid())) {
                socket.connect();
                outputStream = socket.getOutputStream();
                inStream = socket.getInputStream();

                outputStream.write(s.getBytes());
                Log.i("SEND", "Successfully data send");
                Toast.makeText(context, tempDevice.getName(), LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    public void setActivityParent(AppCompatActivity activity){
        activityParent = activity;

        activityResultLauncher = activityParent.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && btAdapter.isEnabled()) {

                        }
                        else {
                            enableBluetooth();
                        }
                    }
                });
    }



    public ArrayList<BluetoothDevice> refresh() {
        bluetoothDevices.clear();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            for (BluetoothDevice device : btAdapter.getBondedDevices()) {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    bluetoothDevices.add(device);
                }
            }
        }
        Log.i("SET", Arrays.toString(btAdapter.getBondedDevices().toArray()));


        return bluetoothDevices;
    }


    public ArrayList<BluetoothDevice> getBluetoothDevicesArray(){
        return bluetoothDevices;
    }

    public boolean isBluetoothEnable(){
        if (btAdapter.isEnabled()){
            return true;
        }else {
            return false;
        }
    }











}
