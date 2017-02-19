package com.example.junior.polytuner;

import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Junior on 2016. 05. 05..
 */

public class ChromaticAsyncTask extends AsyncTask {

    private Tuner tuner = null;
    public LinearLayout lights = null;
    //public TextView freqOutput = null;
    public TextView soundOutput = null;
    public TextView centOutput = null;
    //public TextView messageOutput = null;
    private boolean isTuning = false;

    /*public void setTextViews(TextView freqOutput,TextView soundOutput, TextView centOutput,TextView messageOutput){
        this.freqOutput = freqOutput;
        this.soundOutput = soundOutput;
        this.centOutput = centOutput;
        this.messageOutput = messageOutput;
    }*/

    public void setTextViews(TextView soundOutput, LinearLayout lights, TextView centOutput){
        this.soundOutput = soundOutput;
        this.lights = lights;
        this.centOutput = centOutput;
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

        if(!isTuning) {
            String[] stringInfo = new String[4];
            isTuning = true;
            tuner = new Tuner(4500);
            tuner.startListening();

            double prev_freq = 0;

            while (isTuning) {

                final double prev_freq_out = prev_freq;

                final double freq = tuner.getFrequencySNAC();

                if (freq == 0) {
                    String freq_string = String.valueOf(Math.round(prev_freq_out * 100) / 100.0) + " Hz";
                    stringInfo[0] = freq_string;
                    String tuneInfo[] = tuner.getSoundInfo(prev_freq_out);
                    stringInfo[1] = tuneInfo[0];
                    stringInfo[2] = tuneInfo[1];
                    stringInfo[3] = tuneInfo[2];

                } else {
                    String freq_string = String.valueOf(Math.round(freq * 100) / 100.0) + " Hz";
                    stringInfo[0] = freq_string;
                    String tuneInfo[] = tuner.getSoundInfo(freq);
                    stringInfo[1] = tuneInfo[0];
                    stringInfo[2] = tuneInfo[1];
                    stringInfo[3] = tuneInfo[2];
                }

                if (freq != 0) {
                    prev_freq = freq;
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
        //freqOutput.setText(stringInfo[0]);
        soundOutput.setText(stringInfo[1]);
        centOutput.setText(stringInfo[2]);
        if(stringInfo[3].equals("In tune. :)")) lights.setBackgroundResource(R.drawable.good);
        else if(stringInfo[3].equals("Tune down!")) lights.setBackgroundResource(R.drawable.high);
        else lights.setBackgroundResource(R.drawable.low);
        //centOutput.setText(stringInfo[2]);
        //messageOutput.setText(stringInfo[3]);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

}
