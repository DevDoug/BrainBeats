package com.brainbeats.utils;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.model.MixItem;
import com.brainbeats.model.MixPlaylist;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Douglas on 4/20/2016.
 * Constants for Brain Beats Application.
 */
public class Constants {

    //= Keys for bundles and extras =============================================
    public static final String KEY_LIBRARY_DATA_TYPE                        = "LibraryDataType";
    public static final String KEY_EXTRA_LIBRARY_FILTER_TEXT                = "Filter";
    public static final String KEY_EXTRA_SELECTED_TRACK                     = "SelectedTrack";
    public static final String KEY_EXTRA_SELECTED_MIX                       = "SelectedMix";
    public static final String KEY_EXTRA_SELECTED_USER                      = "SelectedUser";
    public static final String KEY_EXTRA_SEARCH_KEYWORD                     = "SearchKeyword";
    public static final String KEY_EXTRA_SYNC_TYPE                          = "SyncType";
    public static final String KEY_EXTRA_SYNC_ACTION                        = "SyncAction";
    public static final String KEY_EXTRA_SELECTED_TRACK_ID                  = "TrackId";

    //Hash map keys
    public static final String HASH_KEY_ACCESS_TOKEN                        = "access_token";

    //Shared Preferences
    public static final String KEY_EXTRA_IS_INCOGNITO_PREF                  = "IsIncognito";
    public static final String KEY_EXTRA_IS_SYNCED_TO_SC                    = "SyncedToSoundCloud";
    public static final String KEY_EXTRA_IS_GLOBAL_SYNC_REQUIRED            = "IsGlobalSyncRequired";
    public static final String KEY_EXTRA_IS_DISPLAY_CURRENT_PLAYING_SONG    = "currentPlayingSong";
    public static final String RESTORE_PLAYING_SONG                         = "restorePlayingSong";

    public static final String KEY_EXTRA_MAESTRO_SHOWN                      = "MaestroShown";
    public static final String KEY_EXTRA_FIREBASE_TOKEN                      = "FirebaseToken";





    //Sound Cloud
    public static final String SOUND_CLOUD_CLIENT_ID                        = "6af4e9b999eaa63f5d797d466cdc4ccb";
    public static final String SOUND_CLOUD_CLIENT_SECRET                    = "09e8c5b6f91e2ab440b9137008d2d32c";
    public static final String OAUTH_CALLBACK_SCHEME                        = "brainbeats";
    public static final String OAUTH_CALLBACK_HOST                          = "soundcloud/callback";
    public static final String CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

    //Loader Types
    public static final int SOCIAL_LOADER               = 101;
    //public static final int RELATED_TRACKS_LOADER     = 102; //TODO - implement in version 2.0 beta version
    public static final int MIX_TAGS_LOADER             = 103;
    public static final int MIXES_LOADER                = 104;
    public static final int MIX_ITEMS_LOADER            = 105;

    //Intents and Communication
    public static final String SONG_COMPLETE_BROADCAST_ACTION               = "com.brainbeats.play.next";
    public static final String SONG_DONE_LOADING_BROADCAST_ACTION = "com.brainbeats.loading.next";
    public static final String SONG_ERROR_BROADCAST_ACTION                  = "com.brainbeats.song.error";
    public static final String PLAYLIST_COMPLETE_BROADCAST_ACTION           = "com.brainbeats.playlist.complete";
    public static final String RESTORE_FROM_SERVICE_BROADCAST_ACTION        = "com.brainbeats.restore.from.service";



    public static final String INTENT_ACTION_GO_TO_DETAIL_FRAGMENT          = "LoadDetailFragment";

    public static final String INTENT_ACTION_LOAD_FROM_NEW_INTENT           = "LoadFromNewIntent";

    public static final String INTENT_ACTION_GO_TO_MIX_DETAIL_FRAGMENT      = "LoadMixDetailFragment";
    public static final String INTENT_ACTION_DISPLAY_CURRENT_TRACK          = "DisplayCurrentTrack";

    //Misc
    public static final int GRID_SPAN_COUNT = 2;
    public static final int BEAT_LEVEL_INCREASE_DIFFERENCE = 10;
    public static final int MIX_ITEM_DEFAULT_LEVEL = 50;
    public static final int BEAT_ITEM_DRAWABLES[] = new int[]{R.drawable.ic_alpha, R.drawable.ic_beta, R.drawable.ic_google, R.drawable.ic_theta,};

    public static final int USERNAME_MINIMUM_LENGTH = 3;
    public static final int PASSWORD_MINIMUM_LENGTH = 6;


    //Fragment Uris
    public static final Uri LOAD_SONG_URI                                           = Uri.parse("base://dashboard_detail_load_song");
    public static final Uri DASHBOARD_DETAIL_URI                                    = Uri.parse("main://dashboard_detail");
    public static final Uri DASHBOARD_DETAIL_LOAD_DASHBOARD_FAB_IMAGES              = Uri.parse("main://dashboard_detail_load_fab_images");

    public static final Uri DASHBOARD_DETAIL_LOAD_SONG_URI                          = Uri.parse("main://dashboard_detail_load_song");
    public static final Uri DASHBOARD_DETAIL_LOAD_NEW_SONG_URI                      = Uri.parse("main://dashboard_detail_load_new_song");

    public static final Uri DASHBOARD_DETAIL_PLAY_SONG_URI                          = Uri.parse("main://dashboard_detail_play_song");
    public static final Uri DASHBOARD_DETAIL_PAUSE_SONG_URI                         = Uri.parse("main://dashboard_detail_pause_song");
    public static final Uri STOP_SONG_URI                                           = Uri.parse("audio://stop_song");

    public static final Uri GO_TO_REGISTER_NEW_USER_URI                             = Uri.parse("main://register_new_user");
    public static final Uri CREATE_NEW_USER_URI                                     = Uri.parse("main://create_new_user");
    public static final Uri LOGOUT_URI                                              = Uri.parse("main://logout");

    public static final Uri DASHBOARD_DETAIL_DOWNVOTE_SONG_URI                      = Uri.parse("main://dashboard_detail_downvote_song");
    public static final Uri DASHBOARD_DETAIL_SKIP_FORWARD_URI                       = Uri.parse("main://dashboard_detail_skip_forward");
    public static final Uri DASHBOARD_DETAIL_SET_SONG_REPEAT_URI                    = Uri.parse("main://dashboard_detail_repeat_song");
    public static final Uri DASHBOARD_DETAIL_UPDATE_CURRENT_PLAYING_SONG_VIEW       = Uri.parse("main://dashboard_detail_update_current_playing_view");
    public static final Uri DASHBOARD_DETAIL_UPDATE_PROGRESS_BAR_THREAD             = Uri.parse("main://dashboard_detail_update_progress_bar_thread");
    public static final Uri DASHBOARD_DETAIL_CHECK_IF_CURRENT_SONG                  = Uri.parse("main://dashboard_detail_check_if_current_song");

    public static final Uri MIX_SHOW_FAB                                            = Uri.parse("mixer://mix_show_fab");
    public static final Uri NEW_MIX_HIDE_FAB                                        = Uri.parse("mixer://new_mix_hide_fab");
    public static final Uri NEW_MIX_CREATE                                          = Uri.parse("mixer://new_mix_load_create_frag");
    public static final Uri NEW_MIX_LOAD_CONFIRM_FRAG                               = Uri.parse("mixer://new_mix_load_confirm_frag");
    public static final Uri MIX_SHOW_MIX_LIST                                       = Uri.parse("mixer://mix_show_mix_list");
    public static final Uri MIX_ADD_NEW                                             = Uri.parse("mixer://mix_add_new");

    public static final Uri SHOW_NEW_ARTIST_INFO                                    = Uri.parse("mixer://new_artist");
    public static final Uri GO_TO_ARTIST_PROFILE_URI                                = Uri.parse("mixer://go_to_artist_profile");
    public static final Uri ACCEPT_FRIEND_REQUEST_URI                               = Uri.parse("mixer://accept_friend_request");
    public static final Uri GO_TO_ALL_FRIEND_REQUEST_URI                            = Uri.parse("mixer://go_to_all_friend_request");

    public enum AudioServiceRepeatType {
        RepeatOff(0),
        RepeatOne(1),
        RepeatAll(2);

        private int mCode;

        AudioServiceRepeatType(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    public enum LibraryDataType {
        Songs(0),
        Playlists(1),
        Favorites(2);

        private int mCode;

        LibraryDataType(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    public enum SyncDataType {
        Mixes(0),
        RelatedMixes(1),
        Playlists(2),
        Users(3);

        private int mCode;

        SyncDataType(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    public enum SyncDataAction {
        UpdateMix(0),
        UpdateFavorite(1),
        UpdateAddToMixer(2);

        private int mCode;

        SyncDataAction(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    public interface ConfirmDialogActionListener{
        void PerformDialogAction();
    }

    public static Mix buildMixRecordFromTrack(Track track) {
        Mix mix = new Mix();
        //mix.setMixId(track.getID());
        mix.setMixTitle(track.getTitle());
        mix.setMixAlbumCoverArt(track.getArtworkURL());
        mix.setSoundCloudId(track.getID());
        mix.setStreamURL(track.getStreamURL());
        mix.setDuration(track.getDuration());
        mix.setMixTagList(track.getTagList());
        return mix;
    }

    public static Mix buildMixFromCursor(Context context, Cursor cursor, int position) {
        cursor.moveToPosition(position);

        Mix mix = new Mix();
        //mix.setMixId(cursor.getLong(cursor.getColumnIndex(BrainBeatsContract.MixEntry._ID)));
/*        mix.setMixTitle(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
        mix.setMixAlbumCoverArt(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL)));
        mix.setMixFavorite(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE)));
        mix.setSoundCloudId(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID)));
        mix.setMixUserId(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_USER_ID_FK)));
        mix.setIsInLibrary(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY)));
        mix.setIsInMixer(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_MIXER)));
        mix.setStreamURL(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_STREAM_URL)));
        mix.setDuration(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_DURATION)));

        Cursor userCursor = context.getContentResolver().query( //get this mixes user
                BrainBeatsContract.UserEntry.CONTENT_URI,
                null,  //return everything
                BrainBeatsContract.UserEntry._ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                new String[]{String.valueOf(mix.getMixUserId())},
                null);

        BrainBeatsUser brainBeatsBrainBeatsUser = new BrainBeatsUser();
        if (userCursor != null && userCursor.getCount() != 0) {
            userCursor.moveToFirst();
*//*            brainBeatsBrainBeatsUser.setUserId(userCursor.getLong(userCursor.getColumnIndex(BrainBeatsContract.UserEntry._ID)));
            brainBeatsBrainBeatsUser.setUserName(userCursor.getString(userCursor.getColumnIndex(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME)));*//*
            brainBeatsBrainBeatsUser.setSoundCloudUserId(userCursor.getInt(userCursor.getColumnIndex(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID)));
            userCursor.close();
            mix.setUser(brainBeatsBrainBeatsUser);
        }*/

//        Cursor mixItemsCursor = context.getContentResolver().query( //get this mixes mix items
//                BrainBeatsContract.MixItemsEntry.CONTENT_URI,
//                null,  //return everything
//                BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
//                new String[]{" " + cursor.getLong(cursor.getColumnIndex(BrainBeatsContract.MixEntry._ID))},
//                null);

/*        if (mixItemsCursor != null) {
            mixItemsCursor.moveToFirst();

            ArrayList<MixItem> mixItems = new ArrayList<>();
            for (int i = 0; i < mixItemsCursor.getCount(); i++) {
                MixItem mixItem = new MixItem();
                mixItem.setMixItemId(mixItemsCursor.getLong(mixItemsCursor.getColumnIndex(BrainBeatsContract.MixItemsEntry._ID)));
                mixItem.setMixItemTitle(mixItemsCursor.getString(mixItemsCursor.getColumnIndex(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE)));
                mixItem.setMixItemLevel(mixItemsCursor.getInt(mixItemsCursor.getColumnIndex(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL)));
                mixItems.add(mixItem);
                mixItemsCursor.moveToNext();
            }
            mix.setMixItems(mixItems);
            mixItemsCursor.close();
        }*/

        return mix;
    }

    public static BrainBeatsUser buildUserFromCursor(Context context, Cursor cursor) {
        cursor.moveToFirst();

        BrainBeatsUser brainBeatsUser = new BrainBeatsUser();
        //brainBeatsUser.setUserId(cursor.getLong(cursor.getColumnIndex(BrainBeatsContract.UserEntry._ID)));
        brainBeatsUser.setUserName(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME)));
        brainBeatsUser.setSoundCloudUserId(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID)));

        return brainBeatsUser;
    }

    public static ContentValues buildMixRecord(Mix mix) {
        ContentValues values = new ContentValues();
/*        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE, mix.getMixTitle());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL, mix.getMixAlbumCoverArt());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE, mix.getMixFavorite());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_USER_ID_FK, mix.getMixUserId());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID, mix.getSoundCloudId());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_RELATED_MIXES_ID, mix.getRelatedTracksId());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY, mix.getIsInLibrary());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_MIXER, mix.getIsInMixer());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_STREAM_URL, mix.getStreamURL());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_DURATION, mix.getDuration());*/
        return values;
    }

    public static ContentValues buildMixItemsRecord(long mixId, MixItem mixitem) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE, mixitem.getMixItemTitle());
        values.put(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL, mixitem.getMixItemLevel());
        values.put(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY, mixId);
        return values;
    }

    public static ContentValues buildMixPlaylistRecord(MixPlaylist mixPlaylist) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_MIX_ID, mixPlaylist.getMixId());
        values.put(BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_ID, mixPlaylist.getPlaylistId());
        return values;
    }

    public static ContentValues buildUserRecord(BrainBeatsUser brainBeatsUser) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME, brainBeatsUser.getUserName());
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD, Constants.generateEncryptedPass());
        //values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PROFILE_IMG, brainBeatsUser.getUserProfileImage());
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID, brainBeatsUser.getSoundCloudUserId());
        return values;
    }

    public static ContentValues buildUserFollowingRecord(String bbUserId, String scUserId) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_ID, bbUserId);
        values.put(BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID, scUserId);
        return values;
    }

    public static ContentValues[] buildMixItemsBulkRecord(Context context, long mixId) {

        Cursor mixItemsCursor = context.getContentResolver().query( //get this mixes mix items
                BrainBeatsContract.MixItemsEntry.CONTENT_URI,
                null,  //return everything
                BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                new String[]{" " + mixId},
                null);

        if (mixItemsCursor != null) {
            mixItemsCursor.moveToFirst();

            ArrayList<MixItem> mixItems = new ArrayList<>();
            for (int i = 0; i < mixItemsCursor.getCount(); i++) {
                MixItem mixItem = new MixItem();
                mixItem.setMixItemId(mixItemsCursor.getLong(mixItemsCursor.getColumnIndex(BrainBeatsContract.MixItemsEntry._ID)));
                mixItem.setMixItemTitle(mixItemsCursor.getString(mixItemsCursor.getColumnIndex(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE)));
                mixItem.setMixItemLevel(mixItemsCursor.getInt(mixItemsCursor.getColumnIndex(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL)));
                mixItems.add(mixItem);
                mixItemsCursor.moveToNext();
            }

            ContentValues[] contentValues = new ContentValues[mixItems.size()];
            for (int i = 0; i < mixItems.size(); i++) {
                contentValues[i] = buildMixItemsRecord(mixId, mixItems.get(i));
            }

            mixItemsCursor.close();
            return contentValues;
        }

        return null;
    }

    public static AlertDialog buildInfoDialog(Context context, String title, String dialogMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }

    public static AlertDialog buildActionDialog(Context context, String title, String dialogMessage, String positiveButtonConfirm, ConfirmDialogActionListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(dialogMessage);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(positiveButtonConfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.PerformDialogAction();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static HashMap<String, String> mapQueryParams(String fragmentString) {
        HashMap<String, String> queryMap = new HashMap<>();
        String[] paramList = fragmentString.split("#");
        String[] params = paramList[0].split("&");

        for (String param : params) {
            String key = param.split("=")[0];
            String value = param.split("=")[1];
            queryMap.put(key, value);
        }
        return queryMap;
    }

    public static String generateUIFriendlyString(String string){
        String friendlyString = string.split("-")[0];
        return friendlyString;
    }

    //TODO:implement actuall encryption
    public static String generateEncryptedPass() {
        return "Password1";
    }

    public static  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}