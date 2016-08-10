package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainbeats.R;
import java.util.ArrayList;
import java.util.HashMap;

import adapters.ImageAdapter;
import data.BrainBeatsContract;
import entity.Track;
import model.Mix;
import model.MixItem;
import model.Playlist;
import model.User;

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
    public static final String KEY_EXTRA_SEARCH_KEYWORD                     = "SearchKeyword";
    public static final String KEY_EXTRA_SYNC_TYPE                          = "SyncType";
    public static final String KEY_EXTRA_SYNC_ACTION                        = "SyncAction";
    public static final String KEY_EXTRA_SELECTED_TRACK_ID                  = "TrackId";

    //Hash map keys
    public static final String HASH_KEY_ACCESS_TOKEN     = "access_token";

    //Shared Preferences
    public static final String KEY_EXTRA_IS_INCOGNITO_PREF  = "IsIncognito";
    public static final String KEY_EXTRA_IS_SYNCED_TO_SC    = "SyncedToSoundCloud";
    public static final String KEY_EXTRA_IS_GLOBAL_SYNC_REQUIRED = "IsGlobalSyncRequired";

    //Sound Cloud
    public static final String SOUND_CLOUD_CLIENT_ID       = "6af4e9b999eaa63f5d797d466cdc4ccb";
    public static final String SOUND_CLOUD_CLIENT_SECRET   = "09e8c5b6f91e2ab440b9137008d2d32c";

    //Loader Types
    public static final int SOCIAL_LOADER  = 101;
    public static final int RELATED_TRACKS_LOADER  = 102;

    //Misc

    public static final int GRID_SPAN_COUNT = 3;
    public static final int BEAT_LEVEL_INCREASE_DIFFERENCE = 10;
    public static final int MIX_ITEM_DEFAULT_LEVEL = 50;
    public static final int BEAT_ITEM_DRAWABLES[] = new int[]{R.drawable.ic_alpha, R.drawable.ic_beta, R.drawable.ic_google, R.drawable.ic_theta,};
    public static final int PASSWORD_MINIMUM_LENGTH = 4;

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

    public static Mix buildMixRecordFromTrack(Track track) {
        Mix mix = new Mix();
        mix.setMixTitle(track.getTitle());
        mix.setMixAlbumCoverArt(track.getArtworkURL());
        mix.setSoundCloudId(track.getID());
        //mix.setRelatedTracksId(relatedTracksId);
        return mix;
    }

    public static Mix buildMixFromCursor(Context context, Cursor cursor, int position) {
        cursor.moveToPosition(position);
        Mix mix = new Mix();
        mix.setMixId(cursor.getLong(cursor.getColumnIndex(BrainBeatsContract.MixEntry._ID)));
        mix.setMixTitle(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
        mix.setMixAlbumCoverArt(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL)));
        mix.setMixFavorite(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE)));
        mix.setSoundCloudId(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID)));
        mix.setMixUserId(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_USER_ID_FK)));
        mix.setIsInLibrary(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY)));
        mix.setIsInMixer(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_MIXER)));

        String whereClause = BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + "= ?";
        String[] whereArgs = new String[]{
                " " + cursor.getLong(cursor.getColumnIndex(BrainBeatsContract.MixEntry._ID)),
        };

        Cursor mixItemsCursor = context.getContentResolver().query(BrainBeatsContract.MixItemsEntry.CONTENT_URI, null, whereClause, whereArgs, null); // get the mix items associated with this mix

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
            mix.setMixItems(mixItems);
            mixItemsCursor.close();
        }

        return mix;
    }

    public static User buildUserFromCursor(Context context, Cursor cursor) {
        cursor.moveToFirst();

        User user = new User();
        user.setUserId(cursor.getLong(cursor.getColumnIndex(BrainBeatsContract.UserEntry._ID)));
        user.setUserName(cursor.getString(cursor.getColumnIndex(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME)));
        user.setSoundCloudUserId(cursor.getInt(cursor.getColumnIndex(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID)));

        return user;
    }

    public static ContentValues buildMixRecord(Mix mix) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE, mix.getBeatTitle());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL, mix.getMixAlbumCoverArt());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE, mix.getMixFavorite());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_USER_ID_FK, mix.getMixUserId());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID, mix.getSoundCloudId());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_RELATED_MIXES_ID, mix.getRelatedTracksId());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY, mix.getIsInLibrary());
        values.put(BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_MIXER, mix.getIsInMixer());
        return values;
    }

    public static ContentValues buildMixRelatedRecord() {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.MixRelatedEntry.COLUMN_NAME_TAG_CLOUD_ID, 0);
        return values;
    }

    public static ContentValues buildMixItemsRecord(long mixId, MixItem mixitem) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE, mixitem.getMixItemTitle());
        values.put(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL, mixitem.getMixItemLevel());
        values.put(BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY, mixId);
        return values;
    }

    public static ContentValues buildPlaylistRecord(Playlist playlist) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_TITLE, playlist.getPlaylistTitle());
        values.put(BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_SOUNDCLOUD_ID, playlist.getSoundCloudId());
        return values;
    }

    public static ContentValues buildUserRecord(User user) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME, user.getUserName());
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD, Constants.generateEncryptedPass());
        values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID, user.getSoundCloudUserId());
        return values;
    }

    public static ContentValues buildUserFollowingRecord(String bbUserId, String scUserId) {
        ContentValues values = new ContentValues();
        values.put(BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_ID, bbUserId);
        values.put(BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID, scUserId);
        return values;
    }

    public static ContentValues[] buildMixItemsBulkRecord(long mixId, ArrayList<MixItem> mixitems) {
        ContentValues[] contentValues = new ContentValues[mixitems.size()];
        for (int i = 0; i < mixitems.size(); i++) {
            contentValues[i] = buildMixItemsRecord(mixId, mixitems.get(i));
        }
        return contentValues;
    }

    public static Mix buildNewDefaultMixRecord(Context context) {
        Mix defaultMix = new Mix();
        defaultMix.setMixTitle(context.getString(R.string.default_mix_title));
        defaultMix.setMixAlbumCoverArt(context.getString(R.string.default_mix_album_art_url));
        ArrayList<MixItem> defaultMixItems = new ArrayList<>();
        for (int i = 0; i < context.getResources().getStringArray(R.array.default_mix_items).length; i++) {
            MixItem item = new MixItem();
            item.setMixItemTitle(context.getResources().getStringArray(R.array.default_mix_items)[i]);
            item.setMixItemLevel(MIX_ITEM_DEFAULT_LEVEL);
            defaultMixItems.add(item);
        }
        defaultMix.setMixItems(defaultMixItems);

        return defaultMix;
    }

    public static AlertDialog buildRatingDialog(Context context, String title, final ImageAdapter.DialogImageSelectedListener selectionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_image_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ((GridView) dialogView.findViewById(R.id.options_list)).setAdapter(new ImageAdapter(context, selectionListener));
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog buildImageListDialogue(Context context, String title, final ImageAdapter.DialogImageSelectedListener selectionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_image_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ((GridView) dialogView.findViewById(R.id.options_list)).setAdapter(new ImageAdapter(context, selectionListener));
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog buildListDialogue(Context context, String title, int optionsId, AdapterView.OnItemClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.dialog_list_item, R.id.dialog_item, context.getResources().getStringArray(optionsId));
        ((ListView) dialogView.findViewById(R.id.options_list)).setAdapter(adapter);
        ((ListView) dialogView.findViewById(R.id.options_list)).setOnItemClickListener(listener);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog buildConfirmDialog(Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(title);
        builder.setMessage(context.getString(R.string.please_sign_in_to_sound_cloud_dialog));
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("connect to sound cloud", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "set up log in to sound cloud link", Toast.LENGTH_LONG).show();
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

    public static String generateEncryptedPass() {
        return "Password1";
    }

}