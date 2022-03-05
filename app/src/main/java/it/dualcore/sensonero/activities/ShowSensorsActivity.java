package it.dualcore.sensonero.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.dualcore.sensonero.R;
import it.dualcore.sensonero.activities.show_sensors.CardAdapter;
import it.dualcore.sensonero.activities.show_sensors.SensorInfo;

public class ShowSensorsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsensors);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //get all the sensors
        SensorManager sensorManager;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // a list of SensorInfo
        List<SensorInfo> sensorInfo_list;
        sensorInfo_list = new ArrayList<>();

        // fill the list
        for (int i = 0; i < deviceSensors.size(); i++){
            Sensor class_sensor = deviceSensors.get(i);
            int num = i+1;
            sensorInfo_list.add(new SensorInfo(class_sensor.getName(), class_sensor.getStringType(), num));
        }

        CardAdapter adapter = new CardAdapter(sensorInfo_list);
        recyclerView.setAdapter(adapter);
    }
}
