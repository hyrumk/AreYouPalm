package com.madcamp.areyoupalm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.madcamp.areyoupalm.alarm.AlarmHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SetAlarm extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Calendar cal = Calendar.getInstance();
    int alarm_year = cal.get(Calendar.YEAR);
    int alarm_month = cal.get(Calendar.MONTH) + 1;
    int alarm_date = cal.get(Calendar.DATE);
    int alarm_hour = 6;
    int alarm_minute = 0;
    boolean isDayChecked = false;
    boolean isDateSet =false;
    boolean isModifying;
    AppCompatSeekBar seekBar;
    int volume;
    Switch vibration_switch;
    ArrayList<String> repeatDays = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String AlarmList_key = "AlarmList";


        seekBar = findViewById(R.id.volumebar);
        seekBar.setProgress(5);
        seekBar.incrementProgressBy(1);
        seekBar.setMax(5);
        volume = seekBar.getProgress();

        vibration_switch = findViewById(R.id.sw_vibration);

        Intent intent = getIntent();
        isModifying= intent.getBooleanExtra("ismodifying",false);
        if(isModifying){
            System.out.println("이미 저장된 알람을 수정하는 섹션에 진입");
        }
        else{
            if(isPastTime())
                alarm_date += 1;
        }

        setDateText();

        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_time);
        timePicker.setCurrentHour(alarm_hour);
        timePicker.setCurrentMinute(alarm_minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                setHour(hourOfDay, minute);
                setDateText();
            }
        });

        AppCompatImageButton bt_calendar = (AppCompatImageButton) findViewById(R.id.bt_alarm_calender);

        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SetAlarm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        setDate(year,month,date);
                        setDateText();
                    }
                }, alarm_year, alarm_month - 1, alarm_date);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                Calendar tempCal = Calendar.getInstance();
                tempCal.set(2041,11,31);
                dialog.getDatePicker().setMaxDate(tempCal.getTimeInMillis());
                dialog.show();
            }
        });

        setCheckBox();

        Button bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                int id = getId();
                Calendar calendarToAlarm = Calendar.getInstance();
                calendarToAlarm.set(alarm_year, alarm_month - 1, alarm_date, alarm_hour, alarm_minute);

                Boolean[] repeatdays = new Boolean[7];
                Arrays.fill(repeatdays,false);
                if (repeatDays.contains("일")) {
                    repeatdays[0] = true;
                }
                if (repeatDays.contains("월")) {
                    repeatdays[1] = true;
                }
                if (repeatDays.contains("화")) {
                    repeatdays[2] = true;
                }
                if (repeatDays.contains("수")) {
                    repeatdays[3] = true;
                }
                if (repeatDays.contains("목")) {
                    repeatdays[4] = true;
                }
                if (repeatDays.contains("금")) {
                    repeatdays[5] = true;
                }
                if (repeatDays.contains("토")) {
                    repeatdays[6] = true;
                }

                EditText name = (EditText) findViewById(R.id.et_alarm_name);
                EditText number = (EditText) findViewById(R.id.et_tag);
                EditText message = (EditText) findViewById(R.id.sms_message);

                Alarm alarm = new Alarm(id, alarm_year, alarm_month, alarm_date, alarm_hour, alarm_minute, name.getText().toString(), number.getText().toString(), Arrays.asList(repeatdays).contains(true), repeatDays, message.getText().toString(), volume, vibration_switch.isChecked());



                AlarmListApp alarmListApp = (AlarmListApp) getApplication();
                // get Stored AlarmList
                String AlarmListPreferences = sharedPreferences.getString(AlarmList_key,"");
                Gson gson = new Gson();
                ArrayList<Alarm> storedAlarmList = gson.fromJson(AlarmListPreferences, new TypeToken<List<Alarm>>(){}.getType());
                if(!AlarmListPreferences.equals("")){
                    alarmListApp.updateAlarmList(storedAlarmList);
                }
                if(alarmListApp.contains(alarm))
                    alarmListApp.remove(alarm);
                alarmListApp.add(alarm);
                alarmListApp.sort();

                // Update sharedPreferences for alarmList
                storedAlarmList = alarmListApp.getAlarmList();
                gson = new Gson();
                String json = gson.toJson(storedAlarmList);
                sharedPreferences.edit().putString(AlarmList_key, json).apply();


                AlarmHandler.setAlarm(getApplicationContext(), id, calendarToAlarm, repeatdays,
                        name.getText().toString(), number.getText().toString(), "알람 종료",
                        "music", volume, vibration_switch.isChecked());

                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setHour(int hour, int minute){
        alarm_hour = hour;
        alarm_minute = minute;
        if(isToday()) {
            if (isPastTime())
                alarm_date += 1;
        }
        else if(isTomorrow() && !isDateSet && !isPastTime()) {
            alarm_date -= 1;
        }
    }

    public void setDate(int year,int month, int date){
        alarm_year = year;
        alarm_month = month + 1;
        alarm_date = date;
        if(isToday()) {
            if (isPastTime()) {
                Toast.makeText(this.getApplicationContext(),"이미 지난 시간은 선택할 수 없어요. 알람이 내일 울리도록 설정했어요.",Toast.LENGTH_LONG).show();
                System.out.println("지난 시간은 어림도 없지롱");
                alarm_date += 1;
            }
            isDateSet = false;
        } else {
            isDateSet = true;
        }
    }

    public void setDateText(){
        String date_text="";
        TextView tx_date = (TextView) findViewById(R.id.tx_alarm_day);
        if(isDayChecked) {
            if(repeatDays.size()==7)
                date_text += "매일";
            else {
                date_text += "매주 ";
                boolean isFirst = true;
                if (repeatDays.contains("일")) {
                    date_text += "일";
                    isFirst = false;
                }
                if (repeatDays.contains("월")) {
                    if (isFirst) {
                        date_text += "월";
                        isFirst = false;
                    } else
                        date_text += ", 월";
                }
                if (repeatDays.contains("화")) {
                    if(isFirst) {
                        date_text += "화";
                        isFirst = false;
                    } else
                        date_text += ", 화";

                }
                if (repeatDays.contains("수")) {
                    if (isFirst) {
                        date_text += "수";
                        isFirst = false;
                    } else
                        date_text += ", 수";
                }
                if (repeatDays.contains("목")) {
                    if (isFirst) {
                        date_text += "목";
                        isFirst = false;
                    } else
                        date_text += ", 목";
                }
                if (repeatDays.contains("금")) {
                    if (isFirst) {
                        date_text += "금";
                        isFirst = false;
                    } else
                        date_text += ", 금";
                }
                if (repeatDays.contains("토")) {
                    if (isFirst){
                        date_text += "토";
                        isFirst = false;
                    } else
                        date_text += ", 토";
                }
            }

        } else {
            if (alarm_year > cal.get(Calendar.YEAR)) {
                date_text += Integer.toString(alarm_year) + "년 ";
            }
            if (isToday())
                date_text += "오늘 ";
            if (isTomorrow())
                date_text += "내일 ";
            date_text += Integer.toString(alarm_month);
            date_text += "월 ";
            date_text += Integer.toString(alarm_date);
            date_text += "일 ";
            switch (getDayOfWeek()){
                case 1:
                    date_text += "(일)";
                    break;
                case 2:
                    date_text += "(월)";
                    break;
                case 3:
                    date_text += "(화)";
                    break;
                case 4:
                    date_text += "(수)";
                    break;
                case 5:
                    date_text += "(목)";
                    break;
                case 6:
                    date_text += "(금)";
                    break;
                case 7:
                    date_text += "(토)";
                    break;
            }
        }
        tx_date.setText(date_text);
    }

    public boolean isPastTime(){
        return alarm_hour < cal.get(Calendar.HOUR_OF_DAY) || (alarm_hour == cal.get(Calendar.HOUR_OF_DAY) && alarm_minute <=cal.get(Calendar.MINUTE));
    }

    public boolean isToday(){
        return alarm_year == cal.get(Calendar.YEAR) && alarm_month == cal.get(Calendar.MONTH) + 1 && alarm_date == cal.get(Calendar.DATE);
    }

    public boolean isTomorrow(){
        return alarm_year == cal.get(Calendar.YEAR) && alarm_month == cal.get(Calendar.MONTH) + 1 && alarm_date == cal.get(Calendar.DATE) + 1;
    }

    public int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(alarm_year, alarm_month - 1, alarm_date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void setCheckBox(){
        CheckBox cb_sunday = (CheckBox) findViewById(R.id.cb_sun);
        CheckBox cb_monday = (CheckBox) findViewById(R.id.cb_mon);
        CheckBox cb_tuesday = (CheckBox) findViewById(R.id.cb_tue);
        CheckBox cb_wednesday = (CheckBox) findViewById(R.id.cb_wed);
        CheckBox cb_thursday = (CheckBox) findViewById(R.id.cb_thu);
        CheckBox cb_friday = (CheckBox) findViewById(R.id.cb_fri);
        CheckBox cb_saturday = (CheckBox) findViewById(R.id.cb_sat);
        cb_sunday.setOnCheckedChangeListener(this);
        cb_monday.setOnCheckedChangeListener(this);
        cb_tuesday.setOnCheckedChangeListener(this);
        cb_wednesday.setOnCheckedChangeListener(this);
        cb_thursday.setOnCheckedChangeListener(this);
        cb_friday.setOnCheckedChangeListener(this);
        cb_saturday.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(isChecked)
        {
            repeatDays.add(buttonView.getText().toString());
            if(repeatDays.size()==1)
            {
                isDayChecked = true;
            }
        }
        else{
            repeatDays.remove(buttonView.getText().toString());
            if(repeatDays.size()==0)
            {
                isDayChecked = false;
            }
        }
        setDateText();
    }

    public int getId(){
        String id = "";
        boolean isRepeat = false;
        if (repeatDays.size()==7) {
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("일")) {
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("월")) {
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("화")) {
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("수")){
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("목")){
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("금")) {
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (repeatDays.contains("토")){
            id += "1";
            isRepeat = true;
        } else
            id += "0";
        if (isRepeat) {
            id = Integer.toString(Integer.parseInt(id,2));
        } else {
            id = Integer.toString(alarm_year - 2020);
            if(alarm_month<10)
                id += "0"+ Integer.toString(alarm_month);
            else
                id += Integer.toString(alarm_month);
            if(alarm_date<10)
                id += "0"+ Integer.toString(alarm_date);
            else
                id += Integer.toString(alarm_date);
        }
        if(alarm_hour<10)
            id += "0"+ Integer.toString(alarm_hour);
        else
            id += Integer.toString(alarm_hour);
        if(alarm_minute<10)
            id += "0"+ Integer.toString(alarm_minute);
        else
            id += Integer.toString(alarm_minute);
        return Integer.parseInt(id);
    }
}