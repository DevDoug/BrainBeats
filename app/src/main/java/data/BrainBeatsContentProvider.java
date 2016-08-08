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
public class BrainBeatsContentProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BrainBeatsDbHelper mOpenHelper;
    private static final SQLiteQueryBuilder sMixByIDQueryBuilder;

    static final int MIX                = 100;
    static final int MIXITEM            = 200;
    static final int MIX_RELATED        = 300;
    static final int MIX_PLAYLIST       = 400;
    static final int USER               = 500;
    static final int USER_FOLLOWERS     = 600;
    static final int RAW_QUERY          = 700;

    static {
        sMixByIDQueryBuilder = new SQLiteQueryBuilder();
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BrainBeatsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BrainBeatsContract.PATH_MIX, MIX);
        matcher.addURI(authority, BrainBeatsContract.PATH_MIX_ITEM, MIXITEM);
        matcher.addURI(authority, BrainBeatsContract.PATH_MIX_RELATED, MIX_RELATED);
        matcher.addURI(authority, BrainBeatsContract.PATH_MIX_PLAYLIST, MIX_PLAYLIST);
        matcher.addURI(authority, BrainBeatsContract.PATH_USER, USER);
        matcher.addURI(authority, BrainBeatsContract.PATH_USER_FOLLOWERS, USER_FOLLOWERS);
        matcher.addURI(authority, BrainBeatsContract.PATH_RAW_QUERY, RAW_QUERY);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BrainBeatsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MIX:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BrainBeatsContract.MixEntry.TABLE_NAME,
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
                        BrainBeatsContract.MixItemsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MIX_RELATED:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BrainBeatsContract.MixRelatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MIX_PLAYLIST:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BrainBeatsContract.MixPlaylistEntry.TABLE_NAME,
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
                        BrainBeatsContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USER_FOLLOWERS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BrainBeatsContract.UserFollowersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case RAW_QUERY:
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(selection,selectionArgs);
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
                return BrainBeatsContract.MixEntry.CONTENT_TYPE;
            case MIXITEM:
                return BrainBeatsContract.MixItemsEntry.CONTENT_TYPE;
            case MIX_RELATED:
                return BrainBeatsContract.MixRelatedEntry.CONTENT_TYPE;
            case MIX_PLAYLIST:
                return BrainBeatsContract.MixPlaylistEntry.CONTENT_TYPE;
            case USER:
                return BrainBeatsContract.UserEntry.CONTENT_TYPE;
            case USER_FOLLOWERS:
                return BrainBeatsContract.UserFollowersEntry.CONTENT_TYPE;
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
                long _id = db.insert(BrainBeatsContract.MixEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = BrainBeatsContract.MixEntry.buildMixUriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case MIXITEM:
                long _idMixItem = db.insert(BrainBeatsContract.MixItemsEntry.TABLE_NAME, null, values);
                if (_idMixItem > 0)
                    returnUri = BrainBeatsContract.MixItemsEntry.buildMixItemUriWithId(_idMixItem);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case MIX_RELATED:
                long _idMixRelated = db.insert(BrainBeatsContract.MixRelatedEntry.TABLE_NAME, null, values);
                if (_idMixRelated > 0)
                    returnUri = BrainBeatsContract.MixRelatedEntry.buildRelatedMixesUriWithId(_idMixRelated);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case MIX_PLAYLIST:
                long _idMixPlaylist = db.insert(BrainBeatsContract.MixPlaylistEntry.TABLE_NAME, null, values);
                if (_idMixPlaylist > 0)
                    returnUri = BrainBeatsContract.MixRelatedEntry.buildRelatedMixesUriWithId(_idMixPlaylist);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case USER:
                long _idUser = db.insert(BrainBeatsContract.UserEntry.TABLE_NAME, null, values);
                if (_idUser > 0)
                    returnUri = BrainBeatsContract.UserEntry.buildUserUriWithId(_idUser);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case USER_FOLLOWERS:
                long _idUserFollow = db.insert(BrainBeatsContract.UserFollowersEntry.TABLE_NAME, null, values);
                if (_idUserFollow > 0)
                    returnUri = BrainBeatsContract.UserFollowersEntry.buildUserFollowerUriWithId(_idUserFollow);
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
                        BrainBeatsContract.MixEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MIXITEM:
                rowsDeleted = db.delete(
                        BrainBeatsContract.MixItemsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(
                        BrainBeatsContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_FOLLOWERS:
                rowsDeleted = db.delete(
                        BrainBeatsContract.UserFollowersEntry.TABLE_NAME, selection, selectionArgs);
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
                rowsUpdated = db.update(BrainBeatsContract.MixEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MIXITEM:
                rowsUpdated = db.update(BrainBeatsContract.MixItemsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(BrainBeatsContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
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
                        long _id = db.insert(BrainBeatsContract.MixEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(BrainBeatsContract.MixItemsEntry.TABLE_NAME, null, value);
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