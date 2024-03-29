package com.example.boiteaoutils.ui.boussole;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class Boussole implements SensorEventListener {

    private static final String TAG = "Boussole";

    public interface BoussoleListener {
        void onNewAzimuth(float azimuth);
    }

    private BoussoleListener listener;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor; //accelerometer sensor
    private Sensor magneticSensor;// magnetic sensor

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] R = new float[9];
    private float[] I = new float[9];

    private float azimuth;
    private float azimuthFix;

    public Boussole(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setAzimuthFix(float fix) {
        azimuthFix = fix;
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    public void setListener(BoussoleListener l) {
        listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0]; //X

                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];//Y

                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];//Z

                // mGravity = event.values;
                Log.i(TAG+" ACCELEROMETER X", Float.toString(mGravity[0]));
                Log.i(TAG+" ACCELEROMETER Y", Float.toString(mGravity[1]));
                Log.i(TAG+" ACCELEROMETER Z", Float.toString(mGravity[2]));
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];//X
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];//Y
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];//Z
                Log.i(TAG+" Magnetic X", Float.toString(event.values[0]));
                Log.i(TAG+" Magnetic Y", Float.toString(event.values[1]));
                Log.i(TAG+" Magnetic Z", Float.toString(event.values[2]));
            }

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                Log.d(TAG, "azimuth (rad): " + azimuth);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation

                azimuth = (azimuth + azimuthFix + 360) % 360;
                Log.d(TAG, "azimuth (deg): " + azimuth);
                if (listener != null) {
                    listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
