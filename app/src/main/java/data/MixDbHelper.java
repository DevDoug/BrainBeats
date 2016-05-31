package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.Mix;

/**
 * Created by douglas on 5/25/2016.
 */
public class MixDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Mix.db";

    public static final String COLUMN_TYPE_INT_NULL       = " INTEGER";
    public static final String COLUMN_TYPE_INT_NOT_NULL   = " INTEGER";
    public static final String COLUMN_TYPE_TEXT_NULL      = " TEXT";
    public static final String COLUMN_TYPE_TEXT_NOT_NULL  = " TEXT NOT NULL";
    public static final String COMMA_SEPERATOR            = ",";

    public MixDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create mix table
        final String CREATE_TABLE_MIX = "CREATE TABLE " + MixContract.MixEntry.TABLE_NAME + " (" +
                        MixContract.MixEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MixContract.MixEntry.COLUMN_NAME_MIX_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL + COLUMN_TYPE_TEXT_NULL +
                        " );";

        final String CREATE_TABLE_MIX_ITEMS = "CREATE TABLE " + MixContract.MixItemsEntry.TABLE_NAME + " (" +
                        MixContract.MixItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL + COLUMN_TYPE_INT_NULL + COMMA_SEPERATOR +
                        MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + " INTEGER NOT NULL," +


                // Set up the Mix Items fk column as a foreign key to movie table.
                        " FOREIGN KEY (" + MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + ") REFERENCES " +
                        MixContract.MixEntry.TABLE_NAME + " (" + MixContract.MixEntry._ID + "));";

        db.execSQL(CREATE_TABLE_MIX);
        db.execSQL(CREATE_TABLE_MIX_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.MixEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + MixContract.MixItemsEntry.TABLE_NAME);
        onCreate(db);
    }
}