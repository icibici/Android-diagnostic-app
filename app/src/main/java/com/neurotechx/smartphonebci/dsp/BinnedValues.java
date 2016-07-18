package com.neurotechx.smartphonebci.dsp;

/**
 * Created by javi on 17/07/16.
 */
public interface BinnedValues {
    /**
    * Returns the array of values
     */
    public double[] getValues();

    /**
     * The resulution of the binned values
     * @return
     */
    public double getResolution();
}
