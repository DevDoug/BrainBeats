package Models;

/**
 * Created by Douglas on 4/8/2015.
 */
public class WaveModel {

    //should store divide the actual wave amp upper bound by some factor to make it appear better on screen
    public String mWaveName;
    public double mAmplitude;
    public int mFrequency;
    public  double mAmpScaleFactor;
    public  int  mAmplitudeUpperBound;
    public  int  mAmplitudeLowerBound;

    public WaveModel(String wavename, int waveamp, int wavefrequency, int ampscalefactor, int amplitudeupperbound, int amplitudelowerbound){
        this.mWaveName = wavename;
        this.mAmplitude = waveamp;
        this.mFrequency = wavefrequency;
        this.mAmpScaleFactor = ampscalefactor;
        this.mAmplitudeUpperBound = amplitudeupperbound;
        this.mAmplitudeLowerBound = amplitudelowerbound;
    }
    public double getWaveAmplitude(){
        return mAmplitude;
    }
    public int getWaveFrequency(){
       return mFrequency;
    }
    public int getWaveAmplitudeUpperBound(){
        return mAmplitudeUpperBound;
    }
    public int getWaveAmplitudeLowerBOund(){
        return mAmplitudeLowerBound;
    }
}
