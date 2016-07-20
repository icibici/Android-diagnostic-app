package com.neurotechx.smartphonebci.ssvep;


import com.neurotechx.smartphonebci.driver.dsp.BandPowerAccessor;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValues;

/**
 * implementation of a two class ssvep BCI
 */
public class SSVEP {


    private final  SSVEPListener mListener;


    double mEpochLen;
    double mEpochPos = 0;
    double mPushFrequency;
    BandPowerAccessor.Band[] mClassBands;
    double[] mClassMeans;
    BandPowerAccessor mAccessor;

    /**
     *
     */
    public SSVEP(double epochLen, double pushFrequency, BandPowerAccessor.Band classBands[],SSVEPListener listener) {
        mEpochLen = epochLen;
        mPushFrequency = pushFrequency;
        mClassBands = classBands;
        mClassMeans = new double[mClassBands.length];
        mListener = listener;
        mAccessor = new BandPowerAccessor(classBands);
    }

    /**
     * pushes a spectral decomposition to the SSVEP
     * @param values
     */
    public synchronized void push(BinnedValues values){
        double total=0;
        double powers[] = mAccessor.getBandPowers(values);
        for (int i = 0; i < mClassMeans.length ; i++) {
            mClassMeans[i] += powers[i];
            total+=mClassMeans[i];
        }
        mEpochPos+=mPushFrequency;
        double epochPercentage = Math.min(1.0,mEpochPos/mEpochLen);
        double classes[] = new double[mClassMeans.length];
        for (int i = 0; i < classes.length ; i++) {
            classes[i]=mClassMeans[i]/total;
        }
        SSVEPListener.Event event = new SSVEPListener.Event(epochPercentage,classes);
        this.mListener.onEvent(event);
        if (epochPercentage == 1.0){
            mClassMeans = new double[mClassMeans.length];
            mEpochPos = 0;
        }
    }








}
