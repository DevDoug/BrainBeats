package com.brainbeats.sync;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.brainbeats.R;

import com.brainbeats.architecture.AccountManager;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;

/**
 * Created by douglas on 7/28/2016.
 */
public class OfflineSyncManager {

    private static OfflineSyncManager mInstance;
    private Context mContext;

    public OfflineSyncManager(Context context) {
        mContext = context;
    }

    public static synchronized OfflineSyncManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new OfflineSyncManager(context);
        }
        return mInstance;
    }

    public void performSyncOnLocalDb(CoordinatorLayout coordinatorLayout, Bundle extras, ContentResolver provider) {

        Track selectedTrack = extras.getParcelable(Constants.KEY_EXTRA_SELECTED_TRACK);
        Mix selectedMix = extras.getParcelable(Constants.KEY_EXTRA_SELECTED_MIX);
        int SYNC_TYPE = extras.getInt(Constants.KEY_EXTRA_SYNC_TYPE);
        int updateMixAction = extras.getInt(Constants.KEY_EXTRA_SYNC_ACTION);

        switch (SYNC_TYPE) {
            case 0: //com.brainbeats.sync mixes
                try {
                    if(selectedTrack != null){
                        final Cursor mixCursor = provider.query(
                                BrainBeatsContract.MixEntry.CONTENT_URI, //Get mixes
                                null,  //return everything
                                BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                new String[]{String.valueOf(selectedTrack.getID())},
                                null
                        );

                        if (mixCursor != null) {
                            mixCursor.moveToFirst();

                            switch (updateMixAction) {
                                case 0: //add to lib
                                    if (mixCursor.getCount() != 0) { // this mix exists so update the record.
                                        Mix mix = Constants.buildMixFromCursor(mContext, mixCursor, 0);
                                        if (mix.getIsInLibrary() == 0) {
                                            mix.setIsInLibrary(1);
                                            updateMixRecord(provider, mix, selectedTrack.getID());
                                            showSnackMessage(coordinatorLayout, R.string.song_added_to_library_snack_message);
                                        } else if (mix.getIsInLibrary() == 1) {
                                            showSnackMessage(coordinatorLayout, R.string.error_this_mix_is_already_in_library);
                                        }
                                    } else {
                                        addMix(selectedTrack, true, false, false, provider); // create this as a mix from a sound cloud track
                                        showSnackMessage(coordinatorLayout, R.string.song_added_to_library_snack_message);
                                    }
                                    break;
                                case 1: //add to fav
                                    if (mixCursor.getCount() != 0) { // this mix exists so update the record.
                                        Mix mix = Constants.buildMixFromCursor(mContext, mixCursor, 0);
                                        if (mix.getMixFavorite() == 0) {
                                            mix.setMixFavorite(1);
                                            updateMixRecord(provider, mix, selectedTrack.getID());
                                            showSnackMessage(coordinatorLayout, R.string.song_added_to_favorites_snack_message);
                                        } else if (mix.getMixFavorite() == 1) {
                                            showSnackMessage(coordinatorLayout, R.string.error_this_mix_is_already_a_favorite);
                                        }
                                    } else {
                                        addMix(selectedTrack, false, true, false, provider); // create this as a mix from a sound cloud track
                                        showSnackMessage(coordinatorLayout, R.string.song_added_to_favorites_snack_message);
                                    }

                                    if (AccountManager.getInstance(mContext).getIsSyncedToSoundCloud()) {
                                        Bundle settingsBundle = new Bundle();
                                        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, 0);
                                        settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, 2); //Action add mix to user fav on sc.
                                        settingsBundle.putInt(Constants.KEY_EXTRA_SELECTED_TRACK_ID, selectedTrack.getID());
                                        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                                        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                                        SyncManager.getInstance().performSyncOnTable(settingsBundle);
                                    }
                                    break;
                                case 2: //change to bieng mixed in mixer
                                    if (mixCursor.getCount() != 0) { // this mix exists so update the record.
                                        Mix mix = Constants.buildMixFromCursor(mContext, mixCursor, 0);
                                        if (mix.getIsInMixer() == 0) {
                                            mix.setIsInMixer(1);
                                            updateMixRecord(provider, mix, selectedTrack.getID());
                                            showSnackMessage(coordinatorLayout, R.string.item_updated_mix);
                                        } else if (mix.getIsInMixer() == 1) {
                                            showSnackMessage(coordinatorLayout, R.string.error_this_mix_is_in_the_mixer);
                                        }
                                    } else {
                                        addMix(selectedTrack, true, false, false, provider); // create this as a mix from a sound cloud track
                                        showSnackMessage(coordinatorLayout, R.string.song_added_to_mixer_snack_message);
                                    }
                                    break;
                            }
                        }
                    } else {
                        if(selectedMix != null) {
                            selectedMix.setIsInMixer(1);
                            updateMixRecord(provider, selectedMix, selectedMix.getSoundCloudId());
                            showSnackMessage(coordinatorLayout, R.string.item_updated_mix);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //TODO after updating locally tell the syncadapter to update the server if it can be updated on server E.X. user favorites can be added to SC
                break;
            case 1: //com.brainbeats.sync mix related
                break;
            case 2: //com.brainbeats.sync mix playlist
                break;
            case 3: // com.brainbeats.sync user's
                //if the artist exists then see if they are following and toggle otherwise just add user
                final Cursor artistCursor = provider.query(
                        BrainBeatsContract.UserEntry.CONTENT_URI, //Get users
                        null,  //return everything
                        BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                        new String[]{String.valueOf(selectedTrack.getUser().getId())},
                        null
                );

                if (artistCursor != null) {
                    artistCursor.moveToFirst();

                    if (artistCursor.getCount() != 0) { // this user exists so update the record.
                        final Cursor followingArtistCursor = provider.query(
                                BrainBeatsContract.UserFollowersEntry.CONTENT_URI, //Get mixes
                                null,  //return everything
                                BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                new String[]{String.valueOf(selectedTrack.getUser().getId())},
                                null
                        );

                        if (followingArtistCursor.getCount() != 0) {
                            int result = provider.delete(BrainBeatsContract.UserFollowersEntry.CONTENT_URI, BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, new String[]{String.valueOf(selectedTrack.getUser().getId())});
                            showSnackMessage(coordinatorLayout, R.string.artist_removed_from_following);
                        } else {
                            Uri result = provider.insert(BrainBeatsContract.UserFollowersEntry.CONTENT_URI, Constants.buildUserFollowingRecord(AccountManager.getInstance(mContext).getUserId(), String.valueOf(selectedTrack.getUser().getId())));
                            showSnackMessage(coordinatorLayout, R.string.artist_added_to_following);
                        }
                    } else {
                        addUser(selectedTrack.getUser(), true, provider); // create this as a user from sound cloud
                        showSnackMessage(coordinatorLayout, R.string.artist_added_to_following);
                    }
                    artistCursor.close();
                }
                break;
            default:
                break;
        }

    }

    public void addMix(Track track, boolean inLibrary, boolean isFavorite, boolean inMixer, ContentResolver provider) {
        Mix newMix = Constants.buildMixRecordFromTrack(track);
        newMix.setMixFavorite((isFavorite) ? 1 : 0);
        newMix.setIsInLibrary((inLibrary) ? 1 : 0);
        newMix.setIsInMixer((inMixer) ? 1 : 0);

        //Before adding this mix add the mix user if they are not already in the Brain beats system
        Cursor userCursor = mContext.getContentResolver().query( //get this mixes user
                BrainBeatsContract.UserEntry.CONTENT_URI,
                null,  //return everything
                BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                new String[]{String.valueOf(track.getUser().getId())},
                null);

        long userId;
        if(userCursor != null && userCursor.getCount() != 0 ) {
            // this user already exists so just get the user id
            userCursor.moveToFirst();
            userId = userCursor.getLong(userCursor.getColumnIndex(BrainBeatsContract.UserEntry._ID));
            newMix.setMixUserId(userId);
            userCursor.close();
        } else { //otherwise add the user this mix belongs to
            Uri result = provider.insert(BrainBeatsContract.UserEntry.CONTENT_URI, Constants.buildUserRecord(new BrainBeatsUser(track.getUser())));
            userId = ContentUris.parseId(result);
            newMix.setMixUserId(userId);
        }

        Log.i("New Mix", "Adding new mix");
        try {
            Uri result = provider.insert(BrainBeatsContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(newMix));
            mContext.getContentResolver().notifyChange(BrainBeatsContract.MixEntry.CONTENT_URI, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addUser(com.brainbeats.entity.User collection, boolean isFollowing, ContentResolver provider) {
        BrainBeatsUser brainBeatsUser = new BrainBeatsUser();
        brainBeatsUser.setUserName(collection.getUsername());
        brainBeatsUser.setSoundCloudUserId(collection.getId());

        try {
            Uri result = provider.insert(BrainBeatsContract.UserEntry.CONTENT_URI, Constants.buildUserRecord(brainBeatsUser)); //insert brainBeatsUser rec
            if (isFollowing) { //if the brainBeatsUser is following this person add the record to the following table, now when quered
                Uri relatedResult = provider.insert(BrainBeatsContract.UserFollowersEntry.CONTENT_URI, Constants.buildUserFollowingRecord(AccountManager.getInstance(mContext).getUserId(), String.valueOf(collection.getId())));
                Log.i("Add brainBeatsUser", "brainBeatsUser collection Id " + String.valueOf(collection.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMixRecord(ContentResolver provider, Mix mix, int selectedTrackId) {
        try {
            int returnId = provider.update(
                    BrainBeatsContract.MixEntry.CONTENT_URI,
                    Constants.buildMixRecord(mix),
                    BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                    new String[]{String.valueOf(selectedTrackId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSnackMessage(CoordinatorLayout layout, int messageResource) {
        Snackbar createdSnack;
        createdSnack = Snackbar.make(layout, messageResource, Snackbar.LENGTH_LONG);
        createdSnack.show();
    }
}
