package com.brainbeats.utils;

/*
 * Created by Doug on 8/20/2017.
 */

public class Maestro {
    private static final Maestro instance = new Maestro();

    public static Maestro getInstance() {
        return instance;
    }

    private Maestro() {
    }

    public String generateLyrics(){
        return "I like big booty bitches ba ba big booty bitches";
    }


}
