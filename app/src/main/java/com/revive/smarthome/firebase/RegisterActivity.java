package com.revive.smarthome.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.revive.smarthome.EmailVerification;
import com.revive.smarthome.InternetConnectionErrorActivity;
import com.revive.smarthome.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout emailEditText;
    private TextInputLayout passwordEditText;
    private TextInputLayout confirmPasswordEditText;
    private TextView registerButton;
    private TextView back;
    private String email;
    private String password;
    private String confirmPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.register_email_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        confirmPasswordEditText = findViewById(R.id.register_confirm_password_editText);
        registerButton = findViewById(R.id.register_register_button);
        back = findViewById(R.id.register_back);
        progressBar = findViewById(R.id.register_progress_bar);
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // get mail, password and register new user in firebase after email verification
    private void register(){
        email = String.valueOf(Objects.requireNonNull(emailEditText.getEditText()).getText());
        password = String.valueOf(Objects.requireNonNull(passwordEditText.getEditText()).getText());
        confirmPassword = String.valueOf(Objects.requireNonNull(confirmPasswordEditText.getEditText()).getText());

        registerButton.setEnabled(false);
        progressBar.setEnabled(false);
        registerButton.setText("");
        progressBar.setVisibility(View.VISIBLE);

        if (password.equals(confirmPassword)) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(getApplicationContext(), EmailVerification.class);
                                            intent.putExtra("CONFIRM", email);
                                            startActivity(intent);
                                        } else {
                                            Log.e("REGISTER", "sendEmailVerification", task.getException());
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });//if wifi not connected start InternetConnectionErrorActivity
                            }else if (!checkWifiConnection()){
                                startInternetConnectionErrorActivity();
                            }else {

                                Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
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