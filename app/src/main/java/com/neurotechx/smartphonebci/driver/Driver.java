package com.neurotechx.smartphonebci.driver;

import com.google.common.base.Optional;
import com.neurotechx.smartphonebci.dsp.BinnedValuesListener;
import com.neurotechx.smartphonebci.dsp.DemodulatorBuilder;
import com.neurotechx.smartphonebci.dsp.SlidingWindow;

/**
 * Created by javi on 18/07/16.
 */
public class Driver {


    public static final int SAMPLING_RATE = 44100;

    public static class Configuration{

        //FFT window len
        int mWindowLen = 65536; // 0.67 frequency resolution

        //FFT step size
        int mWindowStepLen = (int) (SAMPLING_RATE * 0.25);
        //carrier freq default 1KHz
        double mCarrierFrequency = 1000.;
        //lowest frquency to get from the carried signal
        double mCarriedMin = 0.;
        //highest frquency to get from the carried signal
        double mCarriedMax = 32.;

        final BinnedValuesListener mListener;

        public Configuration(BinnedValuesListener listener) {
            mListener=listener;
        }


        public Configuration withWindowLen(int samples) {
            this.mWindowLen = samples;
            return this;

        }

        public Configuration withWindowStepLen(int samples) {
            this.mWindowStepLen = samples;
            return this;

        }


        public Configuration withCarrierFrequency(double freq) {
            this.mCarrierFrequency = freq;
            return this;

        }

        public Configuration withMaximumCarriedFrequecy(double maxFreq) {
            this.mCarriedMax = maxFreq;
            return this;
        }

        public Configuration withMinimumCarriedFrequecy(double minFreq) {
            this.mCarriedMin = minFreq;
            return this;
        }

         SlidingWindow build() {
            return new DemodulatorBuilder().setmWindowLen(mWindowLen)
                     .setmWindowStepLen(mWindowStepLen)
                     .setmCarrierFrequency(mCarrierFrequency)
                     .setmMinCarriedFrequency(mCarriedMin)
                     .setmMaxCarriedFrequency(mCarriedMax)
                     .setmSamplingFrequency(SAMPLING_RATE)
                     .setmCallback(mListener)
                     .build();
            }
        }



    static Driver INSTANCE;
    CarrierPlayer player;
    AudioReader audioReader;
    SlidingWindow demodulator;


    Driver(Configuration conf){
        player = new CarrierPlayer((float)conf.mCarrierFrequency);
        player.stop();
        player.execute();

        demodulator = conf.build();
        audioReader = new AudioReader(demodulator);
        //this is horrible, FIXME
        audioReader.setProcessing(false);
        audioReader.start();

    }

    public static Optional<Driver> getInstance(Configuration conf){
        if (INSTANCE == null) {
            INSTANCE = new Driver(conf);
        }
        return Optional.fromNullable(INSTANCE);
    }
    public static Optional<Driver> getInstance(){
        return Optional.fromNullable(INSTANCE);
    }

    public void start(){
        player.start();
        audioReader.setProcessing(true);
    }

    public void stop(){
        audioReader.setProcessing(false);
        player.stop();

    }

}
