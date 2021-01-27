package com.madcamp.areyoupalm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1001;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestPermission();
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String AlarmList_key = "AlarmList";

        recyclerView = (RecyclerView) findViewById(R.id.rcv_alarms);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AlarmListApp alarmListApp = (AlarmListApp) getApplication();

        String AlarmListPreferences = sharedPreferences.getString(AlarmList_key,"");
        Gson gson = new Gson();
        ArrayList<Alarm> storedAlarmList = gson.fromJson(AlarmListPreferences, new TypeToken<List<Alarm>>(){}.getType());
        if(!AlarmListPreferences.equals("")){
            alarmListApp.updateAlarmList(storedAlarmList);
        }
        adapter = new RecyclerViewAdapter(alarmListApp.getAlarmList());
        recyclerView.setAdapter(adapter);


        ImageButton bt_addAlarm = (ImageButton)findViewById(R.id.bt_addAlarm);
        bt_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAlarm = new Intent(getApplicationContext(),SetAlarm.class);
                addAlarm.putExtra("ismodifying",false);
                startActivity(addAlarm);
            }
        });
    }
    
    private void RequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                //Permission Granted-System will work
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String AlarmList_key = "AlarmList";
        
        AlarmListApp alarmListApp = (AlarmListApp) getApplication();
        String AlarmListPreferences = sharedPreferences.getString(AlarmList_key,"");
        Gson gson = new Gson();
        ArrayList<Alarm> storedAlarmList = gson.fromJson(AlarmListPreferences, new TypeToken<List<Alarm>>(){}.getType());
        if(!AlarmListPreferences.equals("")){
            alarmListApp.updateAlarmList(storedAlarmList);
        }
        adapter = new RecyclerViewAdapter(alarmListApp.getAlarmList());
        recyclerView.setAdapter(adapter);

    }

}


/*
<TODO> Features we could add
1. Give choices to users on which alarm to select (even from their own soundfiles)
2. Make repeat possible
3. Give a choice to give vibration when the alarm rings

 */