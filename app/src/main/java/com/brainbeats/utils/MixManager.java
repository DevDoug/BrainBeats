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

    public static synchronized MixManager getInstance() {
        if (mInstance == null) {
            mInstance = new MixManager();
        }
        return mInstance;
    }




}
