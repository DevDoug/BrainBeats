package utils;

/**
 * Created by douglas on 5/23/2016.
 * Class should be responsible for directing what beats are suggested to the user based
 * on previus choice and other factors
 */
public class BeatLearner {

    public static BeatLearner mBeatLearnerInstance;
    public boolean mHasStartedLearning;    //if false new user so we have data to work with

    public static BeatLearner getInstance() {
        if (mBeatLearnerInstance == null) {
            mBeatLearnerInstance = new BeatLearner();
        }
        return mBeatLearnerInstance;
    }

    //TODO:Implement machine learing recommendation
    public void loadNextRecommendedBeat() {

    }

    public void loadLastBeat(){

    }
}
