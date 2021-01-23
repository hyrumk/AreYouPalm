package com.madcamp.areyoupalm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {

    Calendar cal = Calendar.getInstance();
    int alarm_year = cal.get(Calendar.YEAR);
    int alarm_month = cal.get(Calendar.MONTH) + 1;
    int alarm_date = cal.get(Calendar.DATE) + 1;
    int alarm_hour = 6;
    int alarm_minute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        Intent intent  = getIntent();
        boolean isModifying= intent.getBooleanExtra("ismodifying",false);
        if(isModifying){
            System.out.println("ㅇㅇㅇㅇ");
        }

        setDateText();

        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_time);
        timePicker.setCurrentHour(alarm_hour);
        timePicker.setCurrentMinute(alarm_minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                alarm_hour = hourOfDay;
                alarm_minute = minute;
                setDateText();
            }
        });

        ImageButton bt_calendar = (ImageButton) findViewById(R.id.bt_alarm_calender);
        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SetAlarm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        setDate(year,month,date);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
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
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setDate(int year,int month, int date){
        alarm_year = year;
        alarm_month = month;
        alarm_date = date;
    }

    public void setDateText(){
        String date_text="";
        TextView tx_date = (TextView) findViewById(R.id.tx_alarm_day);
        if (alarm_year<cal.get(Calendar.YEAR)) {
            date_text += Integer.toString(cal.get(Calendar.YEAR));
        }
        if (alarm_hour<cal.get(Calendar.HOUR_OF_DAY)) {
            date_text += "내일 ";
        } else if (alarm_hour>cal.get(Calendar.HOUR_OF_DAY)) {
            date_text += "오늘 ";
        } else {
            if (alarm_minute<cal.get(Calendar.MINUTE)) {
                date_text += "내일 ";
            } else {
                date_text += "오늘 ";
            }
        }
        date_text += Integer.toString(cal.get(Calendar.MONTH)+1);
        date_text += "월 ";
        date_text += Integer.toString(cal.get(Calendar.DATE));
        date_text += "일 ";
        switch (cal.get(Calendar.DAY_OF_WEEK)){
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
        tx_date.setText(date_text);
    }
}