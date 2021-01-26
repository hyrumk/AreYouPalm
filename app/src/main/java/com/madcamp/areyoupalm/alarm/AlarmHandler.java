package com.madcamp.areyoupalm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;

/*
AlarmHandler class input description

calendar: calendar data. (date, hour, minute), second는 default로 0초
        매주 반복하는 알람인 경우: calendar는 오늘 날짜로 알람 설정된 시간
        매주 반복하지 않는 알람인 경우: 알람 설정된 날짜의 시간
day_array: An array of booleans of size 7. [Sun, Mon, Tue, Wed, Thu, Fri, Sat]
            해당 index가 true면 그 날을 반복해 알람을 울려야하는 것으로 인지
name: alarm name
number: phone number to send to (receive "" if not set)
message: message to text to the given number (receive "" if not set, if phone number set but no text messages set, send a default message)
music (not implemented yet): info on music of choice
 */

public class AlarmHandler {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setAlarm(Context context,
                         int requestCode, Calendar calendar,
                         boolean[] day_array, String name,
                         String number, String message, String music){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("id", requestCode);
        myIntent.putExtra("name", name);
        myIntent.putExtra("day_array", day_array);
        myIntent.putExtra("number", number);
        myIntent.putExtra("message", message);
        myIntent.putExtra("music", music);
        // Below two will be used in setting the next alarm in weekly alarms
        myIntent.putExtra("HOUR_OF_DAY", calendar.get(Calendar.HOUR_OF_DAY));
        myIntent.putExtra("MINUTE", calendar.get(Calendar.MINUTE));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Arrays.asList(day_array).contains(true)){// 매주 반복하는 알람
            Calendar today = Calendar.getInstance();
            int day; int add_day = -1; int iteration_check = -1;
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            if(calendar.before(today)){// 오늘 날짜에 대입한 알람 설정 일자가 현재 시각보다 이전인 경우
                day = today.get(Calendar.DAY_OF_WEEK);// 가장 가까운 미래에 설정할 알람의 일자를 내일부터 check
                if(day == 7){
                    day = 0;
                }
                add_day = 0;
            }
            else{
                day = today.get(Calendar.DAY_OF_WEEK) - 1;// 가장 가까운 미래에 설정할 알람의 일자를 오늘부터 check
                add_day = -1;
            }

            for(int i = day; i<7; i++){
                add_day++;
                if(day_array[i]){
                    iteration_check = i;
                    break;
                }
            }
            if (iteration_check == -1){// If next day wasn't in remainder of the week
                for(int i = 0; i<day; i++){
                    add_day++;
                    if(day_array[i]){
                        iteration_check = i;
                        break;
                    }
                }
            }
            today.add(Calendar.DATE, add_day);
            today.set(Calendar.HOUR_OF_DAY, hour);
            today.set(Calendar.MINUTE, minute);
            today.set(Calendar.SECOND, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, today.getTimeInMillis(), pendingIntent);
        }
        else{// 반복하지 않는 특정 날짜 알람
            Log.d("Tayg", String.valueOf(calendar.getTime()));
            calendar.set(Calendar.SECOND, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        //<TODO> 특정 일자 대상인지 매주 반복인지 확인 후 set the alarm accordingly, (된듯..?)
    }

    /*
    Function editAlarm may be replaceable by setAlarm.


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void editAlarm(Context context,
                          int requestCode, Calendar calendar,
                          boolean[] day_array, String name,
                          String number, String message, String music){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("id", requestCode);
        myIntent.putExtra("name", name);
        myIntent.putExtra("day_array", day_array);
        myIntent.putExtra("number", number);
        myIntent.putExtra("message", message);
        myIntent.putExtra("music", music);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
        //<TODO> 특정 일자 대상인지 매주 반복인지 확인 후 set the alarm accordingly,
    }
    */


    public static void cancelAlarm(Context context, int requestCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, myIntent, 0);

        alarmManager.cancel(pendingIntent);
    }


}
