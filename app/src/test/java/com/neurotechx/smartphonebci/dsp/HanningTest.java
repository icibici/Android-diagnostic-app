package com.neurotechx.smartphonebci.dsp;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by javi on 17/07/16.
 */
public class HanningTest {

    @Test
    public void checkSanity() {
        //values of the hanning window according to octave
        double hanning10[] = new double[]{0.00000,
                0.11698,
                0.41318,
                0.75000,
                0.96985,
                0.96985,
                0.75000,
                0.41318,
                0.11698,
                0.00000,
        };

        Hanning win = new Hanning(10);
        Assert.assertArrayEquals("Check hanning sanity",hanning10,win.mWindow,0.00001);
    }
}