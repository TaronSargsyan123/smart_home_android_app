package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    FragmentContainerView fragmentContainerView;
    private final String HOME_TAG = "HOME_FRAGMENT";
    private final String DEVICES_TAG = "DEVICES_FRAGMENT";







    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView widgetsFragmentButton = (TextView) findViewById(R.id.devicesFragmentButtonOnStartActivity);
        TextView homeFragmentButton = (TextView) findViewById(R.id.homeFragmentButtonOnStartActivity);


        widgetsFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentDevices();
            }
        });
        homeFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentHome();
            }
        });
        setFragmentDevices();

    }


    private void setFragmentDevices(){
        clearFragmentContainer(HOME_TAG);
        clearFragmentContainer(DEVICES_TAG);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainActivityFragmentContainer, DevicesFragment.class, null, DEVICES_TAG)
                .commit();

    }


    private void setFragmentHome(){
        clearFragmentContainer(DEVICES_TAG);
        clearFragmentContainer(HOME_TAG);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainActivityFragmentContainer, HomeFragment.class, null, HOME_TAG)
                .commit();

    }

    private void clearFragmentContainer(String tag){
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);//"HOME_FRAGMENT");
            getSupportFragmentManager().beginTransaction().remove(fragment).commit() ;
        }catch (Exception e){
            Log.i("Fragment exception", e.toString());
        }

    }
}