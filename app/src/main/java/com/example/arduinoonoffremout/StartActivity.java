package com.example.arduinoonoffremout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StartActivity extends AppCompatActivity {
    private final String HOME_TAG = "HOME_FRAGMENT";
    private final String DEVICES_TAG = "DEVICES_FRAGMENT";
    private FloatingActionButton addDeviceMain;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        addDeviceMain = findViewById(R.id.addDeviceMain);

        TextView widgetsFragmentButton = (TextView) findViewById(R.id.devicesFragmentButtonOnStartActivity);
        TextView profileFragmentButton = (TextView) findViewById(R.id.profileFragmentButtonOnStartActivity);


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


    private void setFragmentDevices(){
        clearFragmentContainer(HOME_TAG);
        clearFragmentContainer(DEVICES_TAG);
        addDeviceMain.setEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.mainActivityFragmentContainer, DevicesFragment.class, null, DEVICES_TAG)
                .commit();

    }


    private void setFragmentProfile(){
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
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);//"HOME_FRAGMENT");
            getSupportFragmentManager().beginTransaction().remove(fragment).commit() ;
        }catch (Exception e){
            Log.i("Fragment exception", e.toString());
        }

    }
}