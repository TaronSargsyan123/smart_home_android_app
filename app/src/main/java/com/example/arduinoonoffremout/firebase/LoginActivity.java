package com.example.arduinoonoffremout.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        createAccountTextView = findViewById(R.id.loginCreateAccountTextView);
        loginButton = findViewById(R.id.loginLoginButton);
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.loginLinerLayout);



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
            Log.i("LOGIN", "fields is empty");
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

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
}