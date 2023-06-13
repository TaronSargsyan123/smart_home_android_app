package com.revive.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.revive.smarthome.firebase.LoginActivity;

public class EmailVerification extends AppCompatActivity {

    //logic for email verification
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        TextView textView = findViewById(R.id.email_verification_text_view);
        TextView login = findViewById(R.id.email_verification_login_button);
        Intent intent = getIntent();
        String email = intent.getStringExtra("CONFIRM");
        textView.setText("Email has been sent to " + email);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
            }
        });
    }
}