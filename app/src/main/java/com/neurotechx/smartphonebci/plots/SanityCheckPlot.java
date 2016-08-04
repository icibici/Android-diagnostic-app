package com.neurotechx.smartphonebci.plots;

import android.graphics.Color;
import android.view.View;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.R;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValues;

/**
 * Created by javi on 03/08/16.
 */
public class SanityCheckPlot {
    private XYPlot mPlot;
    private Spectrum spectrum = new Spectrum("Sanity check");

    public SanityCheckPlot(View view) {

        // initialize our XYPlot reference:
        mPlot = (XYPlot) view.findViewById(R.id.sanity_plot);

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.GREEN, null, null, null);

        //series1//Format.setPointLabelFormatter(new PointLabelFormatter());

        // series1Format.setInterpolationParams(
        //        new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));


        // add a new series' to the xyplot:
        mPlot.addSeries(spectrum, series1Format);


        // reduce the number of range labels
        mPlot.setTicksPerRangeLabel(3);

        // rotate domain labels 45 degrees to make them more compact horizontally:
        mPlot.getGraphWidget().setDomainLabelOrientation(-45);
        mPlot.getGraphWidget().setPaddingBottom(50);
        mPlot.getGraphWidget().setPaddingLeft(150);
    }


    class Spectrum implements XYSeries {
        private Optional<BinnedValues> values = Optional.absent();

        private String title;

        private double epochMax = Double.MIN_VALUE;

        public Spectrum(String title) {
            this.title = title;
        }


        public void setValues(BinnedValues binnedValues) {
            values = Optional.of(binnedValues);
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public int size() {

            if (values.isPresent()) {
                return values.get().getValues().length;
            }
            return 0;
        }

        @Override
        public Number getX(int index) {
            if (values.isPresent()) {
                return values.get().getResolution() * index;

            }
            return 0;
        }

        @Override
        public Number getY(int index) {

            if (values.isPresent()) {
                double y = values.get().getValues()[index];
                return y;
            }
            return 0;
        }
    }

    public void push(BinnedValues values) {
        spectrum.setValues(values);
        mPlot.redraw();
    }
}
