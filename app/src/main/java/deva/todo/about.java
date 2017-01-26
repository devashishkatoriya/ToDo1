package deva.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.d("AboutActivityDebug","At Activity_About onCreate()");

        Button bExport = (Button) findViewById(R.id.button4);
        bExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export_function();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AboutActivityDebug","Inside About_activity.onPause()");
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        assert spinner != null;
        String text = String.valueOf(spinner.getSelectedItem());
        try {
            FileOutputStream fileOutputStream = openFileOutput("spinner.txt",MODE_PRIVATE);
            fileOutputStream.write((text).getBytes());
            fileOutputStream.close();
            Log.d("AboutActivityDebug","spinner.txt created!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("AboutActivityDebug","About_activity completed.");
    }

    private void export_function()
    {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");        //Wed, 4 Jul 2008 12:08:56 -0530
        String date = df.format(Calendar.getInstance().getTime());
        String data;
        int i;
        try {

            File directory = new File("/sdcard/Documents");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File myFile = new File("/sdcard/Documents/my_todo_list.txt");             //Export file creation
            myFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(myFile, false);

            FileInputStream fileInputStream = openFileInput("data1.txt");                   //To get Reminder Tasks
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            fos.write(("\n\nDate : " + date + "\n").getBytes());
            fos.write(("\nTodo List :- \n").getBytes());
            i=1;
            while (i<=8) {
                data=bufferedReader.readLine();
                fos.write(("\n"+i+". "+data).getBytes());
                i++;
            }
            fileInputStream.close();
            fos.close();

            AlertDialog.Builder a = new AlertDialog.Builder(about.this);
            a.setMessage("Tasks exported successfully to /sdcard/Documents/")
                    .setCancelable(true)
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = a.create();
            alert.setTitle("Done!");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
