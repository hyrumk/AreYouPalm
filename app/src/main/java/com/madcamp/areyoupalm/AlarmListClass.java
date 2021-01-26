package com.madcamp.areyoupalm;

import android.app.Application;

import java.util.ArrayList;

public class AlarmListClass extends Application {

    private ArrayList<Alarm> alarmList;

    @Override
    public void onCreate() {
        alarmList = new ArrayList<Alarm>();
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void addAlarm(Alarm alarm){
        alarmList.add(alarm);
    }

    public void removeAlarm(Alarm alarm){
        for (int i=0;i<alarmList.size();++i)
        {
            if (alarmList.get(i).id == alarm.id)
            {
                alarmList.remove(i);
                break;
            }
        }
    }

    public ArrayList<Alarm> getAlarmList(){
        return alarmList;
    }

}