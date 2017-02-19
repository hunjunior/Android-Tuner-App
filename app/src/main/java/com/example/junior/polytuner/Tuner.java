package com.example.junior.polytuner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;


/**
 * Created by David Pituk on 2016. 04. 18..
 */
public class Tuner {

    private double refFreq = 440;
    private static final int SAMPLERATE = 44100;
    private static final int CHANNEL_IN_MONO = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING_PCM_16BIT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;

    int bufferSize;
    boolean isListening = false;

    short sData[];
    double dData[];

    //public Processing processing = null;

    public Tuner(int buffer_size) {
        bufferSize = buffer_size;
        sData = new short[bufferSize];
        dData = new double[bufferSize];
        //processing = new Processing();
    }

    public void setRefFreq(double refFreq){
        this.refFreq = refFreq;
    }

    public void startListening() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLERATE, CHANNEL_IN_MONO, ENCODING_PCM_16BIT, bufferSize * 2);
        recorder.startRecording();
        isListening = true;
    }

    public void stopListening() {
        if (null != recorder) {
            isListening = false;
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public double getFrequencySNAC(){

        double dElement,freq = 0;

        if(isListening){
            recorder.read(sData, 0, bufferSize);

            for(int i=0;i<bufferSize;++i){
                dElement = (double) sData[i];
                dData[i] = dElement/32768; //normáljuk a shortot -1 és 1 közé
            }

            freq = Processing.getFreqBySNAC(dData);
        }
        return freq;
    }

    public String [] freqToSign(double freq){
        String sign [] = new String[2];
        double midi = Processing.freqToCent(freq,refFreq);
        double cent = midi - Math.round(midi);
        if(cent < -0.05){
            sign[0] = "b ";
            sign[1] = "   ";
        }
        else if(cent > 0.05){
            sign[0] = "   ";
            sign[1] = " #";
        }

        else{
            sign[0] = "   ";
            sign[1] = "   ";
        }
        return sign;
    }

    public double [] getPolyphonic(){
        double dElement;
        double freqs[] = new double[6];

        if(isListening){
            recorder.read(sData, 0, bufferSize);

            for(int i=0;i<bufferSize;++i){
                dElement = (double) sData[i];
                dData[i] = dElement/32768; //normáljuk a shortot -1 és 1 közé
            }

            freqs = Processing.getPolyFrequencies(dData);
        }
        return freqs;
    }

    public String [] getSoundInfo(double freq){

        String sound [] = new String[3];

        double cent = Processing.freqToCent(freq,refFreq);
        int mult_cntr = (int) Math.round(cent)/12;
        double midi = cent - 12*(double)mult_cntr;
        cent = Math.round((midi - Math.round(midi))*100);
        int cent_int = (int) cent;

        if(cent > 0) sound[1] = "+";
        else sound[1] = "";

        sound[1] += String.valueOf(cent_int);

        if(cent < -7) sound[2] = "Tune up!";
        else if(cent > 5) sound[2] = "Tune down!";
        else sound[2] = "In tune. :)";

        midi = Math.round(midi);

        switch ((int)midi){
            case 0:
                sound[0] = "C";
                break;
            case 1:
                sound[0] = "C#";
                break;
            case 2:
                sound[0] = "D";
                break;
            case 3:
                sound[0] = "D#";
                break;
            case 4:
                sound[0] = "E";
                break;
            case 5:
                sound[0] = "F";
                break;
            case 6:
                sound[0] = "F#";
                break;
            case 7:
                sound[0] = "G";
                break;
            case 8:
                sound[0] = "G#";
                break;
            case 9:
                sound[0] = "A";
                break;
            case 10:
                sound[0] = "A#";
                break;
            case 11:
                sound[0] = "B";
                break;
        }

        return sound;

    }
}
