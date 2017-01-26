package deva.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("myBroadcast")||action.equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
            Log.d("debug", "Broadcast Received in myReceiver, NotificationService is started.");
        }
    }
}
