package com.madcamp.areyoupalm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.madcamp.areyoupalm.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARMON", "ALARM IS ON");
        System.out.println("따르르르르릉");
        try {
            Intent activity = new Intent(context, AlarmActivity.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activity);
        } catch (Exception e){
            Log.d("CATCH_SITUATION",e.getMessage()+"");
        }
    }
}
