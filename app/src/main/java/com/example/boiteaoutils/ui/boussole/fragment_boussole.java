package com.example.boiteaoutils.ui.boussole;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.boiteaoutils.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_boussole extends Fragment {

    private static final String TAG = "Hello";

    private Boussole boussole;

    private float currentAzimuth;
    private PointsCardinauxFormat pointsCardinauxFormat;

    public fragment_boussole() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView" );

        //Initialisation de la classe PointsCardinauxFormat
        pointsCardinauxFormat = new PointsCardinauxFormat(this.getContext());

        //Initialisation de la classe Boussole
        setupBoussole();

        return inflater.inflate(R.layout.fragment_boussole, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start boussole");
        boussole.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume" );
        boussole.start();

        //Bloque l'orientation du fragment en mode portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause" );
        boussole.stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop" );
        boussole.stop();
    }

    private void setupBoussole() {
        boussole = new Boussole(this.getContext());
        Boussole.BoussoleListener cl = getBoussoleListener();
        boussole.setListener(cl);
    }

    private void adjustBoussole(float azimuth) {
        Log.d(TAG, "passage de " + currentAzimuth + "° à " + azimuth +"°");

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        //Récupération de l'imageBoussole et animation de l'image
        ImageView imageBoussole = (ImageView) getView().findViewById(R.id.imageBoussole);
        imageBoussole.startAnimation(an);
    }

    private void adjustPointsCardinauxLabel(float azimuth) {
        //Récupération du texte pointCardinaux et changement du texte
        TextView textView = (TextView) getView().findViewById(R.id.pointCardinaux) ;
        textView.setText(pointsCardinauxFormat.format(azimuth));
    }

    private Boussole.BoussoleListener getBoussoleListener() {
        return new Boussole.BoussoleListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustBoussole(azimuth);
                        adjustPointsCardinauxLabel(azimuth);
                    }
                });
            }
        };
    }
}
