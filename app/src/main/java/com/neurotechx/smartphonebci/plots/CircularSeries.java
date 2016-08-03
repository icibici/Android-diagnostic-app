package com.neurotechx.smartphonebci.plots;

import com.androidplot.xy.XYSeries;
import com.google.common.base.Function;

import org.apache.commons.collections4.queue.CircularFifoQueue;

class CircularSeries implements XYSeries {

    public synchronized CircularFifoQueue<Double> getBuffer() {
        return buffer;
    }

    private CircularFifoQueue<Double> buffer;
    private int channel;
    private String title;
    private Function<Number,Number> mMap = new Function<Number, Number>(){

        @Override
        public Number apply(Number input) {
            return input;
        }
    };


    public CircularSeries(int size, int channel, String title) {
        this.buffer = new CircularFifoQueue<Double>(size);
        this.channel = channel;
        this.title = title;
    }


    public void setMapping(Function<Number,Number> fn){
        mMap = fn;
    }
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public synchronized int size() {
        return this.buffer.size();
    }

    @Override
    public synchronized Number getX(int index) {
        return index;
    }

    @Override
    public synchronized Number getY(int index) {
        return mMap.apply(this.buffer.get(index));
    }

}