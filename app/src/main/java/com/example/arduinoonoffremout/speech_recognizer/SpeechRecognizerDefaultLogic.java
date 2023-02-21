package com.example.arduinoonoffremout.speech_recognizer;

import android.widget.Toast;

import com.example.arduinoonoffremout.DefaultMainWidget;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognizerDefaultLogic {

    public Boolean voiceProcessing(ArrayList<String> text, ArrayList<DefaultMainWidget> widgetsArray){
        String name;
        String[] separated = text.get(0).split(" ");

        String command = separated[0];
        String inputName = text.get(0).replace(command, "");
        inputName = inputName.trim();
        command = command.toLowerCase(Locale.ROOT);
        Boolean notFound = false;
        if (command.equals("включи")) {
            for (DefaultMainWidget defaultMainWidget : widgetsArray) {
                name = defaultMainWidget.getName();
                System.out.println(name);
                System.out.println(inputName);
                if (inputName.equals(name)){
                    System.out.println("Here");
                    defaultMainWidget.on();
                }
                else {
                    notFound = true;
                }
            }
        }else if (command.equals("выключи")){
            for (DefaultMainWidget defaultMainWidget : widgetsArray) {
                name = defaultMainWidget.getName();
                if (inputName.equals(name)){
                    defaultMainWidget.off();
                }else {
                    notFound = true;
                }
            }
        }

        return notFound;

    }



}
