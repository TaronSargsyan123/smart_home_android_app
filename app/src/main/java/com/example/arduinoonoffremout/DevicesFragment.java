package com.example.arduinoonoffremout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arduinoonoffremout.components.CROne.CROneMainWidget;
import com.example.arduinoonoffremout.components.CurVOne.CurVOneMainWidget;
import com.example.arduinoonoffremout.components.DOne.DOneMainWidget;
import com.example.arduinoonoffremout.components.ROneVOne.ROneVOneMainWidget;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;


public class DevicesFragment extends Fragment {
    private FloatingActionButton addDevice;
    public LinearLayout layout;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    private SharedPreferences.Editor sharedPrefEdit;
    private ArrayList<DefaultMainWidget> widgetsArray;
    private MainWidgetsSerializer mainWidgetsSerializer;
    private TextView clearListButton;
    private ImageView imageView;
    private final String FILE_NAME ="localData.txt";// getString(R.string.save_file_name);;
    private TextView listIsEmptyTextView;
    private DevicesDefaultLogic devicesDefaultLogic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        addDevice = requireActivity().findViewById(R.id.add_device_main);
        layout  = view.findViewById(R.id.devices_fragment_layout);
        clearListButton = view.findViewById(R.id.clear_all_devices_fragment);
        widgetsArray = new ArrayList<>();
        mainWidgetsSerializer = new MainWidgetsSerializer();
        imageView = view.findViewById(R.id.no_devices_imageView_fragment_devices);
        listIsEmptyTextView = view.findViewById(R.id.listIs_empty_text_view);
        devicesDefaultLogic = new DevicesDefaultLogic();
        drawDevicesFromFile(FILE_NAME);
        drawImage();

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getContext(), CreateDeviceActivity.class);
                mStartForResult.launch(switchActivityIntent);

            }
        });


        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>Are you sure want to clear device list?</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        layout.removeAllViews();
                        Vibrator v = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            v.vibrate(500);
                        }
                        clearWidgetsList();
                        drawDevicesFromFile(FILE_NAME);
                        clearFromFirebase();
                        drawImage();
                    }
                });
                builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>No</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                builder.create();
                builder.show();

            }
        });


        return view;

    }


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(ActivityResult result) {
                    String deviceID = "0";


                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        try {
                            String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                            String[] arrOfStr = accessMessage.split("/%");
                            if (Objects.equals(arrOfStr[0], "ROneVOne")){
                                addROneVOne(arrOfStr[2], arrOfStr[1], "ROneVOne", String.valueOf(deviceID));

                            }else if (Objects.equals(arrOfStr[0], "CROne")){
                                addCROne(arrOfStr[2], arrOfStr[1], "CROne", String.valueOf(deviceID));

                            }else if(Objects.equals(arrOfStr[0], "CurVOne")){
                                addCurVOne(arrOfStr[2], arrOfStr[1], "CurVOne", String.valueOf(deviceID));

                            }else if(Objects.equals(arrOfStr[0], "DOne")){
                                addDOne(arrOfStr[2], arrOfStr[1], "DOne", String.valueOf(deviceID));

                            }
                        }catch (Exception e){
                            toastPrint("Fail");
                        }
                    }
                    drawImage();
                }
            });






    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addROneVOne(String name, String host, String type, String id){
        ROneVOneMainWidget onOffButton = new ROneVOneMainWidget(this.getContext());
        onOffButton.configNameAndHost(name, host);
        onOffButton.setType(type);
        onOffButton.setIDString(String.valueOf(id));
        Log.i("ID from add widget", id);
        widgetsArray.add(onOffButton);
        layout.addView(onOffButton);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addCROne(String name, String host, String type, String id){
        CROneMainWidget crOneMainWidget = new CROneMainWidget(this.getContext());
        crOneMainWidget.configNameAndHost(name, host);
        crOneMainWidget.setType(type);
        crOneMainWidget.setIDString(String.valueOf(id));
        Log.i("ID from add widget", id);
        widgetsArray.add(crOneMainWidget);
        layout.addView(crOneMainWidget);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addCurVOne(String name, String host, String type, String id){
        CurVOneMainWidget curVOneMainWidget = new CurVOneMainWidget(this.getContext());
        curVOneMainWidget.configNameAndHost(name, host);
        curVOneMainWidget.setType(type);
        curVOneMainWidget.setIDString(String.valueOf(id));
        Log.i("ID from add widget", id);
        widgetsArray.add(curVOneMainWidget);
        layout.addView(curVOneMainWidget);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addDOne(String name, String host, String type, String id){
        DOneMainWidget thVOneMainWidget = new DOneMainWidget(this.getContext());
        thVOneMainWidget.configNameAndHost(name, host);
        thVOneMainWidget.setType(type);
        thVOneMainWidget.setIDString(String.valueOf(id));
        widgetsArray.add(thVOneMainWidget);
        layout.addView(thVOneMainWidget);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());
    }


    private void toastPrint(CharSequence s){
        Context context = this.getContext();
        CharSequence text = s;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawDevicesFromFile(String fileName){
        String widgetString = mainWidgetsSerializer.load(fileName, this.requireContext());
        String[] lines = widgetString.split(System.lineSeparator());
        try {
            for (String line : lines) {
                try {
                    String[] separated = line.split("@");
                    String type = separated[0];
                    String id = separated[1];
                    String name = separated[2];
                    String host = separated[3];
                    Log.i("DEVICE TYPE", type);
                    if (Objects.equals(type, "ROneVOne")){
                        addROneVOne(name, host, type, id);
                    }else if (Objects.equals(type, "CROne")){
                        addCROne(name, host, type, id);
                    }else if (Objects.equals(type, "CurVOne")){
                        Log.i("CurVOne", "Here");
                        addCurVOne(name, host, type, id);
                    }else if (Objects.equals(type, "DOne")){
                        addDOne(name, host, type, id);
                    }
                    Log.i("WIDGETS", widgetString);
                }catch (Exception ignored){

                }

            }

        }catch (Exception e){
            Log.i("Main widgets loading", "File is empty");
        }
    }

    private void clearWidgetsList(){
        mainWidgetsSerializer.clearWidgetsArray(widgetsArray, FILE_NAME, this.requireContext());
    }

    private void drawImage(){
        try {
            if(widgetsArray.isEmpty()){
                clearListButton.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                listIsEmptyTextView.setVisibility(View.VISIBLE);
            }else {
                clearListButton.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                //((ViewGroup) imageView.getParent()).removeView(imageView);
                listIsEmptyTextView.setVisibility(View.GONE);
            }
        }catch (Exception ignore){
        }

    }

    private void clearFromFirebase(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation",getContext().MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        Log.i("EMAIL", email);
        devicesDefaultLogic.clearData(email);
    }


}