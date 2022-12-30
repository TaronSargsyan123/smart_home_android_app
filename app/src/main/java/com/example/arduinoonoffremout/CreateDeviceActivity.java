package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import com.example.arduinoonoffremout.bluetooth.BluetoothDefaultLogic;
import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.Objects;

public class CreateDeviceActivity extends AppCompatActivity  {
    private ListView listView;
    private EditText nameEditText;
    private EditText ssidEditText;
    private EditText passwordEditText;
    private BluetoothDefaultLogic bluetoothDefaultLogic;
    private BluetoothDevice device;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private View lastTouchedView;
    private int green;
    private int backgroundColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        backgroundColor = MaterialColors.getColor(getApplicationContext(), R.attr.colorOnPrimary, Color.WHITE);
        green = Color.parseColor("green");


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
                    if (lastTouchedView  != null) {
                        lastTouchedView.setBackgroundColor(backgroundColor);
                    }
                    view.setBackground(getApplicationContext().getDrawable(R.drawable.shape_green_button));
                    Log.i("DEVICE", device.getAddress());
                    lastTouchedView = view;

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
                if (Objects.equals(device.getName().toString().replace("\\s+",""), "") || device.getName() == null) {
                    temp.add(device.getAddress());
                } else {
                    temp.add(device.getName());
                }
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, temp);


        listView.setAdapter(adapter);
    }



}