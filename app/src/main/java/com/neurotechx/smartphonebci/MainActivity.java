package com.neurotechx.smartphonebci;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;

public class MainActivity extends AppCompatActivity {

    public float carrier_freq = 1000f;
    private AudioTrack track;


    class Player extends AsyncTask<Void, Void, Void> {
        volatile boolean mPlaying = false;

        public void stop() {
            mPlaying = false;
        }

        public void start() {
            mPlaying = true;
        }

        @Override
        protected Void doInBackground(Void... params) {

            short[] buffer = new short[1024];

            track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, CHANNEL_OUT_MONO, ENCODING_PCM_16BIT, buffer.length, AudioTrack.MODE_STREAM);
            float increment = (float) (2 * Math.PI) * carrier_freq / 44100; // angular increment for each sample
            float angle = 0;


            track.play();

            while (true) {
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (short) (Math.sin(angle) * Short.MAX_VALUE);
                    angle += increment;
                }
                if (this.mPlaying) {
                    track.write(buffer, 0, buffer.length);  //write to the audio buffer.... and start all over again!
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Player player = new Player();
        player.execute();


        final Button carrierButton = (Button) findViewById(R.id.carrier_button);
        carrierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrierButton.getText().equals("Play")) {
                    player.start();
                    ((Button) carrierButton).setText("Stop");
                } else {
                    player.stop();
                    carrierButton.setText("Play");
                }

            }
        });


    }


}
