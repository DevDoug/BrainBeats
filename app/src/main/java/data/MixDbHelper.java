package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by douglas on 5/25/2016.
 */
public class MixDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "Mix.db";

    //Common sql statments.
    public static final String CREATE_TABLE                         = "CREATE TABLE ";
    public static final String INTEGER_PRIMARY_KEY_AUTO_INCREMENT   = " INTEGER PRIMARY KEY AUTOINCREMENT,";
    public static final String COLUMN_TYPE_INT_NULL                 = " INTEGER";
    public static final String COLUMN_TYPE_INT_NOT_NULL             = " INTEGER";
    public static final String COLUMN_TYPE_TEXT_NULL                = " TEXT";
    public static final String COLUMN_TYPE_TEXT_NOT_NULL            = " TEXT NOT NULL";
    public static final String COMMA_SEPERATOR                      = ",";

    //Query params
    public static final String WHERE_CLAUSE_LIKE                    = "LIKE ?";
    public static final String WHERE_CLAUSE_EQUAL                   = " = ? ";

    //Db lookup fields
    public static final String DB_ID_FIELD                          = "_id";
    public static final String DB_SC_ID_FIELD                       = "_sc";

    //Sort Types
    public static final String DB_SORT_TYPE_DESC                    = " DESC";

    public MixDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create mix table
        final String CREATE_TABLE_MIX = CREATE_TABLE + MixContract.MixEntry.TABLE_NAME + " (" +
                        MixContract.MixEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        MixContract.MixEntry.COLUMN_NAME_MIX_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_RATING + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_IS_FAVORITE + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_PLAYLIST_ID + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_SOUND_CLOUD_ID + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_USER_ID + COLUMN_TYPE_INT_NOT_NULL + COMMA_SEPERATOR +

                        // Set up the Mix Items fk column as a foreign key to movie table.
                        " FOREIGN KEY (" + MixContract.MixEntry.COLUMN_NAME_MIX_USER_ID + ") REFERENCES " +
                        MixContract.UserEntry.TABLE_NAME + " (" + MixContract.UserEntry._ID + "));";

        final String CREATE_TABLE_MIX_ITEMS = CREATE_TABLE + MixContract.MixItemsEntry.TABLE_NAME + " (" +
                        MixContract.MixItemsEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + COLUMN_TYPE_INT_NOT_NULL + COMMA_SEPERATOR +

                        // Set up the Mix Items fk column as a foreign key to movie table.
                        " FOREIGN KEY (" + MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + ") REFERENCES " +
                        MixContract.MixEntry.TABLE_NAME + " (" + MixContract.MixEntry._ID + "));";

        final String CREATE_TABLE_RELATED_MIX = CREATE_TABLE + MixContract.MixRelatedEntry.TABLE_NAME + " (" +
                        MixContract.MixRelatedEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        MixContract.MixRelatedEntry.COLUMN_NAME_MIX_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixRelatedEntry.COLUMN_NAME_MIX_ID_FOREIGN_KEY + COLUMN_TYPE_INT_NOT_NULL + COMMA_SEPERATOR +

                        " FOREIGN KEY (" + MixContract.MixRelatedEntry.COLUMN_NAME_MIX_ID_FOREIGN_KEY + ") REFERENCES " +
                        MixContract.MixEntry.TABLE_NAME + " (" + MixContract.MixEntry._ID + "));";

        final String CREATE_TABLE_PLAYLIST = CREATE_TABLE + MixContract.MixPlaylistEntry.TABLE_NAME + " (" +
                        MixContract.MixPlaylistEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        MixContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_TITLE + COLUMN_TYPE_TEXT_NULL +
                        " );";

        final String CREATE_TABLE_USER = CREATE_TABLE + MixContract.UserEntry.TABLE_NAME + " (" +
                        MixContract.UserEntry._ID + INTEGER_PRIMARY_KEY_AUTO_INCREMENT +
                        MixContract.UserEntry.COLUMN_NAME_USER_NAME + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.UserEntry.COLUMN_NAME_USER_PASSWORD + COLUMN_TYPE_TEXT_NOT_NULL + COMMA_SEPERATOR +
                        MixContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID + COLUMN_TYPE_TEXT_NULL +
                        " );";

        db.execSQL(CREATE_TABLE_MIX);
        db.execSQL(CREATE_TABLE_MIX_ITEMS);
        db.execSQL(CREATE_TABLE_RELATED_MIX);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.MixEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.MixItemsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.MixRelatedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.MixPlaylistEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
}