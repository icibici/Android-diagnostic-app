package com.neurotechx.smartphonebci.dsp;

import com.google.common.base.Preconditions;

/**
 * Created by javi on 17/07/16.
 */
public class Hanning {
    final double[] mWindow;

    public Hanning(int size) {
        mWindow = new double[size];
        for (int i = 0; i < size - 1; i++) {
            mWindow[i]=0.5 * (1-Math.cos(2.*i*Math.PI/(size-1.)));
        }
    }

    /**
     * applies the window in place
     * @param x
     */
    public void apply(double x[]){
        Preconditions.checkArgument(x.length==mWindow.length);
        for (int i = 0; i < mWindow.length; i++) {
            x[i]=mWindow[i]*x[i];
        }
    }


}
