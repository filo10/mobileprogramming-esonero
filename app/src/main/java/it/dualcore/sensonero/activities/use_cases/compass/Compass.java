package it.dualcore.sensonero.activities.use_cases.compass;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/***************************************************************************************************
*   credits for this compass app to: https://github.com/iutinvg/
*   code partially modified by Filippo Maria Briscese
***************************************************************************************************/

public class Compass implements SensorEventListener {

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }

    private CompassListener listener;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReaging = new float[3];
    private float[] rotationMatrix = new float[9];


    private float azimuth;
    private float azimuthFix;

    public Compass(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    public void setAzimuthFix(float fix) {
        azimuthFix = fix;
    }

    public void start() {
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    public void setListener(CompassListener l) {
        listener = l;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerReading[0] = alpha * accelerometerReading[0] + (1 - alpha)* event.values[0];
                accelerometerReading[1] = alpha * accelerometerReading[1] + (1 - alpha)* event.values[1];
                accelerometerReading[2] = alpha * accelerometerReading[2] + (1 - alpha)* event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetometerReaging[0] = alpha * magnetometerReaging[0] + (1 - alpha)* event.values[0];
                magnetometerReaging[1] = alpha * magnetometerReaging[1] + (1 - alpha)* event.values[1];
                magnetometerReaging[2] = alpha * magnetometerReaging[2] + (1 - alpha)* event.values[2];
            }

            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading,
                    magnetometerReaging);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation Matrix from radians to degrees
                azimuth = (azimuth + azimuthFix + 360) % 360;   // smoothing the result and avoiding problems passing over 0
                if (listener != null) {
                    listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }
}