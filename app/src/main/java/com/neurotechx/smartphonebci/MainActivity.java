package com.neurotechx.smartphonebci;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.driver.Driver;
import com.neurotechx.smartphonebci.driver.dsp.BandPowerAccessor;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValues;
import com.neurotechx.smartphonebci.driver.dsp.BinnedValuesListener;
import com.neurotechx.smartphonebci.ssvep.SSVEP;
import com.neurotechx.smartphonebci.ssvep.SSVEPListener;

public class MainActivity extends AppCompatActivity implements BinnedValuesListener, SSVEPListener{

    //SUPERDIRTY I'M IN A HUGE HURRY!!!
    static Optional<SpectrumPlot> plot = Optional.absent();
    static Optional<AlphaFragment> alpha = Optional.absent();

    SSVEP ssvep;

    TextView []classBoxes = new TextView[2];
    SeekBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);


        ssvep = new SSVEP(10.0, 0.25, new BandPowerAccessor.Band[]{
                new BandPowerAccessor.Band(11.0,13.0),
            new BandPowerAccessor.Band(19.0,21.0)
            },this);



        final Button carrierButton = (Button) findViewById(R.id.carrier_button);
        carrierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrierButton.getText().equals("Play")) {
                    Driver.getInstance(new Driver.Configuration(MainActivity.this)).get().start();
                    ((Button) carrierButton).setText("Stop");
                } else {
                    Driver.getInstance().get().stop();
                    carrierButton.setText("Play");
                }

            }
        });
        classBoxes[0] = (TextView) findViewById(R.id.class_view);
        classBoxes[1] = (TextView) findViewById(R.id.class_view1);
        bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setIndeterminate(false);
        bar.setMax(100);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(adapter);
    }


    @Override
    public void onBinnedValues(BinnedValues values) {
        if(plot.isPresent()){
            plot.get().push(values);
        }
        if(alpha.isPresent()){
            alpha.get().push(values);
        }
        ssvep.push(values);
    }
    static String FORMAT = "%.2f";
    @Override
    public void onEvent(final Event e) {

        runOnUiThread(new Runnable(){

            @Override
            public void run() {

                double progress = e.getEpochProgress();
                classBoxes[0].setText(String.format(FORMAT,e.getClassDistribution()[0]));
                classBoxes[1].setText(String.format(FORMAT,e.getClassDistribution()[1]));
                int ior=(int)(e.getEpochProgress()*100);
                bar.setProgress((int)(e.getEpochProgress()*100));
                //end of epoch mark the final selection
                if (e.getEpochProgress()==1.){
                    int clazz = e.getSelectedClass();
                    classBoxes[0].setBackgroundColor(Color.TRANSPARENT);

                    classBoxes[1].setBackgroundColor(Color.TRANSPARENT);
                    classBoxes[clazz].setBackgroundColor(Color.GREEN);
                }
            }
        });
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return SpectrumPlot.newInstance();
                case 1:
                    return AlphaFragment.newInstance();

            }
            throw new IllegalStateException("We should't be here! "+position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Spectrum";
                case 1:
                    return "Alpha power";

            }
            return null;
        }
    }
}
