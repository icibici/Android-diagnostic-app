package com.neurotechx.smartphonebci.ssvep;

import com.neurotechx.smartphonebci.driver.dsp.Util;

/**
 * Created by javi on 20/07/16.
 */
public interface SSVEPListener {

    public static class Event{
        double mEpochProgress;
        double mClassDistribution[];

        public Event(double epochProgress, double[] classDistribution) {
            this.mEpochProgress = epochProgress;
            this.mClassDistribution = classDistribution;
        }

        /**
         * Range from 0 to 1 to know the position within an epoch
         * it's 1 when the epoch finishes
         * at that moment the outcome should be the value returned by
         * getClassSelection
         * @return
         */
        public double getEpochProgress() {
            return mEpochProgress;
        }

        public double[] getClassDistribution() {
            return mClassDistribution;
        }
        //gets he selected class
        public int getSelectedClass(){
            return Util.argmax(mClassDistribution);//argmax
        }
    }

    public void onEvent(Event e);
}
