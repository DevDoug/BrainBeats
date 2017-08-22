package com.brainbeats.utils;

import android.content.Context;
import android.content.res.Resources;

import com.brainbeats.R;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by Doug on 8/20/2017.
 */

public class Maestro {

    private int standardSongWordCount = 20;

    private static final Maestro instance = new Maestro();

    public static Maestro getInstance() {
        return instance;
    }

    private Maestro() {
    }

    public String generateLyrics(Context context){
        String lyrics = "";

        try {
            Resources res = context.getResources();
            InputStream inputStream = res.openRawResource(R.raw.words);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            String wordList = new String(b);
            String[] words = wordList.split(" ");
            for(int i = 0; i < standardSongWordCount; i++) {
                Random randomGenerator = new Random();
                int random = randomGenerator.nextInt(words.length);
                lyrics = lyrics.concat(words[random]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lyrics;
    }
}
