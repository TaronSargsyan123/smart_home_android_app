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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateDeviceActivity extends AppCompatActivity  {
    //static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    BluetoothAdapter btAdapter;
    private ListView listView;

    private OutputStream outputStream;
    private InputStream inStream;
    private ArrayList<BluetoothDevice> bluetoothDevices;

    private EditText nameEditText;
    private EditText ssidEditText;
    private EditText passwordEditText;

    private BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        Spinner dropdown = findViewById(R.id.spinner1);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<>();
        listView = (ListView) findViewById(R.id.bluetoothItemsList);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.devices, android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


        nameEditText = findViewById(R.id.nameEditText);
        ssidEditText = findViewById(R.id.wifiSSidEditText);
        passwordEditText = findViewById(R.id.wifiPasswordEditText);
        TextView back = findViewById(R.id.backToDeviceList);
        String type;

        if (btAdapter.isEnabled()){
            drawList();
            listViewItemClick();
        }else {
            enableBluetooth();
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
                        send(device);
                        //disableBluetooth();
                    } catch (Exception e) {
                        sendMessage("Fail");
                    }
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


    private void disableBluetooth() {
        if (btAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                btAdapter.disable();
            }

        }
    }

    private void enableBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && btAdapter.isEnabled()) {
                        drawList();

                    }
                    else {
                        enableBluetooth();
                    }
                }
            });


    private ArrayList<BluetoothDevice> refresh() {
        bluetoothDevices.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            for (BluetoothDevice device : btAdapter.getBondedDevices()) {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    bluetoothDevices.add(device);
                }
            }
        }
        Log.i("SET", Arrays.toString(btAdapter.getBondedDevices().toArray()));


        return bluetoothDevices;
    }

    private void drawList() {
        refresh();



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

    private void listViewItemClick(){
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

    private void send(BluetoothDevice tempDevice){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            ParcelUuid[] uuids = tempDevice.getUuids();

            try (BluetoothSocket socket = tempDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid())) {
                socket.connect();
                outputStream = socket.getOutputStream();
                inStream = socket.getInputStream();
                SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
                String email = sharedPreferences.getString("email", null);
                String []temp = email.split("@");
                String deviceName = nameEditText.getText().toString();
                String userName = temp[0];
                String ssid = ssidEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String s = userName + "$" + deviceName + "$" + ssid + "$" + password;
                outputStream.write(s.getBytes());
                Toast.makeText(getApplicationContext(), tempDevice.getName(), LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }







}