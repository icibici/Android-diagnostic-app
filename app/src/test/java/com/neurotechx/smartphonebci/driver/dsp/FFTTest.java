package com.neurotechx.smartphonebci.driver.dsp;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by javi on 17/07/16.
 */
public class FFTTest {

    @Test
    public void testApply() throws Exception {
        double fs = 128;
        int len = 128*2;
        Double signal[] = new Double[len];
        //A signal of 2 seconds sampled at 128 with the main component at 12 hertz
        for (int i=0; i<len; i++){
            signal[i]= Math.sin(i/((double)128)*2*Math.PI*12.);
        }

        FFT fft = new FFT(len,fs);
        BinnedValues values = fft.apply(signal);
        /*
            N (Bins) = FFT Size/2

            FR = Fmax/N(Bins)
            => 128*.5 / 128 = 0.5
         */
        Assert.assertEquals("The resolution makes sense", 0.5, values.getResolution(),0.00001);
        int argMax = Util.argmax(values.getValues());
        Assert.assertEquals("The maximum lays at 12Hz *  ",argMax* values.getResolution(), 12, 0.01);


    }
}