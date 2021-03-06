package com.madcamp.areyoupalm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
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

//<TODO> Enable alarm music customization, alarm vibration customization
// <TODO> Text limit in message
// <TODO> Add text limit notification on textview (red text) when it reaches the max length.
// <TODO> Auto search phone number when setting one
@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SetAlarm extends AppCompatActivity{

    Calendar cal = Calendar.getInstance();
    int prev_id;
    int alarm_year = cal.get(Calendar.YEAR);
    int alarm_month = cal.get(Calendar.MONTH) + 1;
    int alarm_date = cal.get(Calendar.DATE);
    int alarm_hour = 6;
    int alarm_minute = 0;
    boolean isDayChecked = false;
    boolean isDateSet =false;
    boolean isModifying;
    ArrayList<String> repeatDays = new ArrayList<String>();
    EditText et_name;
    EditText et_number;
    EditText et_message;
    TextView name_warning; TextView number_warning;
    AppCompatSeekBar seekBar;
    Switch vibration_switch;
    AppCompatButton btn_sun;AppCompatButton btn_mon;AppCompatButton btn_tue;AppCompatButton btn_wed;AppCompatButton btn_thu;AppCompatButton btn_fri;AppCompatButton btn_sat;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String AlarmList_key = "AlarmList";
        et_name = (EditText) findViewById(R.id.et_alarm_name);
        et_number = (EditText) findViewById(R.id.et_tag);
        et_message = (EditText) findViewById(R.id.sms_message);
        seekBar = (AppCompatSeekBar) findViewById(R.id.volumebar);
        vibration_switch = (Switch) findViewById(R.id.sw_vibration);
        name_warning = findViewById(R.id.notice_name);
        number_warning = findViewById(R.id.notice_number);
        seekBar.incrementProgressBy(1);
        seekBar.setMax(5);

        Intent intent = getIntent();
        isModifying= intent.getBooleanExtra("ismodifying",false);
        if(isModifying){
            prev_id = intent.getIntExtra("id",0);
            alarm_year = intent.getIntExtra("year",0);
            alarm_month = intent.getIntExtra("month",0);
            alarm_date = intent.getIntExtra("date",0);
            alarm_hour = intent.getIntExtra("hour",0);
            alarm_minute = intent.getIntExtra("minute",0);
            et_name.setText(intent.getStringExtra("name"));
            et_number.setText(intent.getStringExtra("number"));
            et_message.setText(intent.getStringExtra("message"));
            repeatDays = intent.getStringArrayListExtra("repeatdays");
            seekBar.setProgress(intent.getIntExtra("volume",0));
            vibration_switch.setChecked(intent.getBooleanExtra("isvibrate", false));

            if(repeatDays.size() != 0){
                isDayChecked = true;
            }
            setDateText();
        }
        else{
            if(isPastTime())
                alarm_date += 1;
            seekBar.setProgress(5);
        }

        Boolean[] repeatdays = new Boolean[7];
        Arrays.fill(repeatdays,false);


        ///////////////// Days Button /////////////////

        btn_sun = findViewById(R.id.sun_btn);
        btn_mon = findViewById(R.id.mon_btn);
        btn_tue = findViewById(R.id.tue_btn);
        btn_wed = findViewById(R.id.wed_btn);
        btn_thu = findViewById(R.id.thu_btn);
        btn_fri = findViewById(R.id.fri_btn);
        btn_sat = findViewById(R.id.sat_btn);

        if (repeatDays.contains("일")) {
            repeatdays[0] = true;
            btn_sun.setTextColor(getResources().getColor(R.color.white));
            btn_sun.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
        }
        if (repeatDays.contains("월")) {
            repeatdays[1] = true;
            btn_mon.setTextColor(getResources().getColor(R.color.white));
            btn_mon.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
        }
        if (repeatDays.contains("화")) {
            repeatdays[2] = true;
            btn_tue.setTextColor(getResources().getColor(R.color.white));
            btn_tue.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
        }
        if (repeatDays.contains("수")) {
            repeatdays[3] = true;
            btn_wed.setTextColor(getResources().getColor(R.color.white));
            btn_wed.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
        }
        if (repeatDays.contains("목")) {
            repeatdays[4] = true;
            btn_thu.setTextColor(getResources().getColor(R.color.white));
            btn_thu.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
        }
        if (repeatDays.contains("금")) {
            repeatdays[5] = true;
            btn_fri.setTextColor(getResources().getColor(R.color.white));
            btn_fri.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
        }
        if (repeatDays.contains("토")) {
            repeatdays[6] = true;
            btn_sat.setTextColor(getResources().getColor(R.color.white));
            btn_sat.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
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
                AlarmListApp alarmListApp = (AlarmListApp) getApplication();
                // get Stored AlarmList
                String AlarmListPreferences = sharedPreferences.getString(AlarmList_key,"");
                Gson gson = new Gson();
                ArrayList<Alarm> storedAlarmList = gson.fromJson(AlarmListPreferences, new TypeToken<List<Alarm>>(){}.getType());
                if(!AlarmListPreferences.equals("")){
                    alarmListApp.updateAlarmList(storedAlarmList);
                }

                if(isModifying) {
                    AlarmHandler.cancelAlarm(getApplicationContext(), prev_id);
                    alarmListApp.removeById(prev_id);
                }

                int id = getId();
                Calendar calendarToAlarm = Calendar.getInstance();
                calendarToAlarm.set(alarm_year, alarm_month - 1, alarm_date, alarm_hour, alarm_minute);

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

                Alarm alarm = new Alarm(id, alarm_year, alarm_month, alarm_date, alarm_hour, alarm_minute,
                                et_name.getText().toString(), et_number.getText().toString(), Arrays.asList(repeatdays).contains(true),
                        repeatDays, et_message.getText().toString(), seekBar.getProgress(), vibration_switch.isChecked(), true);

                if(alarmListApp.contains(alarm))
                    alarmListApp.remove(alarm);
                alarmListApp.add(alarm);
                alarmListApp.sort();

                if(!et_number.getText().toString().matches("[0-9]+") && !et_number.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"유효하지 않은 번호로는 문자가 가지 않습니다.",Toast.LENGTH_SHORT).show();
                }

                // Update sharedPreferences for alarmList
                storedAlarmList = alarmListApp.getAlarmList();
                gson = new Gson();
                String json = gson.toJson(storedAlarmList);
                sharedPreferences.edit().putString(AlarmList_key, json).apply();

                AlarmHandler.setAlarm(getApplicationContext(), id, calendarToAlarm, repeatdays,
                        et_name.getText().toString(), et_number.getText().toString(), et_message.getText().toString(),
                        "music", seekBar.getProgress(), vibration_switch.isChecked());
                Toast.makeText(getApplicationContext(),"알람이 설정되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btn_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[0]){
                    btn_sun.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_sun.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[0] = false;
                    repeatDays.remove(btn_sun.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_sun.setTextColor(getResources().getColor(R.color.white));
                    btn_sun.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[0] = true;
                    repeatDays.add(btn_sun.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
            }
        });

        btn_mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[1]){
                    btn_mon.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_mon.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[1] = false;
                    repeatDays.remove(btn_mon.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_mon.setTextColor(getResources().getColor(R.color.white));
                    btn_mon.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[1] = true;
                    repeatDays.add(btn_mon.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
            }
        });

        btn_tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[2]){
                    btn_tue.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_tue.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[2] = false;
                    repeatDays.remove(btn_tue.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_tue.setTextColor(getResources().getColor(R.color.white));
                    btn_tue.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[2] = true;
                    repeatDays.add(btn_tue.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
            }
        });

        btn_wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[3]){
                    btn_wed.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_wed.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[3] = false;
                    repeatDays.remove(btn_wed.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_wed.setTextColor(getResources().getColor(R.color.white));
                    btn_wed.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[3] = true;
                    repeatDays.add(btn_wed.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
            }
        });

        btn_thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[4]){
                    btn_thu.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_thu.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[4] = false;
                    repeatDays.remove(btn_thu.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_thu.setTextColor(getResources().getColor(R.color.white));
                    btn_thu.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[4] = true;
                    repeatDays.add(btn_thu.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
            }
        });

        btn_fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[5]){
                    btn_fri.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_fri.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[5] = false;
                    repeatDays.remove(btn_fri.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_fri.setTextColor(getResources().getColor(R.color.white));
                    btn_fri.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[5] = true;
                    repeatDays.add(btn_fri.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
            }
        });

        btn_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatdays[6]){
                    btn_sat.setTextColor(getResources().getColor(R.color.grey_letter));
                    btn_sat.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                    repeatdays[6] = false;
                    repeatDays.remove(btn_sat.getText().toString());
                    if(repeatDays.size()==0)
                    {
                        isDayChecked = false;
                    }
                }
                else{
                    btn_sat.setTextColor(getResources().getColor(R.color.white));
                    btn_sat.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.redon));
                    repeatdays[6] = true;
                    repeatDays.add(btn_sat.getText().toString());
                    if(repeatDays.size()==1)
                    {
                        isDayChecked = true;
                    }
                }
                setDateText();
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