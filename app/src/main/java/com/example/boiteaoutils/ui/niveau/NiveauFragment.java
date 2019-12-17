package com.example.boiteaoutils.ui.niveau;


import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boiteaoutils.R;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NiveauFragment extends Fragment {

    private static final String TAG = "NiveauFragment";

    private static final float AXE_X_ORIGINE = 265;
    private static final float AXE_Y_ORIGINE = 535;
    private static final double PENSANTEUR = 9.81;
    private static final float ANGLE_DROIT = 90; //Angle à 90°

    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private SensorEventListener accelerometerEventListener;

    public NiveauFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_niveau, container, false);

        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        ImageView imageNiveau = (ImageView) view.findViewById(R.id.imageNiveau);
        Log.wtf(TAG, "X = " + imageNiveau.getX());
        Log.wtf(TAG, "Y = " + imageNiveau.getY());

        if (accelerometerSensor == null){
            Toast.makeText(getContext(), "La téléphone n'a pas accès à l'accéléromètre", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        accelerometerEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    //Récupération de des axes X, Y et Z
                    float axisX = event.values[0];
                    float axisY = event.values[1];
                    float axisZ = event.values[2];

                    Log.d(TAG, "X = " + axisX);
                    Log.d(TAG, "Y = " + axisY);
                    Log.d(TAG, "Z = " + axisZ);
                    
                    //Gestion de la position X et Y du cercle
                    float density = getContext().getResources().getDisplayMetrics().density;
                    float xInDp = AXE_X_ORIGINE + (float)Math.round(axisX * 50 ) / density;
                    float yInDp = AXE_Y_ORIGINE - (float)Math.round(axisY * 50 ) / density;

                    //Animation de l'image en fonction de l'axe X et Y
                    ImageView imageNiveau = (ImageView) getView().findViewById(R.id.imageNiveau);
                    imageNiveau.setX(xInDp);
                    imageNiveau.setY(yInDp);

                    //Calcul des angles selon les axes des X et Y
                    float angleX = Math.abs(Math.round(((axisX * ANGLE_DROIT) / PENSANTEUR)));
                    float angleY = Math.abs(Math.round(((axisY * ANGLE_DROIT) / PENSANTEUR)));
                    Log.d(TAG, "Angle X = " + angleX);
                    Log.d(TAG, "Angle Y = " + angleY);

                    //Affichage des angles calculés
                    TextView textAngleX = (TextView) getView().findViewById(R.id.textAngleX) ;
                    textAngleX.setText("Angle X = " + angleX  + "°");
                    TextView textAngleY = (TextView) getView().findViewById(R.id.textAngleY) ;
                    textAngleY.setText("Angle Y = " + angleY  + "°");
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
        mSensorManager.registerListener(accelerometerEventListener, accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

        //Bloque l'orientation du fragment en mode portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause" );
        mSensorManager.unregisterListener(accelerometerEventListener);
    }
}
