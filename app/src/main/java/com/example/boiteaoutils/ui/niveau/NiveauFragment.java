package com.example.boiteaoutils.ui.niveau;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boiteaoutils.R;

import static android.content.Context.SENSOR_SERVICE;
import static android.util.Half.EPSILON;

/**
 * A simple {@link Fragment} subclass.
 */
public class NiveauFragment extends Fragment {

    private static final String TAG = "Niveau";
    private static final float AXE_X_ORIGINE = 270;
    private static final float AXE_Y_ORIGINE = 535;

    private SensorManager mSensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;

    private AnimationListener myAnimationListener;

    private Sensor gsensor; //gyro sensor
    private Sensor msensor;// magnetic sensor
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];

    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;


    public NiveauFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_niveau, container, false);

        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);



        if (gyroscopeSensor == null){
            Toast.makeText(getContext(), "The device has no gyroscope", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {


                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    float axisX = event.values[0];
                    float axisY = event.values[1];
                    float axisZ = event.values[2];

                    Log.d(TAG, "Gyroscope X = " + axisX);
                    Log.d(TAG, "Gyroscope Y = " + axisY);
                    Log.d(TAG, "Gyroscope Z = " + axisZ);


                    TextView textX2 = (TextView) getView().findViewById(R.id.textX2) ;
                    textX2.setText("ACCELEROMETER X = " + (float)Math.round(axisX * 100) / 100);
                    TextView textY2 = (TextView) getView().findViewById(R.id.textY2) ;
                    textY2.setText("ACCELEROMETER Y = " + (float)Math.round(axisY * 100) / 100);
                    TextView textZ2 = (TextView) getView().findViewById(R.id.textZ2) ;
                    textZ2.setText("ACCELEROMETER Z = " + (float)Math.round(axisZ * 100) / 100);


                    ImageView imageNiveau = (ImageView) getView().findViewById(R.id.imageNiveau);

                    float density = getContext().getResources().getDisplayMetrics().density;

                    float xInDp = AXE_X_ORIGINE + (float)Math.round(axisX * 100) / density;
                    float yInDp = AXE_Y_ORIGINE - (float)Math.round(axisY * 100) / density;

                    imageNiveau.setX(xInDp);
                    imageNiveau.setY(yInDp);

                }


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {


            }
        };

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume" );
        mSensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(gyroscopeEventListener,gsensor,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(gyroscopeEventListener,msensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause" );
        mSensorManager.unregisterListener(gyroscopeEventListener);
    }
}
