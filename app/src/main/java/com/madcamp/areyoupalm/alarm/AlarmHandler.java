package com.madcamp.areyoupalm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.Calendar;

/*
AlarmHandler class input description

calendar: calendar data. (date, hour, minute), second는 default로 0초
day_array: An array of booleans of size 7. [Sun, Mon, Tue, Wed, Thu, Fri, Sat]
            해당 index가 true면 그 날을 반복해 알람을 울려야하는 것으로 인지
name: alarm name
number: phone number to send to (receive "" if not set)
message: message to text to the given number (receive "" if not set, if phone number set but no text messages set, send a default message)
music (not implemented yet): info on music of choice
 */

public class AlarmHandler {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAlarm(Context context,
                         int requestCode, Calendar calendar,
                         boolean[] day_array, String name,
                         String number, String message, String music){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("day_array", day_array);
        myIntent.putExtra("number", number);
        myIntent.putExtra("message", message);
        myIntent.putExtra("music", music);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
        //<TODO> 특정 일자 대상인지 매주 반복인지 확인 후 set the alarm accordingly,
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editAlarm(Context context,
                          int requestCode, Calendar calendar,
                          boolean[] day_array, String name,
                          String number, String message, String music){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("day_array", day_array);
        myIntent.putExtra("number", number);
        myIntent.putExtra("message", message);
        myIntent.putExtra("music", music);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
        //<TODO> 특정 일자 대상인지 매주 반복인지 확인 후 set the alarm accordingly,
    }

    public void cancelAlarm(Context context, int requestCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, 0);

        alarmManager.cancel(pendingIntent);
    }


}
