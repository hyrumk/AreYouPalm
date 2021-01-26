package com.madcamp.areyoupalm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.madcamp.areyoupalm.alarm.AlarmHandler;

import java.util.Arrays;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    boolean user_press = false;
    Button End_Button;
    MediaPlayer mp;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if-else needed for pop-up screen on alarm.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        }
        else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        setContentView(R.layout.activity_alarm);

        End_Button = findViewById(R.id.end_button);

        if(mp == null){
            mp = MediaPlayer.create(this, R.raw.loud_alarm_clock);
            mp.setLooping(true);
            mp.start();
        }



        Intent intent = getIntent();

        int requestCode = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        boolean[] day_arrays = intent.getBooleanArrayExtra("day_array");
        String number = intent.getStringExtra("number");
        String message = intent.getStringExtra("message");
        String music = intent.getStringExtra("music");
        int HOUR = intent.getIntExtra("HOUR_OF_DAY",0);
        int MINUTE = intent.getIntExtra("MINUTE", 0);

        Boolean[] day_array = new Boolean[7];
        for(int i=0;i<7;i++){
            day_array[i] = day_arrays[i];
        }

        Calendar current = Calendar.getInstance();
        // Updates the alarm if it's a weekly alarm.
        if(Arrays.asList(day_array).contains(true)){// If weekly alarm
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, HOUR);
            calendar.set(Calendar.MINUTE, MINUTE);
            calendar.set(Calendar.SECOND, 0);
            AlarmHandler.setAlarm(getApplicationContext(), requestCode, calendar, day_array, name, number, message, music);
        }
        else{ // If non-weekly alarm
            //<TODO> 알람 삭제 or 끄기! (Also from recylcerview in MainActivity)

        }


        /*<TODO> 사용자가 알람 해제를 누르지 않으면 50초동안 알람 울린 후 문자 보내기.
        current.add(Calendar.SECOND,50);
        while(mp.isPlaying()){

        }
        // Make the alarm ring for 50 seconds unless the user turns it off manually
        if(mp.isPlaying() && Calendar.getInstance() == current){
            mp.release();
            finish();
        }
*/

        End_Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mp.release();
                finish();
                return false;
            }
        });


    }
}

//<TODO> not allow back click, finish activity on turn off button..?