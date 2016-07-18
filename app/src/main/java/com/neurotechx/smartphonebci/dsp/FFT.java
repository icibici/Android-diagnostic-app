package com.neurotechx.smartphonebci.dsp;

import com.google.common.base.Function;
import com.google.common.primitives.Doubles;

import java.util.Arrays;

/**
 * Based on http://stackoverflow.com/questions/9272232/fft-library-in-android-sdk
 * the code is horrible written but it works...
 * */
public class FFT implements Function<Double[], BinnedValues>{

    int n, m;

    // Lookup tables. Only need to recompute when size of FFT changes.
    double[] cos;
    double[] sin;

    Hanning window;
    double samplingFrequency;
    public FFT(int nFFT,double samplingFrequency) {
        this.n = nFFT;
        this.m = (int) (Math.log(n) / Math.log(2));
        window = new Hanning(n);
        this.samplingFrequency = samplingFrequency;
        // Make sure n is a power of 2
        if (n != (1 << m))
            throw new RuntimeException("FFT length must be power of 2");

        // precompute tables
        cos = new double[n / 2];
        sin = new double[n / 2];

        for (int i = 0; i < n / 2; i++) {
            cos[i] = Math.cos(-2 * Math.PI * i / n);
            sin[i] = Math.sin(-2 * Math.PI * i / n);
        }

    }


    public FFTValues apply(Double[] signal){
        //too much java boilerplate? it might affect performance...
        double[] x = Doubles.toArray(Arrays.asList(signal));
        double[] y = new double[x.length];
        window.apply(x);
        this.fft(x,y);
        for (int i=0; i<x.length;i++){
            //real part of the squared complex num
            //and add the negative frequencies
            //keep in mind that everything after x[lenght/2] is garbage
            x[i]= Math.abs(Math.pow(x[i],2)-Math.pow(y[i],2));
        }
        //make it one side spectra by adding negative freqs
        for (int i = 1; i < x.length -1; i++){
            x[i] += x[x.length -i];
        }
        return new FFTValues(x,samplingFrequency,n);
    }
    private void fft(double[] x, double[] y) {
        int i, j, k, n1, n2, a;
        double c, s, t1, t2;

        // Bit-reverse
        j = 0;
        n2 = n / 2;
        for (i = 1; i < n - 1; i++) {
            n1 = n2;
            while (j >= n1) {
                j = j - n1;
                n1 = n1 / 2;
            }
            j = j + n1;

            if (i < j) {
                t1 = x[i];
                x[i] = x[j];
                x[j] = t1;
                t1 = y[i];
                y[i] = y[j];
                y[j] = t1;
            }
        }

        // FFT
        n1 = 0;
        n2 = 1;

        for (i = 0; i < m; i++) {
            n1 = n2;
            n2 = n2 + n2;
            a = 0;

            for (j = 0; j < n1; j++) {
                c = cos[a];
                s = sin[a];
                a += 1 << (m - i - 1);

                for (k = j; k < n; k = k + n2) {
                    t1 = c * x[k + n1] - s * y[k + n1];
                    t2 = s * x[k + n1] + c * y[k + n1];
                    x[k + n1] = x[k] - t1;
                    y[k + n1] = y[k] - t2;
                    x[k] = x[k] + t1;
                    y[k] = y[k] + t2;
                }
            }
        }
    }
}
