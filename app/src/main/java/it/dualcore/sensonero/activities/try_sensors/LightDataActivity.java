package it.dualcore.sensonero.activities.try_sensors;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import it.dualcore.sensonero.R;

public class LightDataActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_05light);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        tv = (TextView) findViewById(R.id.tv_light);
        ListView lv;
        lv = (ListView) findViewById(R.id.lv_light);

        String[] sensorInfoList = {
                getString(R.string.sensor_about_name)+" "+lightSensor.getName(),
                getString(R.string.sensor_about_vendor)+" "+lightSensor.getVendor(),
                getString(R.string.sensor_about_version)+" "+String.format("%d",lightSensor.getVersion()),
                getString(R.string.sensor_about_type)+" "+String.format("%s",lightSensor.getStringType()),
                getString(R.string.sensor_about_range)+" "+String.format("%f",lightSensor.getMaximumRange()),
                getString(R.string.sensor_about_resolution)+" "+String.format("%f", lightSensor.getResolution()),
                getString(R.string.sensor_about_power)+" "+String.format("%f", lightSensor.getPower()),
                getString(R.string.sensor_about_delay)+" "+String.format("%d", lightSensor.getMinDelay())
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensorInfoList);
        lv.setAdapter(adapter);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        tv.setText(String.format(Locale.getDefault(), "%.2f", lux));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
