package com.example.arduinoonoffremout.speech_recognizer;

import android.widget.Toast;

import com.example.arduinoonoffremout.DefaultMainWidget;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SpeechRecognizerDefaultLogic {
    private Boolean notFound = false;



    public Boolean voiceProcessing(ArrayList<String> text, ArrayList<DefaultMainWidget> widgetsArray){
        String command;
        String inputName;
        String name;
        String[] separated = text.get(0).split(" ");
        int length = separated.length;

        if(length == 2){
            command = separated[0];
            inputName = text.get(0).replace(command, "");
            inputName = inputName.trim();
            command = command.toLowerCase(Locale.ROOT);
        }else {
            command = separated[0] + " " + separated[1];
            inputName = text.get(0).replace(command, "");
            inputName = inputName.trim();
            command = command.toLowerCase(Locale.ROOT);

        }



        if (command.equals("включи") || command.equals("turn on")) {
            commandOn(widgetsArray, inputName);
        }else if (command.equals("выключи") || command.equals("отключи")  || command.equals("turn off")){
            commandOff(widgetsArray, inputName);
        }else if (Objects.equals(separated[1], "brightness")){
            commandChangeDimmerValue(widgetsArray, separated[0], separated[2]);
        }else if (Objects.equals(separated[0], "яркость")){
            commandChangeDimmerValue(widgetsArray, separated[1], separated[2]);
        }

        return notFound;

    }


    private void commandOn(ArrayList<DefaultMainWidget> widgetsArray, String inputName){
        String name;
        for (DefaultMainWidget defaultMainWidget : widgetsArray) {
            name = defaultMainWidget.getName();
            if (inputName.equals(name)){
                defaultMainWidget.on();
            }
            else {
                notFound = true;
            }
        }
    }

    private void commandOff(ArrayList<DefaultMainWidget> widgetsArray, String inputName){
        String name;
        for (DefaultMainWidget defaultMainWidget : widgetsArray) {
            name = defaultMainWidget.getName();
            if (inputName.equals(name)){
                defaultMainWidget.off();
            }else {
                notFound = true;
            }
        }
    }

    private void commandChangeDimmerValue(ArrayList<DefaultMainWidget> widgetsArray, String inputName, String value){
        String name;
        for (DefaultMainWidget defaultMainWidget : widgetsArray) {
            name = defaultMainWidget.getName();
            if (inputName.equals(name) && Objects.equals(defaultMainWidget.getType(), "DOne")){
                defaultMainWidget.addValue(value);
            }else {
                notFound = true;
            }
        }
    }

    private void commandSetColor(DefaultMainWidget defaultMainWidget){
        if (Objects.equals(defaultMainWidget.getType(), "CROne")){

        }
    }

    private void commandOpen(DefaultMainWidget defaultMainWidget){
        if (Objects.equals(defaultMainWidget.getType(), "CurVOne")){

        }
    }

    private void commandClose(DefaultMainWidget defaultMainWidget){
        if (Objects.equals(defaultMainWidget.getType(), "CurVOne")){

        }
    }


}
