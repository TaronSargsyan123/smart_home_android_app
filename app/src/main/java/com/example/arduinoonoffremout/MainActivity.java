package com.example.arduinoonoffremout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.arduinoonoffremout.components.ROneVOneMainWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addDevice;
    public LinearLayout layout;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    private ArrayList<Object> widgetsArray = new ArrayList<>();
    private Animation animShake;


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(ActivityResult result) {


                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        try {
                            String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                            String[] arrOfStr = accessMessage.split("/%");
                            addSingleChannelRelayFirstVersion(arrOfStr[1], arrOfStr[0]);
                        }catch (Exception e){
                            toastPrint("Fail");
                        }
                    }
                }
            });


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        addDevice = (FloatingActionButton) findViewById(R.id.addDeviceMain);
        layout  = (LinearLayout) findViewById(R.id.mainActivityLayout);

        addSingleChannelRelayFirstVersion("Kitchen", "192.168.1.12");

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), CreateDeviceActivity.class);
                mStartForResult.launch(switchActivityIntent);

            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addSingleChannelRelayFirstVersion(String name, String host){
        ROneVOneMainWidget onOffButton = new ROneVOneMainWidget(this);
        onOffButton.configNameAndHost(name, host);
        widgetsArray.add(onOffButton.getInfo());
        layout.addView(onOffButton);
        shakeView(onOffButton);

    }

    private void toastPrint(CharSequence s){
        Context context = getApplicationContext();
        CharSequence text = s;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void shakeView(View view){
        view.setAnimation(animShake);
    }


}