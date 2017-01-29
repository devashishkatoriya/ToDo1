package deva.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;


/*
 * Created by Devashish Katoriya on 29-01-2017.
 */

public class myAlarmManager extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    public final static String LOG_TAG = "myAlarmManagerDebug";


    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here update the widget/remote views.
        Bundle extras = intent.getExtras();

        if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
            //One time Alarm Code
            Log.d(LOG_TAG,"Got One-Time alarm");
        }
        //Write your code to be performed later

        Intent serviceIntent = new Intent(context, NotificationService.class);
        Log.d(LOG_TAG,"NotificationService started");
        context.startService(serviceIntent);

        //Release the lock
        wl.release();

    }
    public void SetAlarm(Context context,int m)
    {
        Log.d(LOG_TAG,"Got Repeating alarm");
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myAlarmManager.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        //After after 'm' min
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000 * m , pi);
    }

    public void CancelAlarm(Context context)
    {
        Log.d(LOG_TAG,"Alarm Cancelled!");
        Intent intent = new Intent(context, myAlarmManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        Log.d(LOG_TAG,"Got One-Time alarm");
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myAlarmManager.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}
