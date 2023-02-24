package com.example.arduinoonoffremout;

import android.animation.ValueAnimator;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.example.arduinoonoffremout.firebase.AnalyticsDefaultLogic;
import com.example.arduinoonoffremout.firebase.DevicesDefaultLogic;
import com.example.arduinoonoffremout.speech_recognizer.SpeechRecognizerDefaultLogic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;
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
    private TextView voiceControlButton;
    private TextView designTextView;
    private TextView backUpBarMenu;
    private TextView openUpBarMenu;
    private int designTextViewWidth;
    private int openUpBarMenuWidth;
    private ConstraintLayout constraintLayout;
    private Animation fade;
    private SpeechRecognizerDefaultLogic speechRecognizerDefaultLogic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        constraintLayout = view.findViewById(R.id.parent_fragment_devices);
        addDevice = requireActivity().findViewById(R.id.add_device_main);
        layout  = view.findViewById(R.id.devices_fragment_layout);
        clearListButton = view.findViewById(R.id.clear_all_devices_fragment);
        widgetsArray = new ArrayList<>();
        mainWidgetsSerializer = new MainWidgetsSerializer();
        imageView = view.findViewById(R.id.no_devices_imageView_fragment_devices);
        listIsEmptyTextView = view.findViewById(R.id.listIs_empty_text_view);
        devicesDefaultLogic = new DevicesDefaultLogic();
        voiceControlButton = view.findViewById(R.id.voice_control_button_devices_fragment);
        designTextView = view.findViewById(R.id.design_text_view_devices_fragment);
        backUpBarMenu = view.findViewById(R.id.up_bar_back_arrow_devices_fragment);
        openUpBarMenu = view.findViewById(R.id.open_menu_devices_fragment);
        designTextViewWidth = designTextView.getWidth();
        openUpBarMenuWidth = openUpBarMenu.getWidth();
        fade = AnimationUtils.loadAnimation(getContext(), R.anim.fade_up);
        speechRecognizerDefaultLogic = new SpeechRecognizerDefaultLogic();
        drawDevicesFromFile(FILE_NAME);
        drawImage();
        closeMenu();

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getContext(), CreateDeviceActivity.class);
                mStartForResult.launch(switchActivityIntent);

            }
        });

        backUpBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });

        openUpBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
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

        voiceControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                voiceControlActivityResultLauncher.launch(intent);
            }
        });



        return view;

    }



    ActivityResultLauncher<Intent> voiceControlActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Boolean notFound;
                Intent data = result.getData();
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                notFound = speechRecognizerDefaultLogic.voiceProcessing(text, widgetsArray);

                if (notFound){
                    Toast toast = Toast.makeText(getContext(), "Device not found", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }
    });




    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(ActivityResult result) {
                    String deviceID = "0";


                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        try {
                            View view = null;
                            String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                            String[] arrOfStr = accessMessage.split("/%");
                            if (Objects.equals(arrOfStr[0], "ROneVOne")){
                                view = addROneVOne(arrOfStr[2], arrOfStr[1], "ROneVOne", String.valueOf(deviceID));

                            }else if (Objects.equals(arrOfStr[0], "CROne")){
                                view = addCROne(arrOfStr[2], arrOfStr[1], "CROne", String.valueOf(deviceID));

                            }else if(Objects.equals(arrOfStr[0], "CurVOne")){
                                view = addCurVOne(arrOfStr[2], arrOfStr[1], "CurVOne", String.valueOf(deviceID));

                            }else if(Objects.equals(arrOfStr[0], "DOne")){
                                view = addDOne(arrOfStr[2], arrOfStr[1], "DOne", String.valueOf(deviceID));

                            }

                            try {
                                view.startAnimation(fade);
                                view.setAlpha(1);
                            }catch (Exception ignored){}


                        }catch (Exception e){
                            toastPrint("Fail");
                        }
                    }
                    drawImage();
                }
            });






    @RequiresApi(api = Build.VERSION_CODES.M)
    public ROneVOneMainWidget addROneVOne(String name, String host, String type, String id){
        ROneVOneMainWidget onOffButton = new ROneVOneMainWidget(this.getContext());
        onOffButton.configNameAndHost(name, host);
        onOffButton.setType(type);
        onOffButton.setIDString(String.valueOf(id));
        Log.i("ID from add widget", id);
        widgetsArray.add(onOffButton);
        layout.addView(onOffButton);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());

        return onOffButton;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CROneMainWidget addCROne(String name, String host, String type, String id){
        CROneMainWidget crOneMainWidget = new CROneMainWidget(this.getContext());
        crOneMainWidget.configNameAndHost(name, host);
        crOneMainWidget.setType(type);
        crOneMainWidget.setIDString(String.valueOf(id));
        Log.i("ID from add widget", id);
        widgetsArray.add(crOneMainWidget);
        layout.addView(crOneMainWidget);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());
        return crOneMainWidget;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CurVOneMainWidget addCurVOne(String name, String host, String type, String id){
        CurVOneMainWidget curVOneMainWidget = new CurVOneMainWidget(this.getContext());
        curVOneMainWidget.configNameAndHost(name, host);
        curVOneMainWidget.setType(type);
        curVOneMainWidget.setIDString(String.valueOf(id));
        Log.i("ID from add widget", id);
        widgetsArray.add(curVOneMainWidget);
        layout.addView(curVOneMainWidget);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());
        return curVOneMainWidget;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public DOneMainWidget addDOne(String name, String host, String type, String id){
        DOneMainWidget dOneMainWidget = new DOneMainWidget(this.getContext());
        dOneMainWidget.configNameAndHost(name, host);
        dOneMainWidget.setType(type);
        dOneMainWidget.setIDString(String.valueOf(id));
        widgetsArray.add(dOneMainWidget);
        layout.addView(dOneMainWidget);
        mainWidgetsSerializer.saveWidgets(widgetsArray, FILE_NAME, this.requireContext());
        return dOneMainWidget;
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
                voiceControlButton.setVisibility(View.INVISIBLE);
                designTextView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                listIsEmptyTextView.setVisibility(View.VISIBLE);
            }else {
                clearListButton.setVisibility(View.VISIBLE);
                voiceControlButton.setVisibility(View.VISIBLE);
                designTextView.setVisibility(View.VISIBLE);
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

    private void openMenu(){
        int maxWidth = designTextViewWidth;
        int minWidth = openUpBarMenuWidth;

        openUpBarMenu.setVisibility(View.INVISIBLE);
        designTextView.setText(getResources().getString(R.string.menu));
        backUpBarMenu.setVisibility(View.VISIBLE);
        voiceControlButton.setVisibility(View.VISIBLE);
        clearListButton.setVisibility(View.VISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.design_text_view_devices_fragment,ConstraintSet.END,R.id.parent_fragment_devices,ConstraintSet.END,32);
        constraintSet.applyTo(constraintLayout);

        animate(designTextView, 1, maxWidth, 0);

    }

    private void closeMenu(){
        openUpBarMenu.setVisibility(View.VISIBLE);
        designTextView.setText("");
        backUpBarMenu.setVisibility(View.INVISIBLE);
        voiceControlButton.setVisibility(View.INVISIBLE);
        clearListButton.setVisibility(View.INVISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.clear(R.id.design_text_view_devices_fragment,ConstraintSet.END);
        constraintSet.applyTo(constraintLayout);

        animate(designTextView, 1, 110, 500);


    }

    private void animate(View view, int type, int size, int duration){
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(), size);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (type==1){
                    layoutParams.width = val;
                }else if (type==2){
                    layoutParams.height = val;
                }

                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.start();

    }



}