package com.example.junior.polytuner;

/**
 * Created by Junior on 2016. 05. 15..
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PolyphonicActivity extends Activity {

    private boolean isTuning = false;
    private PolyphonicAsyncTask myPolyphonicAsyncTask = null;

    public TextView E2text = null;
    public TextView A2text = null;
    public TextView D3text = null;
    public TextView G3text = null;
    public TextView B3text = null;
    public TextView E4text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyphonic);

        E2text = (TextView)findViewById(R.id.E2text);

        A2text = (TextView)findViewById(R.id.A2text);

        D3text = (TextView)findViewById(R.id.D3text);

        G3text = (TextView)findViewById(R.id.G3text);

        B3text = (TextView)findViewById(R.id.B3text);

        E4text = (TextView)findViewById(R.id.E4text);
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

    public void startTuning() {
        if (!isTuning) {
            isTuning = true;
            myPolyphonicAsyncTask = new PolyphonicAsyncTask();
            myPolyphonicAsyncTask.setTextViews(E2text, A2text, D3text, G3text, B3text, E4text);
            myPolyphonicAsyncTask.execute();
        }
    }

    public void stopTuning(){
        if(isTuning && myPolyphonicAsyncTask!=null) {
            isTuning = false;
            myPolyphonicAsyncTask.stopTuning();
            myPolyphonicAsyncTask.cancel(true);
            myPolyphonicAsyncTask = null;
        }
    }

    public void startChromaticActivity(View view){
        Intent intent = new Intent(this, ChromaticActivity.class);
        startActivity(intent);
    }
}

