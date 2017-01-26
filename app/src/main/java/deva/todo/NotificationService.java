package deva.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service  {

    private String lol,remind,date;
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
        lol = "1";
        try {
            fileInputStream = openFileInput("row1.txt");                                            //For checking Status
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            lol = bufferedReader.readLine();
            fileInputStream.close();
        }catch (IOException e)
        {
            lol = "1";
        }
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (NotificationService.this) {
                    try {
                        df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");                    //Wed, 4 Jul 2008 12:08:56 -0530
                        date = df.format(Calendar.getInstance().getTime());
                        directory = new File("/sdcard/logs");
                        if (!directory.exists()) {
                            directory.mkdir();
                        }
                        myFile = new File("/sdcard/logs/todo_log.txt");                             //Log file creation
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
                        Log.d("ServiceDebug", "At catch() of TheThread.");
                        try {
                            fos = openFileOutput("spinner.txt", MODE_PRIVATE);
                            fos.write(("25").getBytes());
                            fos.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        myThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lol.equals("1"))
        {
            Intent ino = new Intent();
            ino.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            ino.setAction("myBroadcast");
            sendBroadcast(ino);
            Log.d("ServiceDebug","Broadcast sent using TheService.onDestroy()");
        }
        else
        {
            Log.d("ServiceDebug","Broadcast NOT sent using TheService.onDestroy()");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}