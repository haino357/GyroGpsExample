package com.example.gyrogpsexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private final String TAG = "MainActivity";

    private IGyroService mIGyroService;

    private SensorManager manager;
    private TextView mXAxisValueTextView; //X軸
    private TextView mYAxisValueTextView; //Y軸
    private TextView mZAxisValueTextView; //Z軸

    public float XAxisValue = 0;
    public float YAxisValue = 0;
    public float ZAxisValue = 0;

    private Button mStartButton;

    private IGyroCallback mIGyroCallback = new IGyroCallback.Stub() {
        @Override
        public void result(int w, int h) throws RemoteException {
            // サービスからのコールバック
            // サービスは UI スレッドとは別スレッドで稼働しているため、
            // UI スレッドの View を操作するには、View#post(Runnable r) を利用する
//            mCalcResultTextView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mCalcResultTextView.setText(String.valueOf(w * h));
//                }
//            });
        }
    };

    /**
     * サービスと接続・切断された時の処理
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        /**
         * サービスに接続された
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service

            Log.d(TAG, "onServiceConnected()");
            //サービス上の呼び出せる IGyroServiceのインスタンスを取得する。
            mIGyroService = IGyroService.Stub.asInterface(service);


            // サービスから取得した文字列画面に表示する


//            try {
//                XAxisValue = mIGyroService.getXAxisGyroValue();
//                YAxisValue = mIGyroService.getYAxisGyroValue();
//                ZAxisValue = mIGyroService.getZAxisGyroValue();
////                mXAxisValueTextView.setText(String.valueOf(mIGyroService.getXAxisGyroValue()));
////                mYAxisValueTextView.setText(String.valueOf(mIGyroService.getYAxisGyroValue()));
////                mZAxisValueTextView.setText(String.valueOf(mIGyroService.getZAxisGyroValue()));
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
            mIGyroService = null;
        }
    };

    //---------- Event Listener ----------//
    private View.OnClickListener startServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "startButton.onClick()");
            //サービスを開始する
            Intent serviceIntent = new Intent(getApplicationContext(), GyroService.class);
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        mXAxisValueTextView = (TextView) findViewById(R.id.XAxisValueTextView);
        mYAxisValueTextView = (TextView) findViewById(R.id.YAxisValueTextView);
        mZAxisValueTextView = (TextView) findViewById(R.id.ZAxisValueTextView);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mStartButton = (Button) findViewById(R.id.startButton);

        mStartButton.setOnClickListener(startServiceListener);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //Listenner登録
//        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_GYROSCOPE);
//
//        if(sensors.size() > 0) {
//            Sensor s =sensors.get(0);
//            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            String Xstr = "X軸：" + String.valueOf(event.values[0]);
//            String Ystr = "Y軸：" + String.valueOf(event.values[1]);
//            String Zstr = "Z軸：" + String.valueOf(event.values[2]);
            mXAxisValueTextView.setText(String.valueOf(XAxisValue));
            mYAxisValueTextView.setText(String.valueOf(YAxisValue));
            mZAxisValueTextView.setText(String.valueOf(ZAxisValue));
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
