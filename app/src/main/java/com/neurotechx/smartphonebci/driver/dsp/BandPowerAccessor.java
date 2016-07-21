package com.neurotechx.smartphonebci.driver.dsp;

/**
 * Created by javi on 20/07/16.
 */
public class BandPowerAccessor {



    public static class Band{
        double mHigh;
        double mLow;

        double mBegin=0;
        double mEnd=0;
        double mWeights[] = new double[0];

        public Band(double low, double high) {
            this.mHigh = high;
            this.mLow = low;
        }

        void intWeights(double resolution){
            //Computes the contribution of each bin to the final
            //power
            mBegin = mLow / resolution;
            mEnd = mHigh / resolution;

            //number of bins taking part in this band
            int len = (int) (Math.ceil(mEnd)-Math.ceil(mBegin));
            mWeights = new double[len];
            for (int i = 0; i < mWeights.length; i++) {
                mWeights[i]=1;
            }
            mWeights[0] -= (mBegin - Math.floor(mBegin));
            mWeights[mWeights.length-1] -= (Math.ceil(mEnd)-mEnd);
        }

        public double getPower(double[] bins){
            double power = 0;
            int begin = (int) Math.ceil(mBegin);
            for (int i = 0; i < mWeights.length; i++) {
                power += bins[i+begin]*mWeights[i];
            }
            power/=mWeights.length;
            return power;
        }


    }


    final Band[] mBands;
    boolean mInit=false;
    public BandPowerAccessor(Band bands[]) {
        mBands=bands;
    }


    public double[] getBandPowers(BinnedValues values){
        if(!mInit){
            for(Band b:mBands){
                b.intWeights(values.getResolution());
            }
            mInit = true;
        }
        double[] powers = new double[mBands.length];
        for (int i = 0; i < powers.length; i++) {
            powers[i] = mBands[i].getPower(values.getValues());
        }
        return powers;
    }



}
