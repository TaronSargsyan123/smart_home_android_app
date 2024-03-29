package com.revive.smarthome.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.revive.smarthome.InternetConnectionErrorActivity;
import com.revive.smarthome.R;
import com.revive.smarthome.StartActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private TextView createAccountTextView;
    private TextView loginButton;
    private String email;
    private String password;
    private ProgressBar progressBar;
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
        progressBar = findViewById(R.id.login_progress_bar);
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.login_liner_layout);

        progressBar.setVisibility(View.INVISIBLE);



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
                login();
            }
        });



    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }


    private void login() {
        //check if user already exists go to start activity
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        loginButton.setEnabled(false);
        progressBar.setEnabled(false);
        loginButton.setText("");
        progressBar.setVisibility(View.VISIBLE);

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
                        if (mAuth.getCurrentUser().isEmailVerified()){
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                            startActivity(intent);

                            Log.i("LOGIN", "Success");

                        }else {
                            Toast.makeText(LoginActivity.this, "Please verify your mail", Toast.LENGTH_SHORT).show();
                        }

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