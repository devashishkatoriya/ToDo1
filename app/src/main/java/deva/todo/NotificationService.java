package deva.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;

public class NotificationService extends Service  {

    private final static String LOG_TAG = "ServiceDebug";
    private long[] vib = {500, 1000};                                                    //Vibrate Time (in ms)

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        super.onStartCommand(intent,flags,startId);
        Log.d(LOG_TAG,"At onStartCommand()");

        try
        {
            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/logs/");
            if (!directory.exists()) {
                directory.mkdir();                                                      //Directory creation
            }
            File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/logs/" + "todo_log.txt");
            myFile.createNewFile();                                                     //File creation
            FileOutputStream fos = new FileOutputStream(myFile, true);

            DateFormat df = DateFormat.getDateTimeInstance();
            String date = df.format(Calendar.getInstance().getTime());
            fos.write(("\n\n" + date).getBytes());

            FileInputStream fileInputStream = openFileInput("spinner.txt");             //To get Reminder Value
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            int spinner_number = Integer.parseInt(bufferedReader.readLine());
            fileInputStream.close();
            fos.write(("\nReminder time : " + spinner_number).getBytes());

            fileInputStream = openFileInput("data1.txt");                               //To get Reminder Tasks
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuffer = new StringBuilder();
            int i = 1;
            while (i <= 8) {
                stringBuffer.append(bufferedReader.readLine()).append(" ");
                i++;
            }
            String remind = stringBuffer.toString();
            fileInputStream.close();

            Intent intent1 = new Intent(NotificationService.this, MainActivity.class);   //Creating Notification
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intent1);
            PendingIntent pIn = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(NotificationService.this)
                    .setTicker("Hey there!")
                    .setContentTitle("Reminder!")
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentText(remind)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIn)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(vib)
                    .build();
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if(!remind.equals("        "))
            {
                nm.notify(0, notification);
                fos.write("\nNotification Sent!".getBytes());
            }
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG_TAG,"uh oh! Got IOException :- "+e);
        }
        Log.d(LOG_TAG,"Notification Sent!");
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"At onDestroy()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}