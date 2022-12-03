package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateDeviceActivity extends AppCompatActivity {
    //static final String ACCESS_MESSAGE="ACCESS_MESSAGE";


    private EditText nameEditText;
    private EditText hostEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        Spinner dropdown = findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.devices, android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


        nameEditText = findViewById(R.id.nameEditText);
        hostEditText = findViewById(R.id.hostEditText);
        TextView back = findViewById(R.id.backToDeviceList);
        String type;
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
                try {
                    String message = dropdown.getSelectedItem().toString() + "/%" + hostEditText.getText().toString() + "/%" + nameEditText.getText().toString();
                    sendMessage(message);
                }catch (Exception e){
                    sendMessage("Fail");
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


}