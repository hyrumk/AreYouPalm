package com.madcamp.areyoupalm;

import android.app.Application;

import java.util.ArrayList;

public class AlarmListClass extends Application {

    private ArrayList<Alarm> alarmList;

    @Override
    public void onCreate() {
        //전역 변수 초기화
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
        alarmList.remove(alarm);
    }

    public ArrayList<Alarm> getAlarmList(){
        return alarmList;
    }

}