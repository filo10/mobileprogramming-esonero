package it.dualcore.sensonero.activities.try_sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import it.dualcore.sensonero.R;


public class GyroscopeDataActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener gyroSensorListener;
    private Sensor gyroSensor;
    private TextView xText, yText, zText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_02gyroscope);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        ListView infoList = (ListView) findViewById(R.id.lv_info);
        xText = (TextView) findViewById(R.id.tv_gyroX);
        yText = (TextView) findViewById(R.id.tv_gyroY);
        zText = (TextView) findViewById(R.id.tv_gyroZ);
        if (gyroSensor == null) {
            Log.e("GyroActivity", "Gyroscope not available.");
            finish(); // Close app
        }

        String[] sensorInfoList = {
                getString(R.string.sensor_about_name)+" "+gyroSensor.getName(),
                getString(R.string.sensor_about_vendor)+" "+gyroSensor.getVendor(),
                getString(R.string.sensor_about_version)+" "+String.format("%d",gyroSensor.getVersion()),
                getString(R.string.sensor_about_type)+" "+String.format("%s",gyroSensor.getStringType()),
                getString(R.string.sensor_about_range)+" "+String.format("%f",gyroSensor.getMaximumRange()),
                getString(R.string.sensor_about_resolution)+" "+String.format("%f", gyroSensor.getResolution()),
                getString(R.string.sensor_about_power)+" "+String.format("%f", gyroSensor.getPower()),
                getString(R.string.sensor_about_delay)+" "+String.format("%d", gyroSensor.getMinDelay())
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensorInfoList);
        infoList.setAdapter(adapter);
        gyroSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                xText.setText(String.format(Locale.getDefault(), "%.2f", sensorEvent.values[0]));
                yText.setText(String.format(Locale.getDefault(), "%.2f", sensorEvent.values[1]));
                zText.setText(String.format(Locale.getDefault(), "%.2f", sensorEvent.values[2]));;
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(gyroSensorListener, gyroSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroSensorListener);
    }
}
