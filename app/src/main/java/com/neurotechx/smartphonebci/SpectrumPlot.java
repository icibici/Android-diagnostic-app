package com.neurotechx.smartphonebci;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.dsp.BinnedValues;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SpectrumPlot#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpectrumPlot extends Fragment {

    private XYPlot plot;
    private Spectrum spectrum = new Spectrum("Spectrum");

    public SpectrumPlot() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SpectrumPlot.
     */
    // TODO: Rename and change types and number of parameters
    public static SpectrumPlot newInstance() {
        SpectrumPlot fragment = new SpectrumPlot();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        //TODO omg I'm in a hury!

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.plot=Optional.of(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_spectrum_plot, container, false);

        // initialize our XYPlot reference:
        plot = (XYPlot) view.findViewById(R.id.plot);



        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        //series1//Format.setPointLabelFormatter(new PointLabelFormatter());

       // series1Format.setInterpolationParams(
        //        new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));



        // add a new series' to the xyplot:
        plot.addSeries(spectrum , series1Format);


        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);

        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.getGraphWidget().setPaddingBottom(30);


        // Inflate the layout for this fragment
        return view;
    }

    public void push(BinnedValues values){
        spectrum.setValues(values);
        plot.redraw();
    }


    class Spectrum implements XYSeries {
        private Optional<BinnedValues> values = Optional.absent();

        private String title;


        public Spectrum(String title) {
            ;
            this.title = title;
        }


        public void setValues(BinnedValues binnedValues){
            values = Optional.of(binnedValues);
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public int size() {

            if(values.isPresent()){
                return values.get().getValues().length;
            }
            return 0;
        }

        @Override
        public Number getX(int index) {
            if (values.isPresent()) {
                return values.get().getResolution()*index;

            }
            return 0;
        }

        @Override
        public Number getY(int index) {
            if (values.isPresent()) {
                return values.get().getValues()[index   ];

            }
            return 0;
        }
    }


}
