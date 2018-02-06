package com.brainbeats.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by douglas.ray on 1/26/2018.
 */

public class MaestroGeneratedPart {

    @SerializedName("notes")
    public int[] mNotes;

    public int[] getNotes() {
        return mNotes;
    }

    public void setNotes(int[] notes) {
        this.mNotes = notes;
    }
}