package com.example.arduinoonoffremout.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arduinoonoffremout.InternetConnectionErrorActivity;
import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private TextView createAccountTextView;
    private TextView loginButton;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private LinearLayout register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_email_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        createAccountTextView = findViewById(R.id.login_create_account_textView);
        loginButton = findViewById(R.id.login_login_button);
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.login_liner_layout);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAd();
                login();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
        String emailLocal = sharedPreferences.getString("email", null);
        String passwordLocal = sharedPreferences.getString("password", null);

        if (emailLocal != null && passwordLocal != null){
            Log.i("AAAAAA", emailLocal);
            Log.i("AAAAAA", passwordLocal);
            startStartActivity();
        }

    }

    private void startStartActivity(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }

    private void login() {
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        email = Objects.requireNonNull(emailEditText.getEditText()).getText().toString();
        password = Objects.requireNonNull(passwordEditText.getEditText()).getText().toString();
        if (email.isEmpty() || password.isEmpty()){
            Log.i("LOGIN", "Please enter login and password");
            Toast toast = Toast.makeText(this, "Please enter login and password", Toast.LENGTH_SHORT);
            toast.show();
        }else if (!checkWifiConnection()){
            startInternetConnectionErrorActivity();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        loginButton.setEnabled(false);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                        startActivity(intent);

                        Log.i("LOGIN", "Success");

                    }else {
                        //TODO complete Listener
                        Log.i("LOGIN", "Complete error");
                    }
                }
            });
        }

    }

    private void startInternetConnectionErrorActivity(){
        Intent intent = new Intent(getApplicationContext(), InternetConnectionErrorActivity.class);
        startActivity(intent);
    }

    private Boolean checkWifiConnection(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}