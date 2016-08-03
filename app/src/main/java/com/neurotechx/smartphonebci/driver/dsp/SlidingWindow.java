package com.neurotechx.smartphonebci.driver.dsp;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * Created by javi on 13/07/16.
 */
public class SlidingWindow {


    final CircularFifoQueue<Double> mQueue;
    int mWindowStepLen;
    int mWindowStep;

    //function to apply once we finished the sliding window
    Function<Double[], Void> mFn;


    public SlidingWindow(int windowLen, int windowStepLen, Function<Double[], Void> fn) {
        mQueue = new CircularFifoQueue<Double>(windowLen);
        this.mWindowStepLen = windowStepLen;
        this.mWindowStep =windowStepLen;
        this.mFn = fn;
    }


    public void push(float data) {
        mQueue.add((double) data);
        mWindowStep++;
        if (!mQueue.isAtFullCapacity()){
            return;
        }

        if (mWindowStep >= mWindowStepLen) {
            mWindowStep = 0;
            //delegate the processing of the data to the passed function
            mFn.apply(Iterables.toArray(mQueue, Double.class));

        }
    }

    public void push(float data[]) {
        for (float d : data) {
            this.push(d);
        }
    }

}
