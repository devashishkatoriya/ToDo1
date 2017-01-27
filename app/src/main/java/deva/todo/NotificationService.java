package deva.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Environment;
import android.os.Handler;
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

    private String lol,remind,date;
    private final String LOG_TAG = "serviceDebug";
    private int i,spinner_number;
    private File directory,myFile;
    private FileOutputStream fos;
    private FileInputStream fileInputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private DateFormat df;

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        super.onStartCommand(intent,flags,startId);
        Log.d(LOG_TAG,"Inside onStartCommand()");
        lol = "1";
        try {
            fileInputStream = openFileInput("row1.txt");                                //For checking Status
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            lol = bufferedReader.readLine();
            fileInputStream.close();
            Log.d(LOG_TAG,"Got row1 as "+lol);

            fileInputStream = openFileInput("spinner.txt");                             //To get Reminder Value
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            spinner_number = Integer.parseInt(bufferedReader.readLine());
            fileInputStream.close();
            Log.d(LOG_TAG,"Got spinner as "+spinner_number);
        }catch (IOException e)
        {
            lol = "1";
            spinner_number = 25;
            Log.d(LOG_TAG,"uh oh! Got row1 IOException :- "+e);
        }
        /*
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (NotificationService.this) {
                    try {
                        Log.d(LOG_TAG,"Got location as "+Environment.getExternalStorageDirectory().getPath());
                        df = DateFormat.getDateTimeInstance();
                        //df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");                    //Wed, 4 Jul 2008 12:08:56 -0530
                        date = df.format(Calendar.getInstance().getTime());
                        directory = new File(Environment.getExternalStorageDirectory().getPath()+"/logs/");
                        if (!directory.exists()) {
                            directory.mkdir();
                        }
                        myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/logs/"+"todo_log.txt");                             //Log file creation
                        myFile.createNewFile();
                        fos = new FileOutputStream(myFile, true);
                        fos.write(("\n\n" + date).getBytes());
                        fos.write("\nProcess Started.".getBytes());

                        fileInputStream = openFileInput("spinner.txt");                             //To get Reminder Value
                        inputStreamReader = new InputStreamReader(fileInputStream);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        spinner_number = Integer.parseInt(bufferedReader.readLine());
                        fileInputStream.close();
                        fos.write(("\nReminder value : " + spinner_number).getBytes());

                        fos.write("\nWaiting...".getBytes());
                        TimeUnit.MINUTES.sleep(spinner_number);
                        date = df.format(Calendar.getInstance().getTime());
                        fos.write(("\n" + date).getBytes());
                        fos.write("\nWait completed.".getBytes());

                        fileInputStream = openFileInput("data1.txt");                               //To get Reminder Tasks
                        inputStreamReader = new InputStreamReader(fileInputStream);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuffer = new StringBuilder();
                        i = 1;
                        while (i <= 8) {
                            stringBuffer.append(bufferedReader.readLine()).append(" ");
                            i++;
                        }
                        remind = stringBuffer.toString();
                        fileInputStream.close();

                        long[] vib = {500,1000};                                                    //Vibrate Time (in ms)

                        Intent intent = new Intent(NotificationService.this, MainActivity.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
                        stackBuilder.addParentStack(MainActivity.class);
                        stackBuilder.addNextIntent(intent);
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

                        fileInputStream = openFileInput("row1.txt");
                        inputStreamReader = new InputStreamReader(fileInputStream);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        lol = bufferedReader.readLine();
                        fileInputStream.close();

                        if (lol.equals("1")) {
                            nm.notify(0, notification);
                            fos.write("\nNotified!".getBytes());
                        } else {
                            fos.write("\nNOT Notified.".getBytes());
                        }

                        fos.write("\nProcess completed.".getBytes());
                        fos.close();
                        stopSelf();

                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "At catch() of TheThread :- "+e);
                        try {
                            fos = openFileOutput("spinner.txt", MODE_PRIVATE);
                            fos.write(("25").getBytes());
                            fos.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            Log.d(LOG_TAG, "At 2nd catch() of TheThread :- "+e1);
                        }
                    }
                }
            }
        });
        myThread.start();
        */

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG,"Handler started.");
                try {
                    Log.d(LOG_TAG,"Got location as "+Environment.getExternalStorageDirectory().getPath());
                    df = DateFormat.getDateTimeInstance();
                    //df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");                    //Wed, 4 Jul 2008 12:08:56 -0530
                    date = df.format(Calendar.getInstance().getTime());
                    directory = new File(Environment.getExternalStorageDirectory().getPath()+"/logs/");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/logs/"+"todo_log.txt");                             //Log file creation
                    myFile.createNewFile();
                    fos = new FileOutputStream(myFile, true);
                    fos.write(("\n\n" + date).getBytes());
                    fos.write("\nProcess Started.".getBytes());

                    fos.write(("\nReminder time : " + spinner_number).getBytes());

                    fileInputStream = openFileInput("data1.txt");                               //To get Reminder Tasks
                    inputStreamReader = new InputStreamReader(fileInputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuffer = new StringBuilder();
                    i = 1;
                    while (i <= 8) {
                        stringBuffer.append(bufferedReader.readLine()).append(" ");
                        i++;
                    }
                    remind = stringBuffer.toString();
                    fileInputStream.close();

                    long[] vib = {500,1000};                                                    //Vibrate Time (in ms)

                    Intent intent = new Intent(NotificationService.this, MainActivity.class);   //Creating Notification
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(intent);
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

                    fileInputStream = openFileInput("row1.txt");                        //For rechecking current row1 status
                    inputStreamReader = new InputStreamReader(fileInputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    lol = bufferedReader.readLine();
                    fileInputStream.close();

                    if (lol.equals("1")) {                                              //Finally notifying user
                        nm.notify(0, notification);
                        fos.write("\nNotified!".getBytes());
                    } else {
                        fos.write("\nNOT Notified.".getBytes());
                    }

                    fos.write("\nProcess completed.".getBytes());
                    fos.close();
                    stopSelf();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "At catch() of Handler :- "+e);
                }
                Log.d(LOG_TAG, "Handler completed.");
            }
        }, 60000*spinner_number);
        Log.d(LOG_TAG,"onStartCommand() completed.");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"At onDestroy()");
        if(lol.equals("1"))
        {
            Intent ino = new Intent();
            ino.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            ino.setAction("myBroadcast");
            Log.d(LOG_TAG,"Broadcast sent.");
            sendBroadcast(ino);
        }
        else
        {
            Log.d(LOG_TAG,"Broadcast NOT sent.");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}