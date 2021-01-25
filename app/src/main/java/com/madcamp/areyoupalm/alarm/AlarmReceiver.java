package com.madcamp.areyoupalm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARMON", "ALARM IS ON");
        System.out.println("따르르르르릉");
    }
}
