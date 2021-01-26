package com.madcamp.areyoupalm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
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
        if(curAlarm.isActive)
            holder.activate.setChecked(true);
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
        }
    }
}
