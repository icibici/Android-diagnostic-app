package com.neurotechx.smartphonebci.dsp;

/**
 * Created by javi on 17/07/16.
 */
public abstract class AbstractBinnedValues implements BinnedValues{
    double mValues[]=new double[0];
    double mResolution=0;


    @Override
    public double[] getValues() {
        return mValues;
    }

    @Override
    public double getResolution() {
        return mResolution;
    }
}
