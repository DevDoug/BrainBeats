package com.brainbeats.utils;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.brainbeats.entity.Track;
import com.brainbeats.model.MixItem;

import java.util.ArrayList;

/*
 * Created by douglas on 6/15/2016.
 */

public class MixManager {

    private static MixManager mInstance;

    public static final int DURATION = 150;
    public static final int SAMPLE_RATE = 8000;

    public static synchronized MixManager getInstance() {
        if (mInstance == null) {
            mInstance = new MixManager();
        }
        return mInstance;
    }

    public AudioTrack generateGuitarPart(int noteOne){

        // Get array of frequencies with their relative strengths
        double[][] soundData = buildSoundData(noteOne);

        // TODO
        // Perform a calculation to fill an array with the mixed sound - then play it in an infinite loop
        // Need an AudioTrack that will play calculated loop
        // Track sample info
        int numOfSamples = DURATION * SAMPLE_RATE;
        double sample[] = new double[numOfSamples];
        byte sound[] = new byte[2 * numOfSamples];

        // fill out the array
        for (int i = 0; i < numOfSamples; ++i) {
            double valueSum = 0;

            for (int j = 0; j < soundData.length; j++) {
                valueSum += Math.sin(2 * Math.PI * i / (SAMPLE_RATE / soundData[j][0]));
            }

            sample[i] = valueSum / soundData.length;
        }

        int i = 0;
        for (double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            sound[i++] = (byte) (val & 0x00ff);
            sound[i++] = (byte) ((val & 0xff00) >>> 8);
        }

        // Obtain a minimum buffer size
        int minBuffer = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        if (minBuffer > 0) {
            // Create an AudioTrack
            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, numOfSamples, AudioTrack.MODE_STATIC);

            // Write audio com.brainbeats.data to track
            track.write(sound, 0, sound.length);

            return track;
        }

        return null;
    }

    private double[][] buildSoundData(int noteOne) {
        double[][] soundData = new double[1][4];
        soundData[0][0] = noteOne;
        soundData[0][1] = noteOne;
        soundData[0][2] = noteOne;
        soundData[0][3] = noteOne;
        return soundData;
    }
}
