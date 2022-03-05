package it.dualcore.sensonero.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import it.dualcore.sensonero.R;
import it.dualcore.sensonero.activities.use_cases.call.CallActivity;
import it.dualcore.sensonero.activities.use_cases.compass.CompassActivity;
import it.dualcore.sensonero.activities.use_cases.saber.SaberActivity;


public class UseCasesActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_usescases_info;
    ImageButton btn_compass, btn_call, btn_saber;

    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_use_cases);

        btn_usescases_info = findViewById(R.id.btn_usecases_info);
        btn_usescases_info.setOnClickListener(this);

        btn_compass = findViewById(R.id.btn_compass);
        btn_compass.setOnClickListener(this);

        btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);

        btn_saber = findViewById(R.id.btn_saber);
        btn_saber.setOnClickListener(this);

        sensorManager = ( SensorManager ) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
            // Failure! No accelerometer. Hide cases that use it.
            btn_saber.setVisibility(View.GONE);
            btn_usescases_info.setVisibility(View.GONE);
            btn_compass.setVisibility(View.GONE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            // Failure! No magnetometer. Hide cases that use it.
            btn_compass.setVisibility(View.GONE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null){
            // Failure! No proximity sensor. Hide cases that use it.
            btn_call.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_compass){
            Intent i = new Intent(this, CompassActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_saber){
            Intent i = new Intent(this, SaberActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_call){
            Intent i = new Intent(this, CallActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btn_usecases_info) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Credits");
            dialog.setMessage("Light Saber Icon made by Those Icons (www.flaticon.com/authors/those-icons) from www.flaticon.com is licensed by:\nhttp://creativecommons.org/licenses/by/3.0/");
            dialog.setPositiveButton(R.string.str_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.create().show();
        }
    }
}
