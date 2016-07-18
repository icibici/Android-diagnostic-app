package com.neurotechx.smartphonebci.dsp;

/**
 * Created by javi on 17/07/16.
 */
public class FFTValues extends AbstractBinnedValues{


    public FFTValues(double[] x, double samplingFreq, double nFFT) {
        mValues = new double[x.length/2];
        System.arraycopy(x,0,mValues,0,x.length/2);
        double nBins = nFFT/2;
        mResolution = samplingFreq*.5/nBins;
    }




}
