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
    public static final String CONTENT_AUTHORITY = "com.brainbeats.mixes";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MIX = "mix";

    /* Inner class that defines the table contents for Mix entries */
    public static final class MixEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MIX).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MIX;

        public static final String TABLE_NAME = "mix";
        public static final String COLUMN_NAME_MIX_ID = "mixid";
        public static final String COLUMN_NAME_MIX_TITLE = "mixitle";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
