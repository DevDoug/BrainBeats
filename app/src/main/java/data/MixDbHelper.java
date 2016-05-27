package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.Mix;

/**
 * Created by douglas on 5/25/2016.
 */
public class MixDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
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
                        MixContract.MixEntry.COLUMN_NAME_MIX_TITLE       + COLUMN_TYPE_TEXT_NULL + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_ALPHA_LEVEL + COLUMN_TYPE_INT_NULL  + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_BETA_LEVEL  + COLUMN_TYPE_INT_NULL  + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_GAMMA_LEVEL + COLUMN_TYPE_INT_NULL  + COMMA_SEPERATOR +
                        MixContract.MixEntry.COLUMN_NAME_MIX_THETA_LEVEL + COLUMN_TYPE_INT_NULL  +
                        " );";

        db.execSQL(CREATE_TABLE_MIX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MixContract.MixEntry.TABLE_NAME);
        onCreate(db);
    }
}
