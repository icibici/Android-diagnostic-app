package com.neurotechx.smartphonebci.plots;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.google.common.base.Function;
import com.google.common.math.DoubleMath;
import com.neurotechx.smartphonebci.R;
import com.neurotechx.smartphonebci.driver.dsp.BandPowerAccessor;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValues;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.text.DecimalFormat;


/**
 * Created by javi on 02/08/16.
 */
public class AlphaPlot {

    private XYPlot mPlot;
    private CircularSeries mAlpha;
    private BandPowerAccessor mAccessor = new BandPowerAccessor(
            new BandPowerAccessor.Band[]{new BandPowerAccessor.Band(8, 12)});
    private BandPowerAccessor mLowerThan = new BandPowerAccessor(
            new BandPowerAccessor.Band[]{new BandPowerAccessor.Band(5, 8)});
    private BandPowerAccessor mGreaterThan = new BandPowerAccessor(
            new BandPowerAccessor.Band[]{new BandPowerAccessor.Band(12, 32)});

    public AlphaPlot(View parent) {


        mPlot = (XYPlot) parent.findViewById(R.id.alpha_plot);
        LineAndPointFormatter formatter = new LineAndPointFormatter(
                Color.rgb(118, 216, 227), null, null, null);
        formatter.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter.getLinePaint().setStrokeWidth(8);
        mAlpha = new CircularSeries(60, 0, "Alpha");
        final CircularFifoQueue<Number> buff = new CircularFifoQueue<>(5);
        mAlpha.setMapping(new Function<Number, Number>() {
            @Override
            public Number apply(Number input) {
                buff.add(input);
                if (buff.isAtFullCapacity()){
                       return DoubleMath.mean(buff);
                }else {
                    return input.doubleValue();
                }
            }
        });
        mPlot.getGraphWidget().setPaddingLeft(150);

        mPlot.addSeries(mAlpha, formatter);

        mPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0.00"));
        mPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        mPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        mPlot.getGraphWidget().getRangeGridLinePaint().setColor(Color.TRANSPARENT);
        mPlot.getGraphWidget().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
        mPlot.getGraphWidget().getDomainTickLabelPaint().setColor(Color.TRANSPARENT);
        mPlot.getGraphWidget().getRangeTickLabelPaint().setColor(Color.BLACK);
        mPlot.getLegendWidget().getTextPaint().setColor(Color.BLACK);
        mPlot.setBackgroundColor(Color.TRANSPARENT);

    }


    public void push(BinnedValues values) {
            double alpha = mAccessor.getBandPowers(values)[0];
            double lowerFreqs = mLowerThan.getBandPowers(values)[0];
            double higherFreqs = mGreaterThan.getBandPowers(values)[0];

        mAlpha.getBuffer().add(alpha/(alpha+lowerFreqs+higherFreqs));
        mPlot.redraw();
    }


}
