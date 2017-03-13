package deva.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;

public class About extends AppCompatActivity {

    private final String LOG_TAG = "AboutActivityDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.d(LOG_TAG,"At onCreate()");

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
        Log.d(LOG_TAG,"At onPause()");
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        assert spinner != null;
        String text = String.valueOf(spinner.getSelectedItem());
        try {
            FileOutputStream fileOutputStream = openFileOutput("spinner.txt",MODE_PRIVATE);
            fileOutputStream.write((text).getBytes());
            fileOutputStream.close();
            Log.d(LOG_TAG,"spinner.txt created!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(About.this, "Reminder set for "+text+" min.", Toast.LENGTH_SHORT).show();
    }

    private void export_function()
    {
        DateFormat df = DateFormat.getDateTimeInstance();        //Wed, 4 Jul 2008 12:08:56 -0530
        String date = df.format(Calendar.getInstance().getTime());
        String data;
        int i;
        try {

            File directory = new File(Environment.getExternalStorageDirectory().getPath()+"/Documents/");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/Documents/"+"my_todo_list.txt");             //Export file creation
            myFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(myFile, false);

            FileInputStream fileInputStream = openFileInput("data1.txt");                   //To get Reminder Tasks
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            fos.write(("\n\nDate : " + date + "\n").getBytes());
            fos.write(("\nToDo List :- \n").getBytes());
            i=1;
            while (i<=8) {
                data=bufferedReader.readLine();
                fos.write(("\n"+i+". "+data).getBytes());
                i++;
            }
            fileInputStream.close();
            fos.close();

            AlertDialog.Builder a = new AlertDialog.Builder(About.this);
            a.setMessage("Tasks exported successfully to "+Environment.getExternalStorageDirectory().getPath()+"/Documents/")
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
            Log.d(LOG_TAG,"File successfully exported");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(About.this,"IO Error occurred!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"At onDestroy()");
    }
}
