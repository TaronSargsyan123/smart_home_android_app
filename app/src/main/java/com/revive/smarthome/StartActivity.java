package com.revive.smarthome;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StartActivity extends AppCompatActivity {
    private final String HOME_TAG = "HOME_FRAGMENT";
    private final String DEVICES_TAG = "DEVICES_FRAGMENT";
    private FloatingActionButton addDeviceMain;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // change fragments logic

        setContentView(R.layout.activity_start);

        addDeviceMain = findViewById(R.id.add_device_main);

        TextView widgetsFragmentButton = (TextView) findViewById(R.id.devices_fragment_button_on_start_activity);
        TextView profileFragmentButton = (TextView) findViewById(R.id.profile_fragment_button_on_start_activity);


        widgetsFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentDevices();
            }
        });
        profileFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragmentProfile();
            }
        });
        setFragmentDevices();

    }

    @Override
    public void onBackPressed() {
        // finish activity when back button pressed
        this.finishAffinity();
    }


    private void setFragmentDevices(){
        //clear all fragments and set devices fragment
        clearFragmentContainer(HOME_TAG);
        clearFragmentContainer(DEVICES_TAG);
        addDeviceMain.setEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainActivityFragmentContainer, DevicesFragment.class, null, DEVICES_TAG)
                .commit();

    }


    private void setFragmentProfile(){
        //clear all fragments and set profile fragment
        clearFragmentContainer(DEVICES_TAG);
        clearFragmentContainer(HOME_TAG);
        addDeviceMain.setEnabled(false);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainActivityFragmentContainer, ProfileFragment.class, null, HOME_TAG)
                .commit();

    }

    private void clearFragmentContainer(String tag){
        try {
            //try to clear fragment from fragment container
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            getSupportFragmentManager().beginTransaction().remove(fragment).commit() ;
        }catch (Exception e){
            Log.i("Fragment exception", e.toString());
        }

    }
}