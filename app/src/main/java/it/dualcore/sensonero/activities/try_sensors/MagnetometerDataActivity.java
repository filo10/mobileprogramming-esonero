package it.dualcore.sensonero.activities.try_sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Locale;

import it.dualcore.sensonero.R;

public class MagnetometerDataActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv_aboutMagn, tv_aboutMagn_values, tv_magX, tv_magY, tv_magZ;

    SensorManager sensorManager;
    Sensor mMagnetometer;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_03magnetometer);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        /* get the magnetometer */
        mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        /* start listen to it */
        sensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        tv_magX = findViewById(R.id.tv_magX);
        tv_magY = findViewById(R.id.tv_magY);
        tv_magZ = findViewById(R.id.tv_magZ);

        tv_aboutMagn = findViewById(R.id.tv_aboutMagn);
        tv_aboutMagn_values = findViewById(R.id.tv_aboutMagn_values);

        // printing info about magnetometer
        tv_aboutMagn.setText(String.format(Locale.getDefault(),"%1$s\n%2$s\n%3$s\n%4$s\n%5$s\n%6$s\n%7$s",
                getString(R.string.sensor_about_name),
                getString(R.string.sensor_about_vendor),
                getString(R.string.sensor_about_type),
                getString(R.string.sensor_about_range),
                getString(R.string.sensor_about_resolution),
                getString(R.string.sensor_about_power),
                getString(R.string.sensor_about_delay)));
        tv_aboutMagn_values.setText(String.format(Locale.getDefault(),"%1$s\n%2$s\n%3$s\n%4$s\n%5$s\n%6$s mAh\n%7$s microsec",
                mMagnetometer.getName(),
                mMagnetometer.getVendor(),
                mMagnetometer.getStringType(),
                mMagnetometer.getMaximumRange(),
                mMagnetometer.getResolution(),
                mMagnetometer.getPower(),
                mMagnetometer.getMinDelay()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* listen again to the sensor */
        sensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* stop listening to the sensor */
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /* if data not from magnetometer, don't look at it */
        if (event.sensor.getType() != Sensor.TYPE_MAGNETIC_FIELD)
            return;
        /* show the raw data from sensor */
        tv_magX.setText(String.format(Locale.getDefault(), "%.2f", event.values[0]));
        tv_magY.setText(String.format(Locale.getDefault(), "%.2f", event.values[1]));
        tv_magZ.setText(String.format(Locale.getDefault(), "%.2f", event.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }
}
