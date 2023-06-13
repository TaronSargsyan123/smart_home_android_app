package com.revive.smarthome;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.revive.smarthome.firebase.LoginActivity;


public class ProfileFragment extends Fragment {

    private Button logOut;
    private Button privacyPolicyButton;
    private Button contactUs;
    private Button aboutUs;
    private TextView emailText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        privacyPolicyButton = view.findViewById(R.id.privacy_policy_button);
        logOut = view.findViewById(R.id.logOutButton);
        contactUs = view.findViewById(R.id.contact_us_button);
        aboutUs = view.findViewById(R.id.about_us_button);
        emailText = view.findViewById(R.id.email_text);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Authorisation", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        emailText.setText("Your Email: " + email);

        //show alert dialog when user click to log out button
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

        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPrivacyPolicy();
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutUs(getContext());
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUs();
            }
        });
        return view;
    }

    //log out and go to login activity
    public static void logOut(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Authorisation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", null);
        editor.putString("password", null);
        editor.commit();

        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
        context.startActivity(intent);

    }

    //buttons logic

    private void openPrivacyPolicy(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1hyY9sVR-TtVyyz6crr1ZIngMsn_msgX4/view?usp=sharing"));
        startActivity(browserIntent);
    }

    private void aboutUs(Context context){
        Intent intent = new Intent(context.getApplicationContext(), AboutUs.class);
        context.startActivity(intent);
    }

    private void contactUs(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/revivesmarthome"));
        startActivity(browserIntent);

    }




}