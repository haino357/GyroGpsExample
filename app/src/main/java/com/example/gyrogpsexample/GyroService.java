package com.example.gyrogpsexample;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GyroService<manager> extends Service implements SensorEventListener {

    private final String TAG = "GyroService";

    private SensorManager manager;


    SensorEvent event = null;

    float XGyro = 0;
    float YGyro = 0;
    float ZGyro = 0;

    private RemoteCallbackList<IGyroCallback> mGyroCallbacks = new RemoteCallbackList<IGyroCallback>();


    /**
     * AIDLファイルで定義したインターフェースの中身をここに記載する
     */
    private final IGyroService.Stub mBinder = new IGyroService.Stub() {

        // コールバックを登録する
        @Override
        public void registerCallback(IGyroCallback callback) throws RemoteException {
            Log.d(TAG, "registerCallback()");
            mGyroCallbacks.register(callback);

        }

        //コールバックを取り除く
        @Override
        public void runregisterCallback(IGyroCallback callback) throws RemoteException {
            Log.d(TAG, "runregisterCallback()");
            mGyroCallbacks.unregister(callback);

        }
        @Override
        public float getXAxisGyroValue() throws RemoteException {
            // TODO Auto-generated method stub
            manager = (SensorManager) getSystemService(SENSOR_SERVICE);
            SensorEvent event = null;
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                XGyro = event.values[0];
            }
//            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                XGyro = event.values[0];
//                XGyro = (float) 1.2356;
//            }

            return XGyro;
        }

        @Override
        public float getYAxisGyroValue() throws RemoteException {
            manager = (SensorManager) getSystemService(SENSOR_SERVICE);

//            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                YGyro = event.values[1];
//            }

            return YGyro;
        }

        @Override
        public float getZAxisGyroValue() throws RemoteException {
            manager = (SensorManager) getSystemService(SENSOR_SERVICE);

            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                ZGyro = event.values[2];
            }

            return ZGyro;
        }
    };

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    /**
     * サービスがバインドされた時にコールバックされる
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "バインドされた");
        showToast("バインドされた");

        //Listenner登録
//        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_GYROSCOPE);
//
//        if(sensors.size() > 0) {
//            Sensor s =sensors.get(0);
//            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
//        }
//        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        return mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showToast(String text){
        //Log.i(LOG_TAG, text);
        Toast ts = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        ts.setGravity(Gravity.BOTTOM , 0, 100);
        ts.show();
    }
}
