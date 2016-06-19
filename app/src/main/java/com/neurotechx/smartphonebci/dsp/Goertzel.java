package com.neurotechx.smartphonebci.dsp;

/**
 * Created by javi on 19/06/16.
 */

/**
 * -This file is copied and modified from
 * http://courses.cs.washington.edu/courses/cse477/projectwebs04sp/cse477m/code/public/Goertzel.java
 *
 *
 * The Goertzel class can be used to perform the Goertzel algorithm.  In
 * order to use this class, four primary steps should be executed:
 * initialize the Goertzel class and all its variables (initGoertzel),
 * process one sample of data at a time (processSample), get the
 * relative magnitude returned by the Goertzle algorithm after N samples
 * have been processed (getMagnitudeSquared, getRealImag), and reset the
 * Goertzel class and all its variables (resetGoertzel).
 * <p/>
 * This class is based on a C program implemented by Kevin Banks of
 * Embedded Systems Programming.
 *
 * @author Chris Palistrant, Tony Offer
 * @version 0.0, May 2004
 */
public class Goertzel {

    private float mSamplingRate;
    private float mTargetFrequency;
    private int mBlockSize;

    private double mCoeff, mQ1, mQ2;
    private double mSine, mCosine;




    /**
     * Constructor
     *
     * @param sampleRate is the sampling rate of the signal to be analyzed
     * @param targetFreq is the frequency that Goertzel will look for.
     */
    public Goertzel(float sampleRate, float targetFreq, int blockSize) {
        mSamplingRate = sampleRate;
        mTargetFrequency = targetFreq;
        mBlockSize = blockSize;

        initGoertzel();
    }

    /**
     * Call this method after every block of N samples has been
     * processed.
     *
     * @return void
     */
    public void resetGoertzel() {
        mQ2 = 0;
        mQ1 = 0;
    }

    /**
     * Call this once, to precompute the constants.
     *
     * @return void
     */
    public void initGoertzel() {
        int k;
        float floatN;
        double omega;

        floatN = (float) mBlockSize;
        k = (int) (0.5 + ((floatN * mTargetFrequency) / mSamplingRate));
        omega = (2.0 * Math.PI * k) / floatN;
        mSine = Math.sin(omega);
        mCosine = Math.cos(omega);
        mCoeff = 2.0 * mCosine;

	/*
    System.out.println("For SAMPLING_RATE = " + mSamplingRate);
	System.out.print(" N = " + n);
	System.out.println(" and FREQUENCY = " + mTargetFrequency);
	System.out.println("k = " + k + " and mCoeff = " + mCoeff + "\n");
	*/

        resetGoertzel();
    }

    /**
     * Call this routine for every sample.
     *
     * @param sample is a double
     * @return void
     */
    public void processSample(double sample) {
        double Q0;
        Q0 = mCoeff * mQ1 - mQ2 + sample;
        mQ2 = mQ1;
        mQ1 = Q0;
    }

    public double[] processSignal(double []samples){
        double[] squaredValues = new double[samples.length];
        int i=0;
        initGoertzel();
        for (double sample:samples){
            processSample(sample);
            squaredValues[i++]=getMagnitudeSquared();
        }
        return squaredValues;
    }




    /**
     * Basic Goertzel.  Call this routine after every block to get the
     * complex result.
     *
     * @param parts has length two where the first item is the real
     *              part and the second item is the complex part.
     * @return double[] stores the values in the param
     */
    public double[] getRealImag(double[] parts) {
        parts[0] = (mQ1 - mQ2 * mCosine);
        parts[1] = (mQ2 * mSine);
        return parts;
    }

    /**
     * Optimized Goertzel.  Call this after every block to get the
     * RELATIVE magnitude squared.
     *
     * @return double is the value of the relative mag squared.
     */
    public double getMagnitudeSquared() {
        return (mQ1 * mQ1 + mQ2 * mQ2 - mQ1 * mQ2 * mCoeff);
    }

}
