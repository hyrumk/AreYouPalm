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
import android.widget.Toast;

import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {

    int alarm_year;
    int alarm_month;
    int alarm_date;
    int alarm_hour;
    int alarm_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        Intent intent  = getIntent();
        boolean isModifying= intent.getBooleanExtra("ismodifying",false);
        if(isModifying){
            System.out.println("ㅇㅇㅇㅇ");
        }

        ImageButton bt_calendar = (ImageButton) findViewById(R.id.bt_alarm_calender);
        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(SetAlarm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        setDate(year,month,date);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
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
}