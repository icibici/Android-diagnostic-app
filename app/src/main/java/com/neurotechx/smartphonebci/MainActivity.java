package com.neurotechx.smartphonebci;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    }


    @Override
    public void onBinnedValues(BinnedValues values) {
        if(plot.isPresent()){
            plot.get().push(values);
        }
        ssvep.push(values);
    }

    @Override
    public void onEvent(Event e) {
        Log.d("SSVEP", e.getSelectedClass()+"");
    }
}
