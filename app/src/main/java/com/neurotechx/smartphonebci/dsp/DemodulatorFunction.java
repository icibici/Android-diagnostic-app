package com.neurotechx.smartphonebci.dsp;

import com.google.common.base.Function;

/**
 * Demodulates the spectral components from a carrier spectral analysis
 */
public class DemodulatorFunction implements Function<BinnedValues,BinnedValues> {

    /**
     *
     * @param carrierFrequency frequency of the carrier signal
     * @param minCarriedFrequency lower frequency to extract (i.e. 0Hz)
     * @param maxCarriedFrequency highest frequency to extracct (i.e. 32Hz)
     */
    public DemodulatorFunction(double carrierFrequency, double minCarriedFrequency, double maxCarriedFrequency) {
        this.mCarrierFrequency = carrierFrequency;
        this.mMaxCarriedFrequency = maxCarriedFrequency;
        this.mMinCarriedFrequency = minCarriedFrequency;
    }

    double mCarrierFrequency;
    double mMaxCarriedFrequency;
    double mMinCarriedFrequency;

    @Override
    public BinnedValues apply(BinnedValues input) {
        //position of the carrier frequency
        double carrierPos = input.getResolution() * this.mCarrierFrequency;

        int startPos = (int) (carrierPos + input.getResolution() * this.mCarrierFrequency * mMinCarriedFrequency);
        int len = (int)((mMaxCarriedFrequency - mMinCarriedFrequency)/input.getResolution());

        double values[]= new double[(int)len];
        //Just copy the vaues of the array that correspond to those frequencies
        System.arraycopy(input.getValues(),(int) startPos,values,0,len);
        return new DemoldulatedBinnedValues(values, input.getResolution());
    }

    static class DemoldulatedBinnedValues extends AbstractBinnedValues{
        DemoldulatedBinnedValues(double[] values, double resolution){
            mResolution=resolution;
            mValues = values;
        }

    }
}
