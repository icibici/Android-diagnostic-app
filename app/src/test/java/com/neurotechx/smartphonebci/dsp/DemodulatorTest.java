package com.neurotechx.smartphonebci.dsp;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by javi on 17/07/16.
 */
public class DemodulatorTest {

    class Holder{
        BinnedValues binned;
    }
    @Test
    public void checkDemodulationSanity(){
        double originalFs = 1024;
        double carrierFreq = 128;
        int len = (int)originalFs;
        double carrier[] = new double[len+1];
        //generate carrier signal
        for (int i=0; i<len+1; i++){
            carrier[i]= Math.sin(i/originalFs*2*Math.PI*carrierFreq);
        }




        //double signal[] = new double[len];
        double infoFreq = 10.;
        //info signal is a sinusoid at 10hz we multiply both signals to modulate them
        for (int i=0; i<len+1; i++){
            carrier[i] *= Math.sin(i /originalFs *2*Math.PI*infoFreq);
        }
        short signal[] = new short[carrier.length];
        for (int i = 0; i < signal.length; i++) {
            signal[i]= (short) (255 * carrier[i]);
        }


        final Holder binned = new Holder();
        BinnedValuesListener listener = new BinnedValuesListener() {
            @Override
            public void onBinnedValues(BinnedValues values) {
                binned.binned = values;
            }
        };
        double modulatedMax = 20;
        SlidingWindow window = new DemodulatorBuilder().setmWindowLen(len)
                .setmWindowStepLen(10)
                .setmCarrierFrequency(carrierFreq)
                .setmMinCarriedFrequency(0)
                .setmMaxCarriedFrequency(modulatedMax)
                .setmSamplingFrequency(1024)
                .setmCallback(listener)
                .build();
        //the values are eft in the holder
        window.push(signal);
        Assert.assertEquals("The resolution is 1", 1., binned.binned.getResolution(),0.001);
        //what we get is the spectrum of the demulated signal
        Assert.assertEquals("The size of the spectra makes sense", (int) binned.binned.getResolution()*20,binned.binned.getValues().length);
        int maxFreq = Util.argmax(binned.binned.getValues());
        Assert.assertEquals("The inforation frequency is the maximum frequency after the demodulation", maxFreq*binned.binned.getResolution(),infoFreq,0.001);
    }
}