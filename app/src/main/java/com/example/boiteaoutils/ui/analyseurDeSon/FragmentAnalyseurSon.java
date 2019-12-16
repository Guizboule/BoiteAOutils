package com.example.boiteaoutils.ui.analyseurDeSon;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.boiteaoutils.R;
import android.widget.Button;



import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAnalyseurSon extends Fragment implements MicroRecorderListener{

    private static final String TAG = "TAG_analyseurSon";
    MicroRecorder mr = null;
    private Handler hdlr;

    private GraphView graph;
    private LineGraphSeries<DataPoint> series = null;
    private Button btn;


    private View root;
    public FragmentAnalyseurSon() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "FragmentAnalyseurSon - onCreateView");
        View root = inflater.inflate(R.layout.fragment_analyseur_son, container, false);
        graph = root.findViewById(R.id.id_graph);
        series = new LineGraphSeries<DataPoint>();

        btn = root.findViewById(R.id.id_buttonStart);

        hdlr = new Handler(){
            public void handleMessage(Message msg) {
                Log.i(TAG, "Message reçu!");
                series = (LineGraphSeries<DataPoint>) msg.obj;
                graph.removeAllSeries(); //Supprimer la précédente liste pour empecher de superposer les courbes
                graph.addSeries(series);
                //Maj des axes
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(200000);
            }
        };

        Button button = (Button) root.findViewById(R.id.id_buttonStart);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mr != null){
                    mr.stopRecording();
                    mr = null;
                    btn.setText("Start Recording");
                }else{
                    mr = new MicroRecorder();
                    mr.addListener(FragmentAnalyseurSon.this);
                    mr.startRecording();
                    btn.setText("Stop Recording");
                }
            }
        });

        return root;
    }

    @Override
    public void send(LineGraphSeries<DataPoint> data) {
        Message msg = new Message();
        msg.obj = data;
        hdlr.sendMessage(msg);
    }
}
