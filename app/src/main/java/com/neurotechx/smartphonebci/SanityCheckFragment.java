package com.neurotechx.smartphonebci;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.XYPlot;
import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValues;
import com.neurotechx.smartphonebci.plots.SanityCheckPlot;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SanityCheckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SanityCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SanityCheckFragment extends Fragment
{

    Optional<SanityCheckPlot> mPlot = Optional.absent();

    public SanityCheckFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SanityCheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SanityCheckFragment newInstance()
    {
        SanityCheckFragment fragment = new SanityCheckFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.sanity = Optional.of(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sanity_check, container, false);
        this.mPlot = Optional.of(new SanityCheckPlot(view));
        XYPlot plot = (XYPlot) view.findViewById(R.id.sanity_plot);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.getGraphWidget().setPaddingBottom(50);
        plot.getGraphWidget().setPaddingLeft(120);
        plot.getGraphWidget().setPaddingTop(20);
        plot.getGraphWidget().setPaddingRight(20);
        plot.setRangeValueFormat(new DecimalFormat("0.00"));
        plot.setDomainValueFormat(new DecimalFormat("0.00"));
        return view;
    }

    public void push(BinnedValues values)
    {
        if (mPlot.isPresent())
        {
            mPlot.get().push(values);
        }
    }

}
