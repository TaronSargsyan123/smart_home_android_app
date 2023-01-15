package com.example.arduinoonoffremout.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arduinoonoffremout.R;
import com.example.arduinoonoffremout.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.registerConfirmPasswordEditText);
        registerButton = findViewById(R.id.registerRegisterButton);
        back = findViewById(R.id.registerBack);
        mAuth = FirebaseAuth.getInstance();

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

    private void register(){
        SharedPreferences sharedPreferences = getSharedPreferences("Authorisation",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        email = String.valueOf(Objects.requireNonNull(emailEditText.getEditText()).getText());
        password = String.valueOf(Objects.requireNonNull(passwordEditText.getEditText()).getText());
        confirmPassword = String.valueOf(Objects.requireNonNull(confirmPasswordEditText.getEditText()).getText());


        if (password.equals(confirmPassword)) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.commit();
                                Log.d("REGISTER", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                startActivity(intent);
                            } else {
                                Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });


        }







    }

}