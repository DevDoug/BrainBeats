package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by douglas on 5/25/2016.
 */
public class BrainBeatsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 12;
    public static final String DATABASE_NAME = "Mix.db";

    //Common sql statments.
    public static final String CREATE_TABLE                                 = "CREATE TABLE ";
    public static final String INTEGER_PRIMARY_KEY_AUTO_INCREMENT           = " INTEGER PRIMARY KEY AUTOINCREMENT,";
    public static final String COLUMN_TYPE_INT_NULL                         = " INTEGER";
    public static final String COLUMN_TYPE_INT_NOT_NULL                     = " INTEGER NOT NULL";
    public static final String COLUMN_TYPE_TEXT_NULL                        = " TEXT";
    public static final String COLUMN_TYPE_TEXT_NOT_NULL                    = " TEXT NOT NULL";
    public static final String COLUMN_TYPE_BLOB_NULL                        = " BLOB";
    public static final String COMMA_SEPERATOR                              = ",";
    public static final String CREATE_TABLE_TERMINATION_FOREIGN_KEY         = "));";
    public static final String CREATE_TABLE_TERMINATION                     = ");";

    //Query params
    public static final String WHERE_CLAUSE_LIKE                    = " LIKE ?";
    public static final String WHERE_CLAUSE_EQUAL                   = " = ? ";
    public static final String AND_CLAUSE                           = "AND ";
    public static final String OR_CLAUSE                           = "OR ";

    //Db lookup fields
    public static final String DB_TRUE_VALUE                        = "1";

    //Sort Types
    public static final String DB_SORT_TYPE_DESC                    = " DESC";
    public static final String DB_SORT_TYPE_LIMIT_FIVE              = " ROWID LIMIT 5";



    public BrainBeatsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create mix table
        final String CREATE_TABLE_MIX = CREATE_TABLE + BrainBeatsContract.MixEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.MixEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_RATING + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_PLAYLIST_ID + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_USER_ID_FK + COLUMN_TYPE_INT_NOT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_RELATED_MIXES_ID + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_MIXER + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixEntry.COLUMN_NAME_STREAM_URL + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +

                        " FOREIGN KEY (" + BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_USER_ID_FK + ") REFERENCES " +
                        BrainBeatsContract.UserEntry.TABLE_NAME + " (" + BrainBeatsContract.UserEntry._ID +
                        CREATE_TABLE_TERMINATION_FOREIGN_KEY;

        final String CREATE_TABLE_MIX_ITEMS = CREATE_TABLE + BrainBeatsContract.MixItemsEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.MixItemsEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + COLUMN_TYPE_INT_NOT_NULL + COMMA_SEPERATOR +

                        // Set up the Mix Items fk column as a foreign key to movie table.
                        " FOREIGN KEY (" + BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + ") REFERENCES " +
                        BrainBeatsContract.MixEntry.TABLE_NAME + " (" + BrainBeatsContract.MixEntry._ID +
                        CREATE_TABLE_TERMINATION_FOREIGN_KEY;

        final String CREATE_TABLE_RELATED_MIX = CREATE_TABLE + BrainBeatsContract.MixRelatedEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.MixRelatedEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.MixRelatedEntry.COLUMN_NAME_TAG_CLOUD_ID + COLUMN_TYPE_INT_NULL +
                        CREATE_TABLE_TERMINATION;

        final String CREATE_TABLE_PLAYLIST = CREATE_TABLE + BrainBeatsContract.MixPlaylistEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.MixPlaylistEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_SOUNDCLOUD_ID + COLUMN_TYPE_INT_NULL +
                        CREATE_TABLE_TERMINATION;

        final String CREATE_TABLE_USER = CREATE_TABLE + BrainBeatsContract.UserEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.UserEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.UserEntry.COLUMN_NAME_USER_DESCRIPTION + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD + COLUMN_TYPE_TEXT_NOT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PROFILE_IMG + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + COLUMN_TYPE_INT_NULL +
                        CREATE_TABLE_TERMINATION;

        final String CREATE_TABLE_USER_FOLLOWERS = CREATE_TABLE + BrainBeatsContract.UserFollowersEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.UserFollowersEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_ID + COLUMN_TYPE_INT_NOT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.UserFollowersEntry.COLUMN_NAME_USER_FOLLOWER_ID + COLUMN_TYPE_INT_NOT_NULL +
                        CREATE_TABLE_TERMINATION;

        final String CREATE_TABLE_MIX_TAG = CREATE_TABLE + BrainBeatsContract.MixTagEntry.TABLE_NAME + " (" +
                        BrainBeatsContract.MixTagEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        BrainBeatsContract.MixTagEntry.COLUMN_NAME_TAG_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        BrainBeatsContract.MixTagEntry.COLUMN_NAME_MIX_ID + COLUMN_TYPE_INT_NOT_NULL +
                        CREATE_TABLE_TERMINATION;

        db.execSQL(CREATE_TABLE_MIX);
        db.execSQL(CREATE_TABLE_MIX_ITEMS);
        db.execSQL(CREATE_TABLE_RELATED_MIX);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER_FOLLOWERS);
        db.execSQL(CREATE_TABLE_MIX_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.MixEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.MixItemsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.MixRelatedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.MixPlaylistEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.UserFollowersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BrainBeatsContract.UserEntry.TABLE_NAME);

        onCreate(db);
    }
}