package com.neurotechx.smartphonebci;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
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

    SSVEP ssvep;
    TextView []classBoxes = new TextView[2];
    SeekBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        ssvep = new SSVEP(3.0, 0.25, new BandPowerAccessor.Band[]{
            new BandPowerAccessor.Band(5,7),
            new BandPowerAccessor.Band(9.5,10.5)},this);

        final Optional<Driver> driver = Driver.getInstance(new Driver.Configuration(this));
        if(!driver.isPresent()){
            throw new IllegalStateException("No driver!!");
        }

        final Button carrierButton = (Button) findViewById(R.id.carrier_button);
        carrierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrierButton.getText().equals("Play")) {
                    driver.get().start();
                    ((Button) carrierButton).setText("Stop");
                } else {
                    driver.get().stop();
                    carrierButton.setText("Play");
                }

            }
        });
        classBoxes[0] = (TextView) findViewById(R.id.class_view);
        classBoxes[1] = (TextView) findViewById(R.id.class_view1);
        bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setIndeterminate(false);
        bar.setMax(100);
    }


    @Override
    public void onBinnedValues(BinnedValues values) {
        if(plot.isPresent()){
            plot.get().push(values);
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
}
