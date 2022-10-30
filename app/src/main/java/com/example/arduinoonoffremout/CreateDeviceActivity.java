package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arduinoonoffremout.components.OnOffButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateDeviceActivity extends AppCompatActivity {
    private TextView back;
    private Button createButton;


    private EditText nameEditText;
    private EditText hostEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);
        nameEditText = findViewById(R.id.nameEditText);
        hostEditText = findViewById(R.id.hostEditText);
        back = findViewById(R.id.backToDeviceList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        createButton = findViewById(R.id.addDeviceButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String message =hostEditText.getText().toString() + "/%" + nameEditText.getText().toString();
                sendMessage(message);
            }
        });


    }

    private void sendMessage(String message){

        Intent data = new Intent();
        data.putExtra(MainActivity.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();
    }


}