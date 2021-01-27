package com.madcamp.areyoupalm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class AlarmListApp extends Application {

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

    public void add(Alarm alarm){
        alarmList.add(alarm);
    }

    public void remove(Alarm alarm){
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

    public void updateAlarmList(ArrayList<Alarm> alarmlist){
        alarmList = alarmlist;
    }

    public boolean contains(Alarm alarm){
        for (int i=0;i<alarmList.size();++i) {
            if(alarmList.get(i).id==alarm.id)
                return true;
        }
        return false;
    }

    public void sort(){
        AlarmComparator comp = new AlarmComparator();
        Collections.sort(alarmList, comp);
    }

}