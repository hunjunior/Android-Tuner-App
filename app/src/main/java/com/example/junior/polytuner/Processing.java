package com.example.junior.polytuner;


import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Vector;

/**
 * Created by Junior on 2016. 04. 18..
 */


public class Processing {

    //private Vector<Double> freqs = null;
    //private Vector<Double> peakPos = null;
    //private double [][] peaksAndPos = null;
    //private double [] maxAndPos = null;
    //private double snac[] = null;

    //variables for findPeakZC()
    //private double[][] valueAndPosition = null;
    //private double [] maxAndPosZC = null;
    //private Vector<Integer> positions = null;

    //variables for zeroCrossing()
    //private Vector<Integer> indexes = null;

    //variables for max()
    //private double valueAndIndex[] = null;

    //variables for SNAC()
    //private double snacOutput[] = null;








    //public Processing(){}

    //returns the signal after executing SNAC algorithm
    public static double[] SNAC ( double [] input){

        double snacOutput[]=null;
        //snacOutput = null;

        if(input != null && input.length != 0) {

            int size = input.length;
            snacOutput = new double[size / 2];
            double r[]= new double[size / 2];
            double m[]= new double[size / 2];

            for (int i = 0; i < size / 2; ++i) {
                for (int j = 0; j < size / 2; ++j) {
                    r[i] += input[j] * input[j + i];
                    m[i] += input[j] * input[j] + input[j + i] * input[j + i];
                }
                if (m[i] != 0) {
                    snacOutput[i] = (2*r[i])/m[i];
                }
            }

        }
        //else snacOutput = null;

        return snacOutput;
    }

    //returns an index vector positions, the input signal
    //crosses zero at positions[i] or at between positions[i] and positions[i] + 1
    public static Vector<Integer> zeroCrossing (double [] input){

        Vector<Integer> indexes = null;
        //indexes = null;

        if(input != null) {

            indexes = new Vector<>();
            int size = input.length;

            if (size > 1) {
                for (int i = 0; i < size - 1; ++i) {
                    if (input[i] == 0) indexes.add(i);
                    else if (input[i] * input[i + 1] < 0) indexes.add(i);
                }
                if (input[size - 1] == 0) indexes.add(size - 1);
            }
            else indexes = null;
        }
        //else indexes = null;

        return indexes;
    }

    public static double interpolPeak(double a,double b,double c){
        double ret = 0;
        if(!(a == b && c == b)) {
            ret = (0.5 * (0.5 * ((c - a) * (c - a))) / (2 * b - a - c));
        }
        return ret;
    }

    public static double interpolLoc(double a,double b,double c){
        double ret = 0;
        if(!(a == b && c == b)) {
            ret =  ((0.5 * (c - a)) / (2. * b - a - c));
        }
        return ret;
    }

    public static double log2(double x){
        return Math.log(x)/Math.log(2.0);
    }

    public static double freqToCent(double freq, double ref){
        double cent = 0;

        if(freq > 0 && ref > 0){
            cent = 69 + 12*log2(freq/ref);
        }
        return cent;
    }

    public static double avg (Vector<Double> input){
        double average = 0;
        if(input != null && input.size()>0) {
            int cntr = 0;
            for (int i = 0; i < input.size(); ++i) {
                average += input.get(i);
                ++cntr;
            }
            average = average / cntr;
        }
        return average;
    }

    //returns the value and index in of the maximum element of input between a and b
    //in valueAndPosition vector where the first element is the max value
    //and the second element is the index of max value in double! so CASTING TO INT will be necessary
    public static double[] max(double [] input, int a, int b){

        double valueAndIndex[] = new double[2];

        if(input != null && a < b && a > -1 && b > -1 && input.length > b){
            valueAndIndex[0] = input[a];
            valueAndIndex[1] = (double)a;
            for(int i = a+1;i<=b;++i){
                if(input[i] > valueAndIndex[0]){
                    valueAndIndex[0] = input[i];
                    valueAndIndex[1] = (double)i;
                }
            }
        }
        else if(input != null && a == 0 && b == 0 && input.length == 1){
            valueAndIndex[0] = input[0];
            valueAndIndex[1] = 0.0;
        }

        else valueAndIndex = null;

        return valueAndIndex;
    }

    //returns the values and positions of peaks in input signal, using zero-crossing
    //in valuesAnsPositions 2xn matrix, where the first row is for values, the second is for positions
    //positions are doubles, so CASTING TO INT will be necessary
    public static double[][] findPeakZC(double [] input){

        double[][] valueAndPosition = null;
        //valueAndPosition = null;

        int a,b;
        double p1,p2,p3;

        double [] maxAndPosZC;

        Vector<Integer> positions = zeroCrossing (input);
        //positions = zeroCrossing (input);

        if(positions != null && positions.size() > 2){

            int arraySize = positions.size();
            arraySize = arraySize/2;

            valueAndPosition = new  double[2][arraySize];

            int L = positions.size();

            for(int i = 0; i < L-2; i=i+2){
                a = positions.get(i) + 1;
                b = positions.get(i+2);
                maxAndPosZC = max(input,a,b);

                p1 = input[(int) maxAndPosZC[1]-1];
                p2 = input[(int) maxAndPosZC[1]]; // anyway, p2 equals with maxAndPos[0]
                p3 = input[(int) maxAndPosZC[1]+1];

                valueAndPosition[0][i/2] = p2 + interpolPeak(p1,p2,p3);
                valueAndPosition[1][i/2] = maxAndPosZC[1] + interpolLoc(p1,p2,p3);
            }

            if(L%2 == 0 && L>2){
                a = positions.get(L-3)+1;
                b = positions.get(L-1);
                maxAndPosZC = max(input,a,b);

                p1 = input[(int) maxAndPosZC[1]-1];
                p2 = input[(int) maxAndPosZC[1]];
                p3 = input[(int) maxAndPosZC[1]+1];

                valueAndPosition[0][arraySize-1] = p2 + interpolPeak(p1,p2,p3);
                valueAndPosition[1][arraySize-1] = maxAndPosZC[1] + interpolLoc(p1,p2,p3);

                if(valueAndPosition[0][arraySize-1] == valueAndPosition[0][arraySize-2]) valueAndPosition[1][arraySize-1] = -1;
            }
        }

        return valueAndPosition;
    }

    public static double[][] findPeak(double [] input, int a, int b){
        double[][] valueAndPosition = null;

        if(input != null && a < b && a > -1 && b > -1 && input.length > b){
            Vector<Double> values = new Vector<>();
            Vector<Double> positions = new Vector<>();

            boolean longPeak = false;

            for(int i=a+1; i<b-1; ++i){
                if(input[i] > input[i-1] && input[i] > input[i+1]) {
                    values.add(input[i]);
                    positions.add((double)i);
                    longPeak = false;
                }

                else if(input[i] > input[i-1] && input[i] == input[i+1]) {
                    longPeak = true;
                }

                else if(longPeak && input[i] > input[i+1]) {
                    values.add(input[i]);
                    positions.add((double)i);
                    longPeak = false;
                }
            }

            int L = values.size();

            valueAndPosition = new double[2][L];

            for(int i=0;i<L;++i){
                valueAndPosition[0][i] = values.get(i);
                valueAndPosition[1][i] = positions.get(i);
            }
        }
        return valueAndPosition;
    }



    public static double getFreqBySNAC(double [] input){
        double freq = 0;
        Vector<Double> freqs = new Vector<>();
        Vector<Double> peakPos = new Vector<>();
        double [][] peaksAndPos;
        double [] maxAndPos;
        int lastElementPos;
        double T = 1.0 / 44100.0;

        double snac[] = SNAC(input);

        //freqs = new Vector<>();
        //peakPos = new Vector<>();

        peaksAndPos = findPeakZC(snac);

        if(peaksAndPos != null && peaksAndPos.length != 0) {

            lastElementPos = peaksAndPos[0].length - 1;
            if (peaksAndPos[1][lastElementPos] == -1) --lastElementPos;

            maxAndPos = max(peaksAndPos[0], 0, lastElementPos);

            if(maxAndPos != null) {

            double act_peak;
            double max_peak = maxAndPos[0];

                for (int i = 0; i <= lastElementPos; ++i) {
                    act_peak = peaksAndPos[0][i];
                    if (act_peak > 0.88 * max_peak) peakPos.add(peaksAndPos[1][i]);
                }

                if (peakPos.size() > 1) {
                    for (int j = 0; j < peakPos.size() - 1; ++j) {
                        freqs.add(1 / ((peakPos.get(j + 1) - peakPos.get(j)) * T));
                    }
                }
                if (!freqs.isEmpty()) freq = avg(freqs);
            }
        }
        return freq;
    }

    public static double [] getPolyFrequencies(double [] input){
        double freqs [] = null;

        if(input!=null && input.length>0) {
            freqs = new double[6];
            double F[] = {82.41, 110, 146.83, 196, 246.94, 329.63}; // frequencies of guitar strings (standard tuning)
            int L = input.length;
            double[][] valuesAndPositions;
            double[] maxAndPos;
            Vector<Integer> actPeaks;
            double actFreq;
            double E_freq = 0, A_freq = 0;
            int ind;

            double downFreq[] = new double[6];
            double upFreq[] = new double[6];
            double cents[] = new double[6];

            double Fs = 44100;

            int down[] = new int[6];
            int up[] = new int[6];

            for (int i = 0; i < F.length; ++i) {
                F[i] = F[i] * 2; //
                cents[i] = 69 + 12 * log2(F[i] / 440);
                downFreq[i] = Math.pow(2.0, ((cents[i] - 0.6 - 69) / 12)) * 440;
                upFreq[i] = Math.pow(2.0, ((cents[i] + 0.6 - 69) / 12)) * 440;
                down[i] = (int) (downFreq[i] * L / Fs + 0.5); // have to add 0.5 to the double value
                up[i] = (int) (upFreq[i] * L / Fs + 0.5); // because casting from double to int never rounds (always do ceil)
            }

            FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
            Complex[] complex_fft = transformer.transform(input, TransformType.FORWARD);
            double[] fft = new double[L / 2 + 1];

            for (int i = 0; i < fft.length; ++i) {
                fft[i] = complex_fft[i].abs() / L;
            }

            for (int j = 0; j < 6; ++j) {
                valuesAndPositions = findPeak(fft, down[j], up[j]);
                maxAndPos = max(valuesAndPositions[0], 0, valuesAndPositions[0].length-1);
                actPeaks = new Vector<>();

                if (j == 5) {
                    for (int i = 0; i < valuesAndPositions[0].length; ++i) {
                        if (valuesAndPositions[0][i] > 0.2 * maxAndPos[0]) {
                            actPeaks.add(i);
                        }
                    }

                    if (actPeaks.size() > 1) {
                        for (int i = 0; i < actPeaks.size(); ++i) {
                            ind = (int) valuesAndPositions[1][actPeaks.get(i)];
                            actFreq = ((double) ind + interpolLoc(fft[ind - 1], fft[ind], fft[ind + 1])) * Fs / L;

                            if (actFreq > A_freq * 3 - 1 && actFreq < A_freq * 3 + 1) {
                                valuesAndPositions[0][actPeaks.get(i)] = valuesAndPositions[0][actPeaks.get(i)] / 5;
                            }
                        }
                    }
                }

                if (j == 4) {
                    for (int i = 0; i < valuesAndPositions[0].length; ++i) {
                        if (valuesAndPositions[0][i] > 0.2 * maxAndPos[0]) {
                            actPeaks.add(i);
                        }
                    }

                    if (actPeaks.size() > 1) {
                        for (int i = 0; i < actPeaks.size(); ++i) {
                            ind = (int) valuesAndPositions[1][actPeaks.get(i)];
                            actFreq = ((double) ind + interpolLoc(fft[ind - 1], fft[ind], fft[ind + 1])) * Fs / L;

                            if (actFreq > E_freq * 3 - 2 && actFreq < E_freq * 3 + 2) {
                                valuesAndPositions[0][actPeaks.get(i)] = valuesAndPositions[0][actPeaks.get(i)] / 2;
                            }
                        }
                    }
                }

                maxAndPos = max(valuesAndPositions[0], 0, valuesAndPositions[0].length-1);
                if(maxAndPos == null){
                    freqs[j] = 0;
                }
                else {
                    ind = (int) valuesAndPositions[1][(int) maxAndPos[1]];
                    freqs[j] = ((double) ind + interpolLoc(fft[ind - 1], fft[ind], fft[ind + 1])) * Fs / L;
                }

                if (j == 1) A_freq = freqs[j];
                else if (j == 0) E_freq = freqs[j];
            }

            for(int i=0;i<6;++i){
                freqs[i] = freqs[i]/2;
            }
        }
        return freqs;
    }
}
