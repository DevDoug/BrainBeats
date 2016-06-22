package data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by douglas on 5/25/2016.
 */
public class MixDbContentProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MixDbHelper mOpenHelper;
    private static final SQLiteQueryBuilder sMixByIDQueryBuilder;

    static final int MIX      = 100;
    static final int MIXITEM  = 200;
    static final int USER     = 300;

    static {
        sMixByIDQueryBuilder = new SQLiteQueryBuilder();
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MixContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MixContract.PATH_MIX, MIX);
        matcher.addURI(authority, MixContract.PATH_MIX_ITEM, MIXITEM);
        matcher.addURI(authority, MixContract.PATH_USER, USER);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MixDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MIX:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MixContract.MixEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MIXITEM:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MixContract.MixItemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USER:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MixContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MIX:
                return MixContract.MixEntry.CONTENT_TYPE;
            case MIXITEM:
                return MixContract.MixItemsEntry.CONTENT_TYPE;
            case USER:
                return MixContract.UserEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MIX:
                long _id = db.insert(MixContract.MixEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MixContract.MixEntry.buildMixUriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case MIXITEM:
                long _idMixItem = db.insert(MixContract.MixItemsEntry.TABLE_NAME, null, values);
                if (_idMixItem > 0)
                    returnUri = MixContract.MixItemsEntry.buildMixItemUriWithId(_idMixItem);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case USER:
                long _idUser = db.insert(MixContract.UserEntry.TABLE_NAME, null, values);
                if (_idUser > 0)
                    returnUri = MixContract.UserEntry.buildUserUriWithId(_idUser);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MIX:
                rowsDeleted = db.delete(
                        MixContract.MixEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MIXITEM:
                rowsDeleted = db.delete(
                        MixContract.MixItemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(
                        MixContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MIX:
                rowsUpdated = db.update(MixContract.MixEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MIXITEM:
                rowsUpdated = db.update(MixContract.MixItemsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(MixContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MIX:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MixContract.MixEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MIXITEM:
                db.beginTransaction();
                int returnCountMixItem = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MixContract.MixItemsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCountMixItem++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCountMixItem;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
