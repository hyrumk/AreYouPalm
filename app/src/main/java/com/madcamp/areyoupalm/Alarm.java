package com.madcamp.areyoupalm;

import com.madcamp.areyoupalm.alarm.AlarmHandler;

import java.util.ArrayList;

public class Alarm {
    int id;
    int year;
    int month;
    int date;
    int hour;
    int minute;
    String name;
    String palmTag;
    boolean isActive;
    boolean isRepeat;
    ArrayList<String> repeatDays = new ArrayList<String>();
    String message;
    int volume;
    boolean isVibrate;

    Alarm(int id, int year, int month, int date, int hour, int minute, String name, String palmTag, boolean isRepeat, ArrayList<String> repeatDays, String message, int volume, boolean isVibrate){
        this.id = id;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        this.palmTag = palmTag;
        this.isActive = true;
        this.isRepeat = isRepeat;
        this.repeatDays = repeatDays;
        this.message = message;
        this.volume = volume;
        this.isVibrate = isVibrate;
    }

    public void deactivate(){
        this.isActive = false;
    }

    public void activate(){
        this.isActive = true;
    }
}
