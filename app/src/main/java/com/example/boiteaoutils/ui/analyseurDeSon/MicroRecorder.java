package com.example.boiteaoutils.ui.analyseurDeSon;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.example.boiteaoutils.ui.analyseurDeSon.FFT.fft;

public class MicroRecorder extends Thread {

    private static final String TAG = "TAG_analyseurSon";

    private AudioRecord micro = null;
    private static final int source = MediaRecorder.AudioSource.MIC;
    private static final int sampleRate = 44100;
    private static final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private static final int format = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSize = 0;


    private boolean isRecording = false;
    private short[] data;
    private LineGraphSeries<DataPoint> series = null;

    private MicroRecorderListener listener = null;

    /* **********************************
    ************* CONSTRUCTOR ***********
    *************************************/
    public MicroRecorder(){
        this.bufferSize = 4096;// AudioRecord.getMinBufferSize(this.sampleRate, this.channelConfig, this.format);
        Log.d(TAG, "recorder buffer size = " + this.bufferSize);
        this.micro = new AudioRecord(source, sampleRate, channelConfig, format, this.bufferSize);
        this.series = new LineGraphSeries<DataPoint>();
        this.data = new short[this.bufferSize];
        this.isRecording = false;
    }

    public boolean isRecording(){
        return this.isRecording;
    }

    public void startRecording(){
        this.isRecording = true;
        this.start();
    }

    public void stopRecording(){
        this.isRecording = false;
    }

    public void run(){
        this.micro.startRecording();
        int success=0;
        Log.i(TAG, "etat: "+this.micro.getState());
        Log.i(TAG, "WHILE");
        Complex[] x = new Complex[this.bufferSize];
        while (this.isRecording()){
            Log.i(TAG, "smpl: " + sampleRate);
            this.series = new LineGraphSeries<DataPoint>();
            success = this.micro.read(this.data, 0, this.bufferSize, AudioRecord.READ_BLOCKING);
            for (int i=0; i<this.bufferSize; i++){
                //Log.i(TAG, "Double : " + this.data[i]);
                x[i] = new Complex(this.data[i], 0);
            }

            Complex[] y = fft(x);

            for (int i=0; i<this.bufferSize/2; i++){
                //Log.i(TAG, "Double : " + this.data[i]);
                this.series.appendData(new DataPoint((double)(2*i*20000)/(this.bufferSize),
                        (double)y[i].abs()),
                        true, this.bufferSize);
            }
            if(this.listener!=null){
                this.listener.send(this.series);
            }
        }
        this.micro.stop();
        Log.i(TAG, "Fin du thread");
    }

    public void addListener(MicroRecorderListener l){
        this.listener = l;
    }

    public void delListener(){
        this.listener = null;
    }




}
