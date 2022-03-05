package it.dualcore.sensonero.activities.try_sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

import it.dualcore.sensonero.R;

public class AccelerometerDataActivity extends AppCompatActivity implements SensorEventListener, CompoundButton.OnCheckedChangeListener {

    float acc_x, acc_y, acc_z;
    TextView tv_accX, tv_accY,tv_accZ, tv_aboutAcc, tv_aboutAcc_values, tv_linAcc_exist, tv_aboutLinAcc, tv_aboutLinAcc_values;
    Switch gravityFilter;
    Boolean linearAcc_exist, gravitySwitch_state;

    SensorManager sensorManager;
    Sensor mAccelerometer, mLinAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_01accelerometer);

        tv_accX = findViewById(R.id.tv_accX);
        tv_accY = findViewById(R.id.tv_accY);
        tv_accZ = findViewById(R.id.tv_accZ);

        tv_aboutAcc = findViewById(R.id.tv_aboutAcc);
        tv_aboutAcc_values = findViewById(R.id.tv_aboutAcc_values);

        tv_linAcc_exist = findViewById(R.id.tv_linAcc_exist);

        tv_aboutLinAcc = findViewById(R.id.tv_aboutLinAcc);
        tv_aboutLinAcc_values = findViewById(R.id.tv_aboutLinAcc_values);

        /* set up the switch */
        gravityFilter = findViewById(R.id.switch_gravityFilter);
        gravityFilter.setOnCheckedChangeListener(this);
        gravitySwitch_state = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        /* get the accelerometer */
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        /* start listening to it */
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        /* get the linear accelerometer */
        mLinAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        /* if is not present, delete some widgets */
        if (mLinAccelerometer == null) {      //the device has no linear accelerometer
            linearAcc_exist = false;
            tv_aboutLinAcc.setVisibility(View.GONE);
            tv_aboutLinAcc_values.setVisibility(View.GONE);
        }
        else
            linearAcc_exist = true;

        /* printing info about accelerometer */
        tv_aboutAcc.setText(String.format(Locale.getDefault(),"%1$s\n%2$s\n%3$s\n%4$s\n%5$s\n%6$s\n%7$s",
                getString(R.string.sensor_about_name),
                getString(R.string.sensor_about_vendor),
                getString(R.string.sensor_about_type),
                getString(R.string.sensor_about_range),
                getString(R.string.sensor_about_resolution),
                getString(R.string.sensor_about_power),
                getString(R.string.sensor_about_delay)));
        tv_aboutAcc_values.setText(String.format(Locale.getDefault(),"%1$s\n%2$s\n%3$s\n%4$s\n%5$s\n%6$s mAh\n%7$s microsec",
                mAccelerometer.getName(),
                mAccelerometer.getVendor(),
                mAccelerometer.getStringType(),
                mAccelerometer.getMaximumRange(),
                mAccelerometer.getResolution(),
                mAccelerometer.getPower(),
                mAccelerometer.getMinDelay()));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        /* filter gravity: ON (and linear accelerometer does exist): */
        if (gravitySwitch_state && linearAcc_exist) {
            /* if data not from linear accelerometer, don't look at it */
            if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
                return;
            tv_accX.setText(String.format(Locale.getDefault(), "%.2f", event.values[0]));
            tv_accY.setText(String.format(Locale.getDefault(), "%.2f", event.values[1]));
            tv_accZ.setText(String.format(Locale.getDefault(), "%.2f", event.values[2]));
        }

        /* if data not from accelerometer, don't look at it */
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        /* filter gravity: ON (and linear accelerometer does not exist): */
        if (gravitySwitch_state) {
            /* try to filter gravity with simple filters... */
            if (!linearAcc_exist) {
                float[] gravity = new float[3];
                final float alpha = 0.8f;

                /* Isolate the force of gravity with the low-pass filter. */
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                /* Remove the gravity contribution with the high-pass filter. */
                acc_x = event.values[0] - gravity[0];   // positive moving left (looking your device)
                acc_y = event.values[1] - gravity[1];   // positive moving downwards
                acc_z = event.values[2] - gravity[2];   // positive moving away from the user

                tv_accX.setText(String.format(Locale.getDefault(), "%.2f", acc_x));
                tv_accY.setText(String.format(Locale.getDefault(), "%.2f", acc_y));
                tv_accZ.setText(String.format(Locale.getDefault(), "%.2f", acc_z));
            }
        }

        /* filter gravity: OFF */
        else {
            /* just show the raw data from sensor */
            acc_x = event.values[0];   //positive moving left
            acc_y = event.values[1];   //positive moving downwards
            acc_z = event.values[2];   //positive moving away from the user
            tv_accX.setText(String.format(Locale.getDefault(), "%.2f", acc_x));
            tv_accY.setText(String.format(Locale.getDefault(), "%.2f", acc_y));
            tv_accZ.setText(String.format(Locale.getDefault(), "%.2f", acc_z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }

    @Override
    protected void onResume() {
        super.onResume();
        /* listen again to the sensor */
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* stop listening to the sensor */
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            gravitySwitch_state = true;
            if(linearAcc_exist) {
                tv_linAcc_exist.setText(getString(R.string.using_lin_acc));

                /* printing info about linear accelerometer, if exists... */
                sensorManager.registerListener(this, mLinAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                tv_aboutLinAcc.setText(String.format(Locale.getDefault(),"%1$s\n%2$s\n%3$s\n%4$s\n%5$s\n%6$s\n%7$s",
                        getString(R.string.sensor_about_name),
                        getString(R.string.sensor_about_vendor),
                        getString(R.string.sensor_about_type),
                        getString(R.string.sensor_about_range),
                        getString(R.string.sensor_about_resolution),
                        getString(R.string.sensor_about_power),
                        getString(R.string.sensor_about_delay)));
                tv_aboutLinAcc_values.setText(String.format(Locale.getDefault(),"%1$s\n%2$s\n%3$s\n%4$s\n%5$s\n%6$s mAh\n%7$s microsec",
                        mLinAccelerometer.getName(),
                        mLinAccelerometer.getVendor(),
                        mLinAccelerometer.getStringType(),
                        mLinAccelerometer.getMaximumRange(),
                        mLinAccelerometer.getResolution(),
                        mLinAccelerometer.getPower(),
                        mLinAccelerometer.getMinDelay()));
            }
            else    /* lie about your ability to filter gravity without a linear accelerometer */
                tv_linAcc_exist.setText(getString(R.string.missing_lin_acc));
        }
        else {
            gravitySwitch_state = false;
            tv_linAcc_exist.setText("");
            tv_aboutLinAcc.setText("");
            tv_aboutLinAcc_values.setText("");
        }
    }
}
