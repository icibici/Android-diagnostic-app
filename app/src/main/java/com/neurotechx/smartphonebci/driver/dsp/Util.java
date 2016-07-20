package com.neurotechx.smartphonebci.driver.dsp;

/**
 * Created by javi on 18/07/16.
 */
public class Util {
    public static int argmax(double[] values){
        double max = Double.MIN_VALUE;
        int argMax = -1;

        for (int i = 0; i < values.length; i++) {
            if (max < values[i]){
                max = values[i];
                argMax = i;
            }
        }
        return argMax;
    }
}
