package deva.todo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "mainActivityDebug";

    //Permission code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;

    private int []row = new int[8];
    private TextView t1,t2,t3,t4,t5,t6,t7,t8;
    private EditText e1;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        myPermission();

        t1 =(TextView) findViewById(R.id.textView);
        t2 =(TextView) findViewById(R.id.textView2);
        t3 =(TextView) findViewById(R.id.textView3);
        t4 =(TextView) findViewById(R.id.textView4);
        t5 =(TextView) findViewById(R.id.textView5);
        t6 =(TextView) findViewById(R.id.textView6);
        t7 =(TextView) findViewById(R.id.textView7);
        t8 =(TextView) findViewById(R.id.textView8);
        assert t1 != null;
        assert t2 != null;
        assert t3 != null;
        assert t4 != null;
        assert t5 != null;
        assert t6 != null;
        assert t7 != null;
        assert t8 != null;
        e1 = (EditText) findViewById(R.id.editText);
        assert e1 != null;

        Button bAdd = (Button) findViewById(R.id.button);
        Button bExit = (Button) findViewById(R.id.button2);
        Button bAbout = (Button) findViewById(R.id.button3);

        typeFace();
        read();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                flash();
            }
        }, 1000);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        bAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about();
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one();
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two();
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three();
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                four();
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                five();
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                six();
            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seven();
            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eight();
            }
        });
    }

    private void myPermission()
    {
        if(isWriteStorageAllowed()){
            //If permission is already having then showing the toast
            //Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            return;
        }

        //If the app has not the permission then asking for the permission
        Log.d(LOG_TAG,"Requesting permission.");
        requestStoragePermission();
    }

    //We are calling this method to check the permission status
    private boolean isWriteStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //Explain here why you need this permission
            Toast.makeText(MainActivity.this,"We need Storage Permission for Exporting Files",Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode >= STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Thank you for granting the Permission",Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG,"Permission granted.");
            }else
            {
                Toast.makeText(this,"Sorry, but we need Storage Permission for Exporting Files",Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG,"Permission NOT granted, exiting app.");
                finish();
            }
        }
    }

    private void typeFace()
    {
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/comic.ttf");
        t1.setTypeface(face);
        t2.setTypeface(face);
        t3.setTypeface(face);
        t4.setTypeface(face);
        t5.setTypeface(face);
        t6.setTypeface(face);
        t7.setTypeface(face);
        t8.setTypeface(face);
        Log.d(LOG_TAG,"typeFace() completed.");
    }
    private void row_status()
    {
      try {
            FileOutputStream f7 = openFileOutput("row1.txt",MODE_PRIVATE);
            if(row[0]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[1]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[2]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[3]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[4]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[5]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[6]==1)
            {
                f7.write(("1").getBytes());
            }
            else if (row[7]==1)
            {
                f7.write(("1").getBytes());
            }
            else
            {
                f7.write(("0").getBytes());
            }
            f7.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG,"row_status() completed.");
    }

    private void flash() {
        if(t1.getText().equals("")) {
            row[0]=0;
        } else {
            row[0]=1;
        }
        if(t2.getText().equals("")) {
            row[1]=0;
        } else {
            row[1]=1;
        }
        if(t3.getText().equals("")) {
            row[2]=0;
        } else {
            row[2]=1;
        }
        if(t4.getText().equals("")) {
            row[3]=0;
        } else {
            row[3]=1;
        }
        if(t5.getText().equals("")) {
            row[4]=0;
        } else {
            row[4]=1;
        }
        if(t6.getText().equals("")) {
            row[5]=0;
        } else {
            row[5]=1;
        }
        if(t7.getText().equals("")) {
            row[6]=0;
        } else {
            row[6]=1;
        }
        if(t8.getText().equals(""))
        {
            row[7]=0;
        }
        else
        {
            row[7]=1;
        }
        Log.d(LOG_TAG,"flash() completed");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        write_function();
        row_status();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                service_caller();
            }
        }, 1000);

        Log.d(LOG_TAG,"Activity paused.");
    }
    private void write_function()
    {
        try {
            FileOutputStream f1 = openFileOutput("data1.txt", MODE_PRIVATE);
            f1.write((t1.getText().toString()+"\n").getBytes());
            f1.write((t2.getText().toString()+"\n").getBytes());
            f1.write((t3.getText().toString()+"\n").getBytes());
            f1.write((t4.getText().toString()+"\n").getBytes());
            f1.write((t5.getText().toString()+"\n").getBytes());
            f1.write((t6.getText().toString()+"\n").getBytes());
            f1.write((t7.getText().toString()+"\n").getBytes());
            f1.write((t8.getText().toString()+"\n").getBytes());
            f1.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG,"write_function() completed.");
    }
    private void read()
    {
        try {
            FileInputStream f1 = openFileInput("data1.txt");
            InputStreamReader ias = new InputStreamReader(f1);
            BufferedReader br = new BufferedReader(ias);
            String lol;
            if((lol=br.readLine())!=null)
            {
                t1.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t2.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t3.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t4.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t5.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t6.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t7.setText(lol);
            }
            if((lol=br.readLine())!=null)
            {
                t8.setText(lol);
            }
            f1.close();
            Log.d(LOG_TAG,"read() completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void one()
    {

        if(!t1.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t1.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t2.getText().toString();
            t1.setText(temp);
            temp=t3.getText().toString();
            t2.setText(temp);
            temp=t4.getText().toString();
            t3.setText(temp);
            temp=t5.getText().toString();
            t4.setText(temp);
            temp=t6.getText().toString();
            t5.setText(temp);
            temp=t7.getText().toString();
            t6.setText(temp);
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void two()
    {
        if(!t2.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t2.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t3.getText().toString();
            t2.setText(temp);
            temp=t4.getText().toString();
            t3.setText(temp);
            temp=t5.getText().toString();
            t4.setText(temp);
            temp=t6.getText().toString();
            t5.setText(temp);
            temp=t7.getText().toString();
            t6.setText(temp);
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void three()
    {
        if(!t3.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t3.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t4.getText().toString();
            t3.setText(temp);
            temp=t5.getText().toString();
            t4.setText(temp);
            temp=t6.getText().toString();
            t5.setText(temp);
            temp=t7.getText().toString();
            t6.setText(temp);
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void four()
    {
        if(!t4.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t4.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t5.getText().toString();
            t4.setText(temp);
            temp=t6.getText().toString();
            t5.setText(temp);
            temp=t7.getText().toString();
            t6.setText(temp);
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void five()
    {
        if(!t5.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t5.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t6.getText().toString();
            t5.setText(temp);
            temp=t7.getText().toString();
            t6.setText(temp);
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void six()
    {
        if(!t6.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t6.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t7.getText().toString();
            t6.setText(temp);
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void seven()
    {
        if(!t7.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t7.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            temp=t8.getText().toString();
            t7.setText(temp);
            t8.setText("");
            flash();
            write_function();
        }
    }
    private void eight()
    {
        if(!t8.getText().equals(""))
        {
            Toast.makeText(MainActivity.this, (t8.getText().toString() + " Completed!"), Toast.LENGTH_SHORT).show();
            t8.setText("");
            row[7]=0;
            write_function();
        }
    }

    private void add()
    {

        String my_text = e1.getText().toString();
        if(!my_text.equals("")) {
            try {
                if(row[0]==0)
                {
                    t1.setText(my_text);
                    row[0]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[1]==0)
                {
                    t2.setText(my_text);
                    row[1]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[2]==0)
                {
                    t3.setText(my_text);
                    row[2]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[3]==0)
                {
                    t4.setText(my_text);
                    row[3]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[4]==0)
                {
                    t5.setText(my_text);
                    row[4]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[5]==0)
                {
                    t6.setText(my_text);
                    row[5]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[6]==0)
                {
                    t7.setText(my_text);
                    row[6]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else if(row[7]==0)
                {
                    t8.setText(my_text);
                    row[7]=1;
                    e1.setText("");
                    Toast.makeText(MainActivity.this, "Task successfully added.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Overflow Error!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void about()
    {
        Intent intent = new Intent("deva.todo.about");
        startActivity(intent);
    }
    private void exit()
    {

        AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
        a.setMessage("Are you sure you want to close this app ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        write_function();
                        row_status();
                        Toast.makeText(getApplicationContext(),"Thank you for using this application!",Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG,"Activity finished using exit()");
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(LOG_TAG,"At exit(), dialog cancelled.");
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a.create();
        alert.setTitle("Alert!");
        alert.show();
    }
    private void service_caller()
    {
        int i;
        for(i=0;i<8;i++)
        {
            if(row[i]==1)
            {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setAction("myBroadcast");
                sendBroadcast(intent);
                Log.d(LOG_TAG,"Broadcast sent using service_caller()");
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"Activity Destroyed!");
    }
    /*
    public void ClearAll()
    {
        eight();
        seven();
        six();
        five();
        four();
        three();
        two();
        one();
    }

    public void ClearOne()
    {
        one();
    }
    */
}
