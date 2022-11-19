package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateDeviceActivity extends AppCompatActivity {
    //static final String ACCESS_MESSAGE="ACCESS_MESSAGE";


    private EditText nameEditText;
    private EditText hostEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);
        nameEditText = findViewById(R.id.nameEditText);
        hostEditText = findViewById(R.id.hostEditText);
        TextView back = findViewById(R.id.backToDeviceList);
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
                    String message =hostEditText.getText().toString() + "/%" + nameEditText.getText().toString();
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