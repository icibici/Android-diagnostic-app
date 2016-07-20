package com.neurotechx.smartphonebci.driver;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.neurotechx.smartphonebci.driver.dsp.SlidingWindow;

/**
 * Created by javi on 18/07/16.
 */
public class AudioReader extends Thread {

    SlidingWindow sink;
    volatile boolean isProcessing = false;
    volatile boolean isRunning = false;

    public AudioReader(SlidingWindow sink) {
        this.sink = sink;
    }

    public void halt() {
        this.isRunning = false;
    }
    public void setProcessing(boolean processing) {
        this.isProcessing = processing;
    }



    @Override
    public void run() {


        int bufferReadResult;
        AudioRecord audioRecord;


        int bufferLen = 2048;//AudioRecord.getMinBufferSize(samplingRate,
        //       CHANNEL_OUT_MONO, ENCODING_PCM_16BIT);
        short[] audioData = new short[bufferLen];
            /* set audio recorder parameters, and start recording */
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, Driver.SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferLen);
        audioRecord.startRecording();

      //  isProcessing = true;
        isRunning = true;

        int read = 0;
        while (this.isRunning) {
            if(this.isProcessing) {
                read = audioRecord.read(audioData, 0, audioData.length);
                sink.push(audioData);
            }
        }


    }
}

