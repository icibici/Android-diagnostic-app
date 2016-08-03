package com.neurotechx.smartphonebci;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValues;
import com.neurotechx.smartphonebci.plots.AlphaPlot;



public class AlphaFragment extends Fragment {

    Optional<AlphaPlot> mPlot= Optional.absent();
    public AlphaFragment() {
        // Required empty public constructor
    }


    public static AlphaFragment newInstance() {
        AlphaFragment fragment = new AlphaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.alpha=Optional.of(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alpha, container, false);
        this.mPlot = Optional.of(new AlphaPlot(view));
        return view;
    }

    public void push(BinnedValues values){
        if (mPlot.isPresent()){
            mPlot.get().push(values);
        }
    }

}
