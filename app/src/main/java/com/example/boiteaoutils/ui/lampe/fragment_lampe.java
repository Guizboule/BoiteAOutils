package com.example.boiteaoutils.ui.lampe;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.boiteaoutils.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_lampe extends Fragment {

    Camera camera = null;
    Switch sw;
    boolean isFlashOn = false;


    public fragment_lampe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_lampe, container, false);

        sw = (Switch) view.findViewById(R.id.switchLampe);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("Hello" ," light = " + getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));
                Log.d("Hello" ," switch = " + isChecked);
                if (isFlashOn == false){
                    on();
                }else{
                    off();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void on() {
        if(isFlashOn == true){
            Toast.makeText(getContext(), "Flashlight is already ON", Toast.LENGTH_SHORT).show();
        }else {
            try{
                CameraManager camManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                camManager.setTorchMode(cameraId, true);
                isFlashOn = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void off() {
        if(isFlashOn == false){
            Toast.makeText(getContext(), "Flashlight is already OFF", Toast.LENGTH_SHORT).show();
        }else {
            try{
                CameraManager camManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                camManager.setTorchMode(cameraId, false);
                isFlashOn = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
