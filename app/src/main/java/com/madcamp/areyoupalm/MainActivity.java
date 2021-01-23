package com.madcamp.areyoupalm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Button testbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


        ImageButton bt_addAlarm = (ImageButton)findViewById(R.id.bt_addAlarm);
        bt_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAlarm = new Intent(getApplicationContext(),SetAlarm.class);
                startActivity(addAlarm);
            }
        });





       testbutton = findViewById(R.id.testbuttoon);
       testbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(),SMSActivity.class);
               startActivity(i);
           }
       });
    }
}