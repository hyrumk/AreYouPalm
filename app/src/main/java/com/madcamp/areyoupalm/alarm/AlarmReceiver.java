package com.madcamp.areyoupalm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.madcamp.areyoupalm.AlarmActivity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        boolean[] day_array = intent.getBooleanArrayExtra("day_array");
        Log.d("ARRAY", String.valueOf(day_array));
        String number = intent.getStringExtra("number");
        String message = intent.getStringExtra("message");
        String music = intent.getStringExtra("music");
        int HOUR = intent.getIntExtra("HOUR_OF_DAY",0);
        int MINUTE = intent.getIntExtra("MINUTE", 0);
        System.out.println("따르르르르릉");
        try {
            Intent activity = new Intent(context, AlarmActivity.class);
            activity.putExtra("id", requestCode);
            activity.putExtra("name", name);
            activity.putExtra("day_array", day_array);
            activity.putExtra("number", number);
            activity.putExtra("message", message);
            activity.putExtra("music", music);
            activity.putExtra("HOUR_OF_DAY", HOUR);
            activity.putExtra("MINUTE", MINUTE);
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activity);
        } catch (Exception e){
            Log.d("CATCH_SITUATION",e.getMessage()+"");
        }
    }
}
