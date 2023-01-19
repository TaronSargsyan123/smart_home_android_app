package com.example.arduinoonoffremout;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.arduinoonoffremout.firebase.LoginActivity;
import com.example.arduinoonoffremout.firebase.RegisterActivity;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private Button logOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        logOut = view.findViewById(R.id.logOutButton);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(getContext());
            }
        });

        return view;
    }

    public static void logOut(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Authorisation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", null);
        editor.putString("password", null);
        editor.commit();

        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
        context.startActivity(intent);

    }




}