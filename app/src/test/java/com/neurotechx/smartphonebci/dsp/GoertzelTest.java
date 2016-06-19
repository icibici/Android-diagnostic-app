package com.neurotechx.smartphonebci.dsp;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by javi on 19/06/16.
 */
public class GoertzelTest {

    @Test
    public void testProcessSignal() throws Exception {
        int len = 128*3;
        double signal[] = new double[len];
        //A signal of 3 seconds sampled at 128 with the main component at 12 hertz
        for (int i=0; i<len; i++){
            signal[i]= Math.sin(i/((double)128)*2*Math.PI*12.);
        }

        Goertzel filter12 = new Goertzel(128,12,len);
        double powers[] = filter12.processSignal(signal);
        double power12 = 0;
        for(double val : powers){
            power12+=val;
        }
        power12/=powers.length;

        Goertzel filter20 = new Goertzel(128,20,len);
        powers = filter20.processSignal(signal);
        double power20 = 0;
        for(double val : powers){
            power20+=val;
        }
        power20/=powers.length;
        Assert.assertTrue("The sum of the power for 12 hertz should be greater than 20Hz ", power12>power20);


    }
}