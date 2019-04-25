package com.example.gyrogpsexample;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager manager;
    private TextView mXAxisValueTextView; //X軸
    private TextView mYAxisValueTextView; //Y軸
    private TextView mZAxisValueTextView; //Z軸


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        mXAxisValueTextView = (TextView) findViewById(R.id.XAxisValueTextView);
        mYAxisValueTextView = (TextView) findViewById(R.id.YAxisValueTextView);
        mZAxisValueTextView = (TextView) findViewById(R.id.ZAxisValueTextView);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Listenner登録
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_GYROSCOPE);

        if(sensors.size() > 0) {
            Sensor s =sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            String Xstr = "X軸：" + String.valueOf(event.values[0]);
            String Ystr = "Y軸：" + String.valueOf(event.values[1]);
            String Zstr = "Z軸：" + String.valueOf(event.values[2]);
            mXAxisValueTextView.setText(Xstr);
            mYAxisValueTextView.setText(Ystr);
            mZAxisValueTextView.setText(Zstr);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
