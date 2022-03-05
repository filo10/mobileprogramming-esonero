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

import it.dualcore.sensonero.R;

public class ProximityDataActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_04proximity);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        tv = (TextView) findViewById(R.id.tv_proxy);

        ListView lv;
        lv = (ListView) findViewById(R.id.lv_proxy);

        String[] sensorInfoList = {
                getString(R.string.sensor_about_name)+" "+proximitySensor.getName(),
                getString(R.string.sensor_about_vendor)+" "+proximitySensor.getVendor(),
                getString(R.string.sensor_about_version)+" "+String.format("%d",proximitySensor.getVersion()),
                getString(R.string.sensor_about_type)+" "+String.format("%s",proximitySensor.getStringType()),
                getString(R.string.sensor_about_range)+" "+String.format("%f",proximitySensor.getMaximumRange()),
                getString(R.string.sensor_about_resolution)+" "+String.format("%f", proximitySensor.getResolution()),
                getString(R.string.sensor_about_power)+" "+String.format("%f", proximitySensor.getPower()),
                getString(R.string.sensor_about_delay)+" "+String.format("%d", proximitySensor.getMinDelay())
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensorInfoList);
        lv.setAdapter(adapter);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance >= proximitySensor.getMaximumRange())
            tv.setText(getString(R.string.str_no_obj_detect));
        else tv.setText(getString(R.string.str_obj_detected));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
