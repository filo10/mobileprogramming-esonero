package it.dualcore.sensonero.activities.use_cases.saber;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import it.dualcore.sensonero.R;

public class SaberActivity extends AppCompatActivity implements SensorEventListener {

    ConstraintLayout background;

    ToggleButton tgl_saberSwitch;

    MediaPlayer saber_idle, saber_clash;

    Sensor accelerometer;
    SensorManager sensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_14saber);

        /* get the audio */
        saber_idle = MediaPlayer.create(this, R.raw.lightsaber_idle);
        saber_clash = MediaPlayer.create(this, R.raw.lightsaber_clash);

        background = findViewById(R.id.saber_background);
        tgl_saberSwitch = findViewById(R.id.tgl_saberSwitch);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        /* the sword power button */
        tgl_saberSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    /* wooo! So my saber has green laser */
                    background.setBackground(getDrawable(R.drawable.gradient_background));
                    saber_idle.start();
                    saber_idle.setLooping(true);
                }
                else {
                    /* better keeping my jedi abilities hidden... */
                    background.setBackground(getDrawable(R.color.black_overlay));
                    saber_idle.seekTo(0);
                    saber_idle.pause();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        /* stop playing sound and listening to sensor */
        sensorManager.unregisterListener(this);
        saber_idle.seekTo(0);
        saber_idle.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /* stop playing sound and listening to sensor */
        sensorManager.unregisterListener(this);
        saber_idle.seekTo(0);
        saber_idle.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* listen again to sensor, and check if sword was on, if so resume playing sound */
        if (tgl_saberSwitch.isChecked())
            saber_idle.start();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /* listen to accelerometer */
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        /* if the sword is on */
        if (tgl_saberSwitch.isChecked()) {
            float accX;//, accY, accZ;
            accX = event.values[0];
            //accY = event.values[1];
            //accZ = event.values[2];

            /* playing a sound when you show your incredible skills */
            if ((Math.abs(accX) > 12))  //  || (Math.abs(accY) > 10) || (Math.abs(accZ) > 10))
                saber_clash.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }
}
