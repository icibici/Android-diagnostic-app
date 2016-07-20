package com.neurotechx.smartphonebci.driver.dsp;

/**
 * Created by javi on 19/06/16.
 */
public class SSVEPTest {

//    class TestListener implements SSVEP.SelectionListener{
//        int mSelected;
//        @Override
//        public void onSelection(int stimuli) {
//            mSelected=stimuli;
//        }
//    }
//    @Test
//    public void testPush() throws Exception {
//
//        int len = 128*3;
//        double signalStimulus1[] = new double[len+1];
//        double signalStimulus2[] = new double[len+1];
//        //A signal of 3 seconds sampled at 128 with the main component at 12 hertz
//        for (int i=0; i<len+1; i++){
//            signalStimulus1[i]= Math.sin(i/((double)128)*2*Math.PI*12.);
//        }
//
//        for (int i=0; i<len+1; i++){
//            signalStimulus2[i]= Math.sin(i/((double)128)*2*Math.PI*7.5);
//        }
//
//
//        TestListener listener = new TestListener();
//        SSVEP bci = new SSVEP(128,3,new double[]{7.5,12},listener);
//        //we push 3 seconds of 12 hertz so it fires stimulus 1
//        for (double sample:signalStimulus1){
//            bci.push(sample);
//        }
//        Assert.assertEquals("the selected class corresponds to 12 hertz", listener.mSelected,1);
//        //we push 3 seconds of 7.5 hertz so it fires stimulus 0
//        for (double sample:signalStimulus2){
//            bci.push(sample);
//        }
//        Assert.assertEquals("the selected class corresponds to 7 hertz", listener.mSelected,0);
//
//
//
//    }
}