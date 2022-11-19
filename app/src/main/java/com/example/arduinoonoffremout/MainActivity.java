package com.example.arduinoonoffremout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.example.arduinoonoffremout.components.ROneVOneMainWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    FragmentContainerView fragmentContainerView;
    private TextView homeFragmentButton;
    private TextView widgetsFragmentButton;
    private final String HOME_TAG = "HOME_FRAGMENT";
    private final String DEVICES_TAG = "DEVICES_FRAGMENT";







    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        widgetsFragmentButton = (TextView) findViewById(R.id.devicesFragmentButtonOnMainActivity);
        homeFragmentButton = (TextView) findViewById(R.id.homeFragmentButtonOnMainActivity);


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

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainActivityFragmentContainer, DevicesFragment.class, null, DEVICES_TAG)
                .commit();

    }


    private void setFragmentHome(){
        clearFragmentContainer(DEVICES_TAG);

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
            Log.i("Fragment excaption", e.toString());
        }

    }









}