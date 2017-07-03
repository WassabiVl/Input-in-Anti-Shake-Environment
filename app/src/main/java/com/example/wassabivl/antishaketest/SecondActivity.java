package com.example.wassabivl.antishaketest;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class SecondActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private int[] imageArray;
    private int x1=0,x2=120,y2=300;
    private long end = 0;
    float x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        //to modify the grid Layout programmatically
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        //to programatically change the image, create the array
        imageArray = new int[17];
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
        imageArray[16] = R.drawable.image16;
        //disable entery into edittext
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setKeyListener(null);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            x =(float) Math.pow(event.values[1], 2)*5;
            y = (float) Math.pow(event.values[0], 2)*5;

        }
        new Thread(new Runnable() {
            @Override
            public void run() { // Handles rendering the live sensor data
                for (int i = 0; i < 1; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
                            tableLayout.setX(x2+x);
                            tableLayout.setY(y2-y);
                        }
                    });
                }
            }
        }).start();
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
        if (x1==16){//once the last picture has reached, it will Show a notification to close
            Context context = getApplicationContext();
            CharSequence text = "This is the end of the Test!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            //shutdown system so no new entry is registered
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask ();
            }
            else{
                this.finishAffinity();
            }
        }
        x1++;
        TextView textView= (TextView) findViewById(R.id.editText);
        long start = System.currentTimeMillis();
        long change = start - end;
        end= start;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(imageArray[x1]);
        try { //to write to txt file
            File root = new File(Environment.getExternalStorageDirectory().toString());
            File saveFile = new File(root, "fullAS.txt");
            FileWriter writer = new FileWriter(saveFile,true);
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

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }
}
