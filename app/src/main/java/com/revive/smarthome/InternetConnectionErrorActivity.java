package com.revive.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.revive.smarthome.firebase.LoginActivity;

public class InternetConnectionErrorActivity extends AppCompatActivity {
    private TextView tryAgainButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_connection_error);
        tryAgainButton = findViewById(R.id.internet_connection_error_try_again_button);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWifiConnection()){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private Boolean checkWifiConnection(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}