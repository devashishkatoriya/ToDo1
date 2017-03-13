package deva.todo;

/*
 * Created by Devashish Katoriya on 29-01-2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyAlarmManager alarm = new MyAlarmManager();
        alarm.SetAlarm(context,60);
    }
}
