package com.example.kaushalyaadvani.birthdayreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kaushalyaadvani on 13/07/16.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent("com.cdac.birthday");

        PendingIntent pi =  PendingIntent.getBroadcast(context,0,i, 0);


        alm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000 , pi);




    }

}
