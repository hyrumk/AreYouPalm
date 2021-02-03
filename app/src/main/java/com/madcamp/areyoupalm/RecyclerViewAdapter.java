package com.madcamp.areyoupalm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.madcamp.areyoupalm.alarm.AlarmHandler;

import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
    Context mContext;

    public RecyclerViewAdapter(ArrayList<Alarm> alarmList){
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext  = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alarmlist_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm curAlarm = alarmList.get(position);
        if (curAlarm.hour < 12)
            holder.ampmView.setText("오전");
        else
            holder.ampmView.setText("오후");
        String time = "";
        if (curAlarm.hour > 12)
            time += Integer.toString(curAlarm.hour - 12);
        else
            time += Integer.toString(curAlarm.hour);
        time += ":";
        if(curAlarm.minute < 10)
            time += "0" + Integer.toString(curAlarm.minute);
        else
            time += Integer.toString(curAlarm.minute);
        holder.timeView.setText(time);
        holder.nameView.setText(curAlarm.name);
        holder.tagView.setText(curAlarm.palmTag);

        String date_text = "";
        if(curAlarm.isRepeat)
        {
            if (curAlarm.repeatDays.contains("일")) {
                date_text += "일 ";
            }
            if (curAlarm.repeatDays.contains("월")) {
                date_text += "월 ";
            }
            if (curAlarm.repeatDays.contains("화")) {
                date_text += "화 ";
            }
            if (curAlarm.repeatDays.contains("수")) {
                date_text += "수 ";
            }
            if (curAlarm.repeatDays.contains("목")) {
                date_text += "목 ";
            }
            if (curAlarm.repeatDays.contains("금")) {
                date_text += "금 ";
            }
            if (curAlarm.repeatDays.contains("토")) {
                date_text += "토 ";
            }
            if (date_text.equals("일 월 화 수 목 금 토 "))
                date_text = "매일";
        }
        else{
            Calendar cal = Calendar.getInstance();
            if(curAlarm.year != cal.get(Calendar.YEAR))
                date_text += Integer.toString(curAlarm.year) + "년 ";
            date_text += Integer.toString(curAlarm.month) + "월 " + Integer.toString(curAlarm.date) + "일 ";
            cal.set(curAlarm.year, curAlarm.month, curAlarm.date);
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
        }
        holder.repeatDateView.setText(date_text);
        holder.activate.setChecked(curAlarm.isActive);

        holder.activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    curAlarm.activate();
                    Calendar cal = Calendar.getInstance();
                    cal.set(curAlarm.year,curAlarm.month,curAlarm.date,curAlarm.hour,curAlarm.minute);
                    Boolean[] repeatdays = new Boolean[7];
                    Arrays.fill(repeatdays,false);
                    if(curAlarm.repeatDays.contains("일"))
                        repeatdays[0] = true;
                    if(curAlarm.repeatDays.contains("월"))
                        repeatdays[1] = true;
                    if(curAlarm.repeatDays.contains("화"))
                        repeatdays[2] = true;
                    if(curAlarm.repeatDays.contains("수"))
                        repeatdays[3] = true;
                    if(curAlarm.repeatDays.contains("목"))
                        repeatdays[4] = true;
                    if(curAlarm.repeatDays.contains("금"))
                        repeatdays[5] = true;
                    if(curAlarm.repeatDays.contains("토"))
                        repeatdays[6] = true;

                    AlarmHandler.setAlarm(mContext, curAlarm.id, cal, repeatdays, curAlarm.name, curAlarm.palmTag, curAlarm.message,"music", curAlarm.volume, curAlarm.isVibrate);
                    curAlarm.isActive = true;

                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    String AlarmList_key = "AlarmList";
                    Gson gson = new Gson();
                    String json = gson.toJson(alarmList);
                    sharedPreferences.edit().putString(AlarmList_key, json).apply();

                }
                else {
                    curAlarm.deactivate();
                    curAlarm.isActive = false;
                    AlarmHandler.cancelAlarm(mContext, curAlarm.id);

                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    String AlarmList_key = "AlarmList";
                    Gson gson = new Gson();
                    String json = gson.toJson(alarmList);
                    sharedPreferences.edit().putString(AlarmList_key, json).apply();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ampmView;
        TextView timeView;
        TextView nameView;
        TextView tagView;
        TextView repeatDateView;
        Switch activate;

        public ViewHolder(@NonNull View v) {
            super(v);
            ampmView = (TextView) v.findViewById(R.id.tx_am_pm);
            timeView = (TextView) v.findViewById(R.id.tx_alarm_time);
            nameView = (TextView) v.findViewById(R.id.tx_alarm_name);
            tagView = (TextView) v.findViewById(R.id.tx_alarm_tag);
            repeatDateView = (TextView) v.findViewById(R.id.tx_repeat_date);
            activate = (Switch) v.findViewById(R.id.sw_activate);
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent modifyAlarm = new Intent(mContext, SetAlarm.class);
                    modifyAlarm.putExtra("ismodifying",true);
                    int pos = getAdapterPosition();
                    modifyAlarm.putExtra("id",alarmList.get(pos).id);
                    modifyAlarm.putExtra("year", alarmList.get(pos).year);
                    modifyAlarm.putExtra("month", alarmList.get(pos).month);
                    modifyAlarm.putExtra("date", alarmList.get(pos).date);
                    modifyAlarm.putExtra("hour", alarmList.get(pos).hour);
                    modifyAlarm.putExtra("minute", alarmList.get(pos).minute);
                    modifyAlarm.putExtra("name", alarmList.get(pos).name);
                    modifyAlarm.putExtra("number", alarmList.get(pos).palmTag);
                    modifyAlarm.putExtra("message", alarmList.get(pos).message);
                    modifyAlarm.putExtra("repeatdays", alarmList.get(pos).repeatDays);
                    modifyAlarm.putExtra("volume", alarmList.get(pos).volume);
                    modifyAlarm.putExtra("isvibrate", alarmList.get(pos).isVibrate);
                    mContext.startActivity(modifyAlarm);
                }
            });
        }
    }
}
