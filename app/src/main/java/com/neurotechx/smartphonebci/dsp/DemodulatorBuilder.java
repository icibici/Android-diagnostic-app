package com.neurotechx.smartphonebci.dsp;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class DemodulatorBuilder {
    private int mWindowLen;
    private int mWindowStepLen;
    private double mSamplingFrequency;
    private double mCarrierFrequency;
    private double mMinCarriedFrequency;
    private double mMaxCarriedFrequency;
    private Function<BinnedValues, Void> mCallback;

    public DemodulatorBuilder setmWindowLen(int mWindowLen) {
        this.mWindowLen = mWindowLen;
        return this;
    }

    public DemodulatorBuilder setmWindowStepLen(int mWindowStepLen) {
        this.mWindowStepLen = mWindowStepLen;
        return this;
    }

    public DemodulatorBuilder setmSamplingFrequency(double mSamplingFrequency) {
        this.mSamplingFrequency = mSamplingFrequency;
        return this;
    }

    public DemodulatorBuilder setmCarrierFrequency(double mCarrierFrequency) {
        this.mCarrierFrequency = mCarrierFrequency;
        return this;
    }

    public DemodulatorBuilder setmMinCarriedFrequency(double mMinCarriedFrequency) {
        this.mMinCarriedFrequency = mMinCarriedFrequency;
        return this;
    }

    public DemodulatorBuilder setmMaxCarriedFrequency(double mMaxCarriedFrequency) {
        this.mMaxCarriedFrequency = mMaxCarriedFrequency;
        return this;
    }
    public DemodulatorBuilder setmCallback(final BinnedValuesListener listener){
        this.mCallback = new Function<BinnedValues, Void>() {
            @Override
            public Void apply(BinnedValues input) {
                listener.onBinnedValues(input);
                return null;
            }

        };
        return this;
    }

    public SlidingWindow build() {
        FFT fft = new FFT(mWindowLen, mSamplingFrequency);
        DemodulatorFunction demodulatorFunction = new DemodulatorFunction(mCarrierFrequency, mMinCarriedFrequency, mMaxCarriedFrequency);
        Function<Double[],BinnedValues> demodulation = Functions.compose(demodulatorFunction,fft);
        Function<Double[],Void> fn = Functions.compose(mCallback,demodulation);
        return new SlidingWindow(mWindowLen, mWindowStepLen, fn );
    }
}