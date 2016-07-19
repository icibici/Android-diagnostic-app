package com.neurotechx.smartphonebci;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.driver.Driver;
import com.neurotechx.smartphonebci.dsp.BinnedValues;
import com.neurotechx.smartphonebci.dsp.BinnedValuesListener;

public class MainActivity extends AppCompatActivity implements BinnedValuesListener{

    //SUPERDIRTY I'M IN A HUGE HURRY!!!
    static Optional<SpectrumPlot> plot= Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
