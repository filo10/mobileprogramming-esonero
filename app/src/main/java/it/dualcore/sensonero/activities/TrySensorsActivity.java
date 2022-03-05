package it.dualcore.sensonero.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.dualcore.sensonero.R;
import it.dualcore.sensonero.activities.try_sensors.AccelerometerDataActivity;
import it.dualcore.sensonero.activities.try_sensors.GyroscopeDataActivity;
import it.dualcore.sensonero.activities.try_sensors.LightDataActivity;
import it.dualcore.sensonero.activities.try_sensors.MagnetometerDataActivity;
import it.dualcore.sensonero.activities.try_sensors.ProximityDataActivity;

public class TrySensorsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_accelerometer, btn_gyroscope, btn_magnetometer, btn_proximity, btn_brightness;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_sensors);

        btn_accelerometer = findViewById(R.id.btn_1_acc);
        btn_gyroscope = findViewById(R.id.btn_2_gyro);
        btn_magnetometer = findViewById(R.id.btn_3_magn);
        btn_proximity = findViewById(R.id.btn_4_prox);
        btn_brightness = findViewById(R.id.btn_5_bright);

        btn_accelerometer.setOnClickListener(this);
        btn_gyroscope.setOnClickListener(this);
        btn_magnetometer.setOnClickListener(this);
        btn_proximity.setOnClickListener(this);
        btn_brightness.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        hideMissingSensorsButtons();
    }

    public void hideMissingSensorsButtons() {
        //check if some sensor of interest does not exist, if so don't show the relative button
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
            // Failure! No accelerometer. Hide relative button.
            btn_accelerometer.setVisibility(View.GONE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null){
            // Failure! No gyroscope. Hide relative button.
            btn_gyroscope.setVisibility(View.GONE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            // Failure! No magnetometer. Hide relative button.
            btn_magnetometer.setVisibility(View.GONE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null){
            // Failure! No proximity sensor. Hide relative button.
            btn_proximity.setVisibility(View.GONE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null){
            // Failure! No light sensor. Hide relative button.
            btn_brightness.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_1_acc) {
            Intent i = new Intent(this, AccelerometerDataActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_2_gyro) {
            Intent i = new Intent(this, GyroscopeDataActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_3_magn) {
            Intent i = new Intent(this, MagnetometerDataActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_4_prox) {
            Intent i = new Intent(this, ProximityDataActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_5_bright) {
            Intent i = new Intent(this, LightDataActivity.class);
            startActivity(i);
        }
    }
}
