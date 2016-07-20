package com.neurotechx.smartphonebci.driver.dsp;

/**
 * Created by javi on 17/07/16.
 */
public interface BinnedValuesListener {
    /**
     * Sends the values once the sliding window processes the values
     * @param values
     */
    public void onBinnedValues(BinnedValues values);
}
