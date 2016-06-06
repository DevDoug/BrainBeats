package data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by douglas on 5/25/2016.
 */
public class MixContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MixContract() {
    }

    // The "Content authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.brainbeats";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MIX        = "mix";
    public static final String PATH_MIX_ITEM   = "mixitem";
    public static final String PATH_USER       = "user";

    /* Inner class that defines the table contents for Mix entries */
    public static final class MixEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MIX).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX;

        public static final String TABLE_NAME                      = "mix";
        public static final String COLUMN_NAME_MIX_TITLE           = "mixtitle";
        public static final String COLUMN_NAME_MIX_ALBUM_ART_URL   = "albumarturl";

        public static Uri buildMixUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MixItemsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MIX_ITEM).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_ITEM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX_ITEM;

        public static final String TABLE_NAME                         = "mixitem";
        public static final String COLUMN_NAME_MIX_ITEM_TITLE         = "mixitemtitle";
        public static final String COLUMN_NAME_MIX_ITEM_LEVEL         = "level";
        public static final String COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY  = "mixitemforeignkey";

        public static Uri buildMixItemUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_USER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME                         = "user";
        public static final String COLUMN_NAME_USER_NAME              = "username";

        public static Uri buildUserUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
