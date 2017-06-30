package com.example.wassabivl.antishaketest;

import android.Manifest;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SecondActivity extends AppCompatActivity implements SensorEventListener{
    public static final float Shake_Gravity_Threshold = 2.7f; //set a threshold limit not to activate the anitshake due to gravity
    SensorManager sensorManager;
    private int[] imageArray;
    int x=0;
    long end = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        //to modify the grid Layout programmatically
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setRowOrderPreserved(false);
        //to programatically change the image, create the array
        imageArray = new int[16];
        imageArray[0] = R.drawable.image0;
        imageArray[1] = R.drawable.image1;
        imageArray[2] = R.drawable.image2;
        imageArray[3] = R.drawable.image3;
        imageArray[4] = R.drawable.image4;
        imageArray[5] = R.drawable.image5;
        imageArray[6] = R.drawable.image6;
        imageArray[7] = R.drawable.image7;
        imageArray[8] = R.drawable.image8;
        imageArray[9] = R.drawable.image9;
        imageArray[10] = R.drawable.image10;
        imageArray[11] = R.drawable.image11;
        imageArray[12] = R.drawable.image12;
        imageArray[13] = R.drawable.image13;
        imageArray[14] = R.drawable.image14;
        imageArray[15] = R.drawable.image15;
        //disable entery into edittext
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setKeyListener(null);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float x = (int) Math.pow(event.values[0], 2)*10;
        final float y = (int) Math.pow(event.values[1], 2);
        float z = event.values[2];
        float gx = x / SensorManager.GRAVITY_EARTH;
        float gy = y / SensorManager.GRAVITY_EARTH;
        float gz = z / SensorManager.GRAVITY_EARTH;
        float gForce = (float) Math.sqrt(gx*gx+gy*gy+gz*gz);
        if (gForce>Shake_Gravity_Threshold) {
            new Thread(new Runnable() {
                @Override
                public void run() { // Handles rendering the live sensor data
                    for (int i = 0; i < 1; i++) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int l = (-Math.round(x));
                                int u = (92-Math.round(y));
                                int r = (Math.round(x));
                                int d = (92+Math.round(y));
                                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                // imageView.setPadding(l,u,r,d);
                                // marginParams.setMargins(l, u, r, d);
                                ViewGroup.MarginLayoutParams lFooter = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                                lFooter.bottomMargin = d;
                                lFooter.leftMargin = l;
                                lFooter.rightMargin = r;
                                lFooter.topMargin=u;
                                imageView.setLayoutParams(lFooter);
                            }
                        });
                        // sleep to slow down the add of entries
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            Log.e("graph1", e.toString());
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void button0(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("0");}
    public void button1(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("1");}
    public void button2(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("2");}
    public void button3(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("3");}
    public void button4(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("4");}
    public void button5(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("5");}
    public void button6(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("6");}
    public void button7(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("7");}
    public void button8(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("8");}
    public void button9(View v){TextView textView= (TextView) findViewById(R.id.editText);textView.append("9");}
    public void buttonE(View v)  {
        //need to call to change the picture after this button is pressed
        TextView textView= (TextView) findViewById(R.id.editText);
        long start = System.currentTimeMillis();
        long change = start - end;
        end= start;
        x++;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(imageArray[x]);
        try { //to write to txt file
            File root = new File(Environment.getExternalStorageDirectory().toString());
            File gpxfile = new File(root, "imageAS.txt");
            FileWriter writer = new FileWriter(gpxfile,true);
            BufferedWriter writer1 = new BufferedWriter(writer);
            String string1 = textView.getText().toString() + "  " + change + " ";
            writer1.append(string1);
            writer1.newLine();
            writer1.flush();
            writer1.close();
            textView.setText("");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (x==16){//once the last picture has reached, it will start the second activity
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

    }
    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }
}
