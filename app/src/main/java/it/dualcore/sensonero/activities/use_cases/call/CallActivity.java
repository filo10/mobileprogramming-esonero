package it.dualcore.sensonero.activities.use_cases.call;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import it.dualcore.sensonero.R;

public class CallActivity extends AppCompatActivity implements SensorEventListener {

    private PowerManager pm;
    private PowerManager.WakeLock mWakeLock;

    SensorManager sensorManager;
    private Sensor proximitySensor;

    private ToggleButton tgl_callSwitch, tgl_vivavoce;
    private TextView tv_vivavoce, tv_timer;
    Chronometer chrono;
    MediaPlayer ringer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_13call);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "Call:incall");

        tv_timer = findViewById(R.id.tv_timer);
        chrono = findViewById(R.id.chrono_call);

        tgl_callSwitch = findViewById(R.id.tgl_callSwitch);
        tgl_vivavoce = findViewById(R.id.tgl_vivavoce);
        tv_vivavoce = findViewById(R.id.tv_vivavoce);
        tgl_vivavoce.setVisibility(View.GONE);
        tv_vivavoce.setVisibility(View.GONE);
        chrono.setVisibility(View.GONE);

        /* init the ringtone */
        ringer = MediaPlayer.create(this, R.raw.oldphone);
        ringer.start();
        ringer.setLooping(true);

        /* answer the call */
        tgl_callSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    /* starting the call: stop ringing, show and hide some widgets, start a chronometer */
                    ringer.seekTo(0);
                    ringer.pause();
                    tgl_vivavoce.setVisibility(View.VISIBLE);
                    tv_vivavoce.setVisibility(View.VISIBLE);
                    tv_timer.setVisibility(View.GONE);
                    chrono.setVisibility(View.VISIBLE);
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();
                }
                else {
                    /* ending the call: start ringing, show and hide some widgets, stop and hide the chrono*/
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    tgl_vivavoce.setVisibility(View.GONE);
                    tgl_vivavoce.setChecked(false);
                    tv_vivavoce.setVisibility(View.GONE);
                    tv_timer.setVisibility(View.VISIBLE);
                    chrono.stop();
                    chrono.setVisibility(View.GONE);
                    ringer.start();
                }
            }
        });
        /* speakerphone toggle. vivavoce = speakerphone */
        tgl_vivavoce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /* if using the speakerphone, stop using proximity sensor */
                if (isChecked){
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                }
            }
        });
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        /* if call started and speakerphone is OFF */
        if (tgl_callSwitch.isChecked() && !tgl_vivavoce.isChecked()) {
            /* turn off the screen if there is something near the screen */
            float distance = event.values[0];
            if (!mWakeLock.isHeld() && distance < proximitySensor.getMaximumRange())
                mWakeLock.acquire();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        /* if call not started, then ring the phone */
        if (!tgl_callSwitch.isChecked())
            ringer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* stop ringing */
        ringer.seekTo(0);
        ringer.pause();
        /* stop listening to the sensor */
        sensorManager.unregisterListener(this);
        if (mWakeLock.isHeld())
            mWakeLock.release();
    }
}
