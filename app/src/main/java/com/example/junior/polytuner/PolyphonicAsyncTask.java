package com.example.junior.polytuner;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * Created by Junior on 2016. 05. 09..
 */

public class PolyphonicAsyncTask extends AsyncTask {

    private Tuner tuner = null;
    private boolean isTuning = false;

    public TextView E2text = null;
    public TextView A2text = null;
    public TextView D3text = null;
    public TextView G3text = null;
    public TextView B3text = null;
    public TextView E4text = null;

    public void setTextViews(TextView E2text,TextView A2text, TextView D3text,TextView G3text, TextView B3text, TextView E4text){
        this.E2text = E2text;
        this.A2text = A2text;
        this.D3text = D3text;
        this.G3text = G3text;
        this.B3text = B3text;
        this.E4text = E4text;
    }

    public void stopTuning(){
        if(isTuning){
            isTuning = false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        if (!isTuning) {
            String[] stringInfo = new String[6];
            isTuning = true;
            tuner = new Tuner(32768);
            tuner.startListening();

            final double [] prev_freqs = {0,0,0,0,0,0};

            while (isTuning) {

                final double[] prev_freqs_out = new double[6];

                System.arraycopy(prev_freqs, 0, prev_freqs_out, 0, 6);

                final double [] freqs = tuner.getPolyphonic();

                if (freqs[0] == 0) {
                    stringInfo[0] = (tuner.freqToSign(prev_freqs_out[0])[0] + " E2 " + tuner.freqToSign(prev_freqs_out[0])[1]);
                    stringInfo[1] = (tuner.freqToSign(prev_freqs_out[1])[0] + " A2 " + tuner.freqToSign(prev_freqs_out[1])[1]);
                    stringInfo[2] = (tuner.freqToSign(prev_freqs_out[2])[0] + " D3 " + tuner.freqToSign(prev_freqs_out[2])[1]);
                    stringInfo[3] = (tuner.freqToSign(prev_freqs_out[3])[0] + " G3 " + tuner.freqToSign(prev_freqs_out[3])[1]);
                    stringInfo[4] = (tuner.freqToSign(prev_freqs_out[4])[0] + " B3 " + tuner.freqToSign(prev_freqs_out[4])[1]);
                    stringInfo[5] = (tuner.freqToSign(prev_freqs_out[5])[0] + " E4 " + tuner.freqToSign(prev_freqs_out[5])[1]);
                }

                else {
                    stringInfo[0] = (tuner.freqToSign(freqs[0])[0] + " E2 " + tuner.freqToSign(freqs[0])[1]);
                    stringInfo[1] = (tuner.freqToSign(freqs[1])[0] + " A2 " + tuner.freqToSign(freqs[1])[1]);
                    stringInfo[2] = (tuner.freqToSign(freqs[2])[0] + " D3 " + tuner.freqToSign(freqs[2])[1]);
                    stringInfo[3] = (tuner.freqToSign(freqs[3])[0] + " G3 " + tuner.freqToSign(freqs[3])[1]);
                    stringInfo[4] = (tuner.freqToSign(freqs[4])[0] + " B3 " + tuner.freqToSign(freqs[4])[1]);
                    stringInfo[5] = (tuner.freqToSign(freqs[5])[0] + " E4 " + tuner.freqToSign(freqs[5])[1]);
                }

                /*if (freqs[0] == 0) {
                    stringInfo[0] = ("E2 " + String.valueOf(Processing.freqToCent(prev_freqs_out[0],440)));
                    stringInfo[1] = ("A2 " + String.valueOf(Processing.freqToCent(prev_freqs_out[1],440)));
                    stringInfo[2] = ("D3 " + String.valueOf(Processing.freqToCent(prev_freqs_out[2],440)));
                    stringInfo[3] = ("G3 " + String.valueOf(Processing.freqToCent(prev_freqs_out[3],440)));
                    stringInfo[4] = ("B3 " + String.valueOf(Processing.freqToCent(prev_freqs_out[4],440)));
                    stringInfo[5] = ("E4 " + String.valueOf(Processing.freqToCent(prev_freqs_out[5],440)));

                } else {
                    stringInfo[0] = ("E2 " + String.valueOf(Processing.freqToCent(freqs[0],440)));
                    stringInfo[1] = ("A2 " + String.valueOf(Processing.freqToCent(freqs[1],440)));
                    stringInfo[2] = ("D3 " + String.valueOf(Processing.freqToCent(freqs[2],440)));
                    stringInfo[3] = ("G3 " + String.valueOf(Processing.freqToCent(freqs[3],440)));
                    stringInfo[4] = ("B3 " + String.valueOf(Processing.freqToCent(freqs[4],440)));
                    stringInfo[5] = ("E4 " + String.valueOf(Processing.freqToCent(freqs[5],440)));
                }*/

                if (freqs[0] != 0) {
                    System.arraycopy(freqs, 0, prev_freqs, 0, 6);
                }
                publishProgress(stringInfo);
            }
            tuner.stopListening();
            tuner = null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        String [] stringInfo = (String[]) values;
        E2text.setText(stringInfo[0]);
        A2text.setText(stringInfo[1]);
        D3text.setText(stringInfo[2]);
        G3text.setText(stringInfo[3]);
        B3text.setText(stringInfo[4]);
        E4text.setText(stringInfo[5]);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

}