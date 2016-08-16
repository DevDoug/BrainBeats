package data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by douglas on 5/25/2016.
 */
public class BrainBeatsContract {

    public BrainBeatsContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.brainbeats";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MIX                 = "mix";
    public static final String PATH_MIX_ITEM            = "mixitem";
    public static final String PATH_USER                = "user";
    public static final String PATH_USER_FOLLOWERS      = "userfollowers";
    public static final String PATH_MIX_RELATED         = "mixrelated";
    public static final String PATH_MIX_PLAYLIST        = "playlist";
    public static final String PATH_RAW_QUERY           = "rawquery";

    public static final Uri CONTENT_URI_RAW_QUERY = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RAW_QUERY).build();


    public static final class MixEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MIX).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX;

        public static final String TABLE_NAME                           = "mix";
        public static final String COLUMN_NAME_MIX_TITLE                = "mixtitle";
        public static final String COLUMN_NAME_MIX_ALBUM_ART_URL        = "albumarturl";
        public static final String COLUMN_NAME_MIX_RATING               = "mixrating";
        public static final String COLUMN_NAME_IS_FAVORITE              = "isfavorite";
        public static final String COLUMN_NAME_MIX_PLAYLIST_ID          = "playlistid";
        public static final String COLUMN_NAME_SOUND_CLOUD_ID           = "soundcloudid";
        public static final String COLUMN_NAME_MIX_USER_ID_FK           = "userid";
        public static final String COLUMN_NAME_RELATED_MIXES_ID         = "relatedtracksforeignkey";
        public static final String COLUMN_NAME_IS_IN_LIBRARY            = "isinlibrary";
        public static final String COLUMN_NAME_IS_IN_MIXER              = "isInMixer";
        public static final String COLUMN_NAME_STREAM_URL               = "mixStreamUrl";

        public static Uri buildMixUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MixItemsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MIX_ITEM).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_ITEM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_ITEM;

        public static final String TABLE_NAME                           = "mixitem";
        public static final String COLUMN_NAME_MIX_ITEM_TITLE           = "mixitemtitle";
        public static final String COLUMN_NAME_MIX_ITEM_LEVEL           = "mixlevel";
        public static final String COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY    = "mixitemforeignkey";

        public static Uri buildMixItemUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MixRelatedEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MIX_RELATED).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_RELATED;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_RELATED;

        public static final String TABLE_NAME                           = "mixesrelated";
        public static final String COLUMN_NAME_TAG_CLOUD_ID             = "tagcloudid";

        public static Uri buildRelatedMixesUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MixPlaylistEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MIX_PLAYLIST).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_PLAYLIST;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_PLAYLIST;

        public static final String TABLE_NAME                           =  "playlists";
        public static final String COLUMN_NAME_PLAYLIST_TITLE           = "playlisttitle";
        public static final String COLUMN_NAME_PLAYLIST_SOUNDCLOUD_ID   = "soundcloudplaylistid";


        public static Uri buildPlaylistUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_USER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME                           = "user";
        public static final String COLUMN_NAME_USER_NAME                = "username";
        public static final String COLUMN_NAME_USER_PASSWORD            = "password";
        public static final String COLUMN_NAME_USER_PROFILE_IMG         = "userprofileimg";
        public static final String COLUMN_NAME_USER_SOUND_CLOUD_ID      = "soundcloudid";

        public static Uri buildUserUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UserFollowersEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_USER_FOLLOWERS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME                           = "userfollowers";
        public static final String COLUMN_NAME_USER_ID                  = "userid";
        public static final String COLUMN_NAME_USER_FOLLOWER_ID         = "userfollowerid";

        public static Uri buildUserFollowerUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
