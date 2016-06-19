package com.neurotechx.smartphonebci.dsp;


import com.google.common.math.DoubleMath;

/**
 * implementation of a two class ssvep BCI
 */
public class SSVEP {
    /**
     * Calback to inform the interface about what class has been selected
     * the index corresponds to the index of the stimiliFreqs passed
     */
    public interface SelectionListener {
        public void onSelection(int stimuli);
    }
    private final SelectionListener mListener;


    double mSamplingFreq;
    double [] mEpoch;
    double[] mStimuliFreqs;
    int mIndex =0;

    /**
     * Creates a SSVEP processor
     * @param samplingFreq the sampling frequency of the signal
     * @param epochLen the size of the epoch to analyse (around 3 secs?) in seconds
     * @param stimuliFreqs frequencies of the stimuli
     * @param listener callback to inform the stimuli that the subject is focused on
     */
    public SSVEP(double samplingFreq, double epochLen, double[] stimuliFreqs, SelectionListener listener) {
        this.mSamplingFreq = samplingFreq;
        this.mStimuliFreqs = stimuliFreqs;
        this.mListener = listener;
        this.mEpoch = new double[(int)(epochLen*samplingFreq)];
    }

    /**
     * Pushes a reading from the input signal
     * @param reading
     */
    public void push(double reading){
        //TODO: allow sending several readings at time
        mEpoch[mIndex++]=reading;
        if (mIndex ==mEpoch.length){
            getSelection();
            mIndex=0;
        }
    }

    private void getSelection(){
        //results of the powers
        double powers[] = new double[mStimuliFreqs.length];
        double max= Double.MIN_VALUE;
        int selection = -1;
        for (int i=0; i<mStimuliFreqs.length;i++){
            double power = computeGoertzel(mStimuliFreqs[i]);
            if( power > max){
                max = power;
                selection =i;
            }
        }
        this.mListener.onSelection(selection);
    }

    /*
    * Computes the power at the given freq and one harmonic
     */
    private double computeGoertzel(double freq){
        Goertzel filterFreq = new Goertzel(mSamplingFreq,freq,mEpoch.length);
        Goertzel filterFreqHarmonic = new Goertzel(mSamplingFreq,freq*2,mEpoch.length);
        double power = DoubleMath.mean(filterFreq.processSignal(mEpoch));
        double powerHarmonic = DoubleMath.mean(filterFreqHarmonic.processSignal(mEpoch));
        return (power+powerHarmonic);


    }






}
