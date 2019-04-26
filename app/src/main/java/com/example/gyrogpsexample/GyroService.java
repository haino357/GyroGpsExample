package com.example.gyrogpsexample;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;

public class GyroService extends Service implements SensorEventListener {

    private final String TAG = "GyroService";

    private SensorManager manager;

    /*
     * AIDLファイルで定義したインターフェースの中身をここに記載する
     */
    private final IGyroService.Stub mBinder = new IGyroService.Stub() {
        @Override
        public int getXAxisGyroValue() throws RemoteException {
            manager = (SensorManager) getSystemService(SENSOR_SERVICE);
            int XGyro = 0;

            SensorEvent event = null;
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                XGyro = (int) event.values[0];
            }

            return XGyro;
        }

        @Override
        public int getYAxisGyroValue() throws RemoteException {
            return 0;
        }

        @Override
        public int getZAxisGyroValue() throws RemoteException {
            return 0;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
