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
    double[] mClassAccum;
    BandPowerAccessor mAccessor;

    /**
     *
     */
    public SSVEP(double epochLen, double pushFrequency, BandPowerAccessor.Band classBands[],SSVEPListener listener) {
        mEpochLen = epochLen;
        mPushFrequency = pushFrequency;
        mClassBands = classBands;
        mClassAccum = new double[mClassBands.length];
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
        for (int i = 0; i < mClassAccum.length ; i++) {
            mClassAccum[i] += powers[i];
            total+= mClassAccum[i];
        }
        mEpochPos+=mPushFrequency;
        double epochPercentage = Math.min(1.0,mEpochPos/mEpochLen);
        double classes[] = new double[mClassAccum.length];
        for (int i = 0; i < classes.length ; i++) {
            classes[i]= mClassAccum[i]/total;
        }
        SSVEPListener.Event event = new SSVEPListener.Event(epochPercentage,classes);
        this.mListener.onEvent(event);
        if (epochPercentage == 1.0){
            mClassAccum = new double[mClassAccum.length];
            mEpochPos = 0;
        }
    }








}
