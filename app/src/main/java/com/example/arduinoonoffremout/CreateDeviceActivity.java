package com.example.arduinoonoffremout;

import static android.widget.Toast.LENGTH_LONG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arduinoonoffremout.bluetooth.BluetoothDefaultLogic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateDeviceActivity extends AppCompatActivity  {
    //static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    private ListView listView;
    private EditText nameEditText;
    private EditText ssidEditText;
    private EditText passwordEditText;
    private BluetoothDefaultLogic bluetoothDefaultLogic;
    private BluetoothDevice device;
    private ArrayList<BluetoothDevice> bluetoothDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        Spinner dropdown = findViewById(R.id.spinner1);


        listView = (ListView) findViewById(R.id.bluetoothItemsList);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.devices, android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        bluetoothDefaultLogic = new BluetoothDefaultLogic(this, this);

        bluetoothDefaultLogic.setActivityParent(this);
        nameEditText = findViewById(R.id.nameEditText);
        ssidEditText = findViewById(R.id.wifiSSidEditText);
        passwordEditText = findViewById(R.id.wifiPasswordEditText);
        TextView back = findViewById(R.id.backToDeviceList);
        String type;

        if (bluetoothDefaultLogic.isBluetoothEnable()){
            drawList();
            listViewItemClick();
        }else {
            bluetoothDefaultLogic.enableBluetooth();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button createButton = findViewById(R.id.addDeviceButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (device != null) {
                    try {
                        String message = dropdown.getSelectedItem().toString() + "/%" + "0.0.0.0" + "/%" + nameEditText.getText().toString();
                        sendMessage(message);
                        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", Context.MODE_PRIVATE);
                        String email = sharedPreferences.getString("email", null);
                        String []temp = email.split("@");
                        String deviceName = nameEditText.getText().toString();
                        String userName = temp[0];
                        String ssid = ssidEditText.getText().toString();
                        String password =  passwordEditText.getText().toString();
                        String s = userName + "$" + deviceName + "$" + ssid + "$" + password;

                        bluetoothDefaultLogic.send(device, s);
                        //disableBluetooth();
                    } catch (Exception e) {
                        sendMessage("Fail");
                    }
                }


            }
        });

    }

    private void listViewItemClick(){
        bluetoothDevices = bluetoothDefaultLogic.getBluetoothDevicesArray();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    device = bluetoothDevices.get(i);
                    Log.i("DEVICE", device.getAddress());

                }

            }
        });

    }

    private void sendMessage(String message){

        Intent data = new Intent();
        data.putExtra(DevicesFragment.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();

    }









    private void drawList() {
        bluetoothDevices = bluetoothDefaultLogic.getBluetoothDevicesArray();
        bluetoothDefaultLogic.refresh();
        ArrayList<String> temp = new ArrayList<>();
        for (BluetoothDevice device : bluetoothDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                temp.add(device.getName() + " " + device.getAddress());
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, temp);


        listView.setAdapter(adapter);
    }



}