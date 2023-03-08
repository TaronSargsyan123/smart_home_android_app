package com.revive.smarthome;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.revive.smarthome.firebase.LoginActivity;


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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>Are you sure want to logout?</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        logOut(getContext());
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