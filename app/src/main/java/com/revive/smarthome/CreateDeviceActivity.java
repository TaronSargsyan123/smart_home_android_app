package com.revive.smarthome;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.color.MaterialColors;
import com.revive.smarthome.bluetooth.BluetoothDefaultLogic;

import java.util.ArrayList;
import java.util.Objects;

public class CreateDeviceActivity extends AppCompatActivity {
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
    private Boolean bluetoothSupport;
    private ProgressBar progressBar;
    private ParcelUuid uuid;
    String[] permissions = {"android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 80);
        }

        backgroundColor = MaterialColors.getColor(getApplicationContext(), R.attr.colorOnPrimary, Color.WHITE);
        green = Color.parseColor("green");
        progressBar = findViewById(R.id.add_device_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        Spinner dropdown = findViewById(R.id.spinner1);


        listView = (ListView) findViewById(R.id.bluetooth_items_list);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.devices, android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


        nameEditText = findViewById(R.id.name_edit_text);
        ssidEditText = findViewById(R.id.wifi_ssid_edit_text);
        passwordEditText = findViewById(R.id.wifi_password_editText);
        TextView back = findViewById(R.id.back_to_device_list);
        String type;

        try {
            bluetoothDefaultLogic = new BluetoothDefaultLogic(this, this);
            bluetoothDefaultLogic.setActivityParent(this);
            bluetoothSupport = true;
            if (bluetoothDefaultLogic.isBluetoothEnable()) {
                drawList();
                listViewItemClick();
            } else {
                bluetoothDefaultLogic.enableBluetooth();
            }
        } catch (Exception ignored) {
            Toast toast = Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT);
            toast.show();
            bluetoothSupport = false;
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView createButton = findViewById(R.id.add_device_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (device != null && bluetoothSupport) {
                    try {
                        String message = dropdown.getSelectedItem().toString() + "/%" + "0.0.0.0" + "/%" + nameEditText.getText().toString();

                        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation", Context.MODE_PRIVATE);
                        String email = sharedPreferences.getString("email", null);
                        String[] temp = email.split("@");
                        String deviceName = nameEditText.getText().toString();
                        String userName = temp[0];
                        String ssid = ssidEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        char separator = '/';
                        String s = ssid + separator + password + separator + userName + separator + deviceName;

                        bluetoothDefaultLogic.send(device, s, progressBar);


                        sendMessage(message);



                    } catch (Exception e) {
                        sendMessage("Fail");
                    }
                }else if (device == null && bluetoothSupport){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please select device", Toast.LENGTH_SHORT);
                    toast.show();

                    String message = dropdown.getSelectedItem().toString() + "/%" + "0.0.0.0" + "/%" + nameEditText.getText().toString();
                    sendMessage(message);
                } else if (!bluetoothSupport){
                    String message = dropdown.getSelectedItem().toString() + "/%" + "0.0.0.0" + "/%" + nameEditText.getText().toString();
                    sendMessage(message);
                }



            }
        });

    }

    private void listViewItemClick(){
        bluetoothDevices = bluetoothDefaultLogic.getBluetoothDevicesArray();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {}
                device = bluetoothDevices.get(i);
                Log.i("BLUE", device.getAddress() + " " + device.getName());
                if (lastTouchedView  != null) {
                    lastTouchedView.setBackgroundColor(backgroundColor);
                }
                view.setBackground(getApplicationContext().getDrawable(R.drawable.shape_green_button));
                Log.i("DEVICE", device.getAddress());
                lastTouchedView = view;

            }
        });

    }

    private void sendMessage(String message){

        Intent data = new Intent();
        data.putExtra(DevicesFragment.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        progressBar.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);


    }


    private void drawList() {
        bluetoothDevices = bluetoothDefaultLogic.refresh();
        Log.i("BLUE", bluetoothDevices.toString() +" ");
        ArrayList<String> temp = new ArrayList<>();
        for (BluetoothDevice device : bluetoothDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            }
            if (Objects.equals(device.getName().toString().replace("\\s+",""), "") || device.getName() == null) {
                temp.add(device.getAddress());
            } else {
                temp.add(device.getName());
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, temp);


        listView.setAdapter(adapter);
    }


}