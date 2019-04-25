package com.example.gyrogpsexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {


    LocationManager locationManager;

    private SensorManager manager;
    private TextView mXAxisValueTextView; //X軸
    private TextView mYAxisValueTextView; //Y軸
    private TextView mZAxisValueTextView; //Z軸
    private TextView mLatitudeTextView;  //緯度
    private TextView mLongitudeTextView; //軽度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        mXAxisValueTextView = (TextView) findViewById(R.id.XAxisValueTextView);
        mYAxisValueTextView = (TextView) findViewById(R.id.YAxisValueTextView);
        mZAxisValueTextView = (TextView) findViewById(R.id.ZAxisValueTextView);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 50, (LocationListener) this);
        }
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

    private void locationStart() {
        Log.d("debug", "locationStart()");

        //LocationManager インスタンス作成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled");
        } else {
            // GPSを設定するように促す
            Intent settingsIntent =
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Log.d("debug", "checkSelfPermission false");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 50, (LocationListener) this);
    }

    //結果の受け取り

    /**
     * Android Quickstart:
     * https://developers.google.com/sheets/api/quickstart/android
     *
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *      requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permission
     *      which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionResult(
            int requestCode, @NonNull String[]permissions, @NonNull int[]grantResults) {
        if (requestCode == 1000) {
            //使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug", "checkSelfPermission true");

                locationStart();
            } else {
                //そ時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上ないもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onStatus(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //緯度の表示
        mLatitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        String latitude = "緯度" + location.getLatitude();
        mLatitudeTextView.setText(latitude);

        //軽度の表示
        mLongitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        String longtude = "軽度" + location.getLongitude();
        mLongitudeTextView.setText(longtude);

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
