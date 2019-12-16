package com.example.boiteaoutils.ui.analyseurDeSon;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public interface MicroRecorderListener {

    public void send(LineGraphSeries<DataPoint> data);

}
