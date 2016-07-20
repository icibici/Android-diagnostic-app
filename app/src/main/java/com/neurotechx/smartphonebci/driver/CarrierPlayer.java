package com.neurotechx.smartphonebci.driver;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;

/**
 * Created by javi on 18/07/16.
 */
public class CarrierPlayer extends AsyncTask<Void, Void, Void> {
    volatile boolean mPlaying = false;

    final float mCarrierFreq;
    AudioTrack mTrack;

    public CarrierPlayer(float mCarrierFreq) {
        this.mCarrierFreq = mCarrierFreq;
    }

    public void stop() {
        mPlaying = false;
    }

    public void start() {
        mPlaying = true;
    }

    @Override
    protected Void doInBackground(Void... params) {

        short[] buffer = new short[1024];

        mTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, CHANNEL_OUT_MONO, ENCODING_PCM_16BIT, buffer.length, AudioTrack.MODE_STREAM);
        float increment = (float) (2 * Math.PI) * mCarrierFreq / 44100; // angular increment for each sample
        float angle = 0;


        mTrack.play();

        while (true) {
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (short) (Math.sin(angle) * Short.MAX_VALUE);
                angle += increment;
            }
            //avoid overflows
            angle = angle % (int)(44100/mCarrierFreq);

            if (this.mPlaying) {
                mTrack.write(buffer, 0, buffer.length);  //write to the audio buffer.... and start all over again!
            }
        }
    }
}

