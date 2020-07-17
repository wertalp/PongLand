package ch.wertal.pongland;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    GameView gameView ;
    TestView testView ;

    Sensor sensor ;
    SensorManager sensorManager;

    SoundPool soundPool ;


    private final float FACTOR_FRICTION = 0.5f; // imaginary friction on the
    // screen
    private final float GRAVITY = 9.8f; // acceleration of gravity
    private float mAx; // acceleration along x axis
    private float mAy; // acceleration along y axis
    private final float mDeltaT = 0.5f; // imaginary time interval between each
    // acceleration updates


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // loadMusik();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void clicked(View view){
        Toast.makeText(this, "clicked", LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // obtain the three accelerations from sensors
        mAx = event.values[0];
        mAy = event.values[1];

        final float mAz = event.values[2];

        // taking into account the frictions
        mAx = Math.signum(mAx) * Math.abs(mAx)
                * (1 - FACTOR_FRICTION * Math.abs(mAz) / GRAVITY);
        mAy = Math.signum(mAy) * Math.abs(mAy)
                * (1 - FACTOR_FRICTION * Math.abs(mAz) / GRAVITY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadMusik() {

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
         soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(attrs)
                .build();
        int soundIds[] = new int[10];
        soundIds[0] = soundPool.load(this, R.raw.birds, 1);
        soundIds[1] = soundPool.load(this, R.raw.birds, 1);
        soundPool.play(1,1,1,0,30,1);
    }

}