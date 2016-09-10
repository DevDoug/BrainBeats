package utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import entity.Collection;
import entity.RelatedTracksResponse;
import entity.Track;
import web.WebApiManager;

/**
 * Created by douglas on 5/23/2016.
 * Class should be responsible for directing what beats are suggested to the user based
 * on previus choice and other factors
 */
public class BeatLearner {

    public static BeatLearner mBeatLearnerInstance;
    public Context mContext;

    public BeatLearner(Context context) {
        this.mContext = context;
    }

    public static BeatLearner getInstance(Context context) {
        if (mBeatLearnerInstance == null) {
            mBeatLearnerInstance = new BeatLearner(context);
        }
        return mBeatLearnerInstance;
    }

    public interface RecommendationCompleteListener {
        void recommendationComplete(Track track);
    }

    //TODO:Implement machine learning recommendation
    public void loadNextRecommendedBeat(int selectedTrackId, RecommendationCompleteListener listener) {
        //for now do something very basic and just return a random related mix.
        WebApiManager.getRelatedTracks(mContext, String.valueOf(selectedTrackId), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Log.i(getClass().getSimpleName(), "Response = " + object.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<RelatedTracksResponse>() {
                }.getType();
                try {
                    RelatedTracksResponse relatedTracks = gson.fromJson(object.toString(), token);
                    ArrayList<Collection> mCollections = (ArrayList<Collection>) relatedTracks.getCollection();

                    Random rand = new Random();
                    int n = rand.nextInt(mCollections.size());
                    Collection randomTrack = mCollections.get(n);

                    WebApiManager.getTrack(mContext, String.valueOf(randomTrack.getId()), new WebApiManager.OnObjectResponseListener() {
                        @Override
                        public void onObjectResponse(JSONObject object) {
                            Gson gson = new Gson();
                            Type token = new TypeToken<Track>() {
                            }.getType();
                            Track relatedTrack = gson.fromJson(object.toString(), token);

                            WebApiManager.getSoundCloudUser(mContext, String.valueOf(relatedTrack.getUser().getId()), new WebApiManager.OnObjectResponseListener() {
                                @Override
                                public void onObjectResponse(JSONObject object) {
                                    Gson gson = new Gson();
                                    Type token = new TypeToken<entity.User>() {}.getType();
                                    entity.User soundCloudUser = gson.fromJson(object.toString(), token);
                                    relatedTrack.setUser(soundCloudUser);
                                    listener.recommendationComplete(relatedTrack);
                                }
                            }, new WebApiManager.OnErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                            //listener.recommendationComplete(relatedTrack);
                        }
                    }, new WebApiManager.OnErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "Response = " + error.toString());
            }
        });
    }

    //TODO - implement in version 2.0 beta version
    public Track downVoteTrack(int selectedTrackId) { //load the last track they played
        return null;
    }

    //TODO - implement in version 2.0 beta version
    public Track upVoteTrack(int selectedTrackId){
        return null;
    }
}
