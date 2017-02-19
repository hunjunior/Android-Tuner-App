package com.example.junior.polytuner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ChromaticActivity extends Activity {

    private boolean isTuning = false;
    LinearLayout lights = null;
    //public TextView freqOutput = null;
    //public TextView line = null;
    public TextView soundOutput = null;
    public TextView centOutput = null;
    //public TextView messageOutput = null;
    private ChromaticAsyncTask myChromaticAsyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chromatic);

        //freqOutput = (TextView)findViewById(R.id.textFreq);

        /*line = (TextView)findViewById(R.id.textMIDI);
        if (line != null) {
            line.setText("-------------------");
        }*/

        soundOutput = (TextView) findViewById(R.id.textSound);
        lights = (LinearLayout) findViewById(R.id.lights);

        centOutput = (TextView) findViewById(R.id.textCent);

        //messageOutput = (TextView) findViewById(R.id.textMessage);

    }

    @Override
    public void onPause(){
        super.onPause();
        stopTuning();
    }

    @Override
    public void onResume(){
        super.onResume();
        startTuning();
    }

    public void startTuning(){
        if (!isTuning) {
            isTuning = true;
            myChromaticAsyncTask = new ChromaticAsyncTask();
            //myChromaticAsyncTask.setTextViews(freqOutput,soundOutput,centOutput,messageOutput);
            myChromaticAsyncTask.setTextViews(soundOutput, lights, centOutput);
            myChromaticAsyncTask.execute();
        }
    }

    public void stopTuning(){
        if(isTuning && myChromaticAsyncTask!=null) {
            isTuning = false;
            myChromaticAsyncTask.stopTuning();
            myChromaticAsyncTask.cancel(true);
            myChromaticAsyncTask = null;
        }
    }

    public void startPolyphonicActivity(View view){
        Intent intent = new Intent(this, PolyphonicActivity.class);
        startActivity(intent);
    }

}
