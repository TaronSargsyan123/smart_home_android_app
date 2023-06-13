package com.revive.smarthome;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// about us activity logic
public class AboutUs extends AppCompatActivity {
    private TextView text;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        text = findViewById(R.id.about_text_view);
        back = findViewById(R.id.back_to_main_activity_about_us);
        text.setText("Welcome to Revive, the smart home app that makes your life easier and more convenient.\n" +
                "\n" +
                "At Revive, we believe that technology should make your life simpler, not more complicated. That's why we created a smart home app that is intuitive, easy to use, and customizable to fit your unique needs.\n" +
                "\n" +
                "Our team of experienced developers and designers are passionate about creating innovative solutions that improve your daily routine. We understand that your time is valuable, and that's why our app is designed to save you time and energy.\n" +
                "\n" +
                "With Revive, you can control all of your smart devices from one place, whether you're at home or on the go. Our app is compatible with a wide range of devices, including smart lights, thermostats, locks, cameras, and more.\n" +
                "\n" +
                "In addition to controlling your smart devices, Revive also provides insights into your energy usage and helps you save money on your utility bills. You can set schedules, timers, and rules to automate your devices and reduce your energy consumption.\n" +
                "\n" +
                "At Revive, we are committed to providing our users with the best possible experience. We are constantly improving and updating our app to meet the changing needs of our users.\n" +
                "\n" +
                "Thank you for choosing Revive as your smart home app. We look forward to helping you simplify your life and make your home smarter.");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}