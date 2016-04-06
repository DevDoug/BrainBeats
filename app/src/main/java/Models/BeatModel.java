package Models;

/**
 * Created by Douglas on 3/29/2015.
 */
public class BeatModel {

    public WaveModel mAlphaWave;
    public WaveModel mGammaWave;
    public WaveModel mThetaWave;
    public WaveModel mDeltaWave;

    public BeatModel(WaveModel AlphaWave,WaveModel GammWave, WaveModel thetawave, WaveModel deltawave){
        this.mAlphaWave = AlphaWave;
        this.mGammaWave = GammWave;
        this.mThetaWave = thetawave;
        this.mDeltaWave = deltawave;
    }
}
