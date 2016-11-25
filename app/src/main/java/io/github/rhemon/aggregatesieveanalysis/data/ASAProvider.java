package io.github.rhemon.aggregatesieveanalysis.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by rhemon on 9/23/16.
 */
public class ASAProvider extends ContentProvider {

    private ASADBHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ASADATA = 100;
    private static final int ASADATA_ID = 101;

    static {
        sUriMatcher.addURI(ASADBContract.CONTENT_AUTHORITY, ASADBContract.ASADATA_PATH, ASADATA);
        sUriMatcher.addURI(ASADBContract.CONTENT_AUTHORITY, ASADBContract.ASADATA_PATH + "/#", ASADATA_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ASADBHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch(match) {
            case ASADATA:
                cursor = db.query(ASADBContract.ASADBEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ASADATA_ID:
                selection = ASADBContract.ASADBEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ASADBContract.ASADBEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    private boolean validValues(ContentValues values) {

        String title = values.getAsString(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Requires a title");
        }

        String jsonobjection = values.getAsString(ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT);
        if (jsonobjection == null) {
            throw new IllegalArgumentException("Requires Data");
        }

        return true;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ASADATA:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                if (validValues(values)) {
                    long id = db.insert(ASADBContract.ASADBEntry.TABLE_NAME, null, values);
                    if (id == -1) {
                        Log.e(getClass().getSimpleName(), "Failed to insert data");
                        return null;
                    }
                    getContext().getContentResolver().notifyChange(uri, null);

                    return ContentUris.withAppendedId(uri, id);
                }
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (match) {
            case ASADATA:
                rowsDeleted = db.delete(ASADBContract.ASADBEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case ASADATA_ID:
                selection = ASADBContract.ASADBEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ASADBContract.ASADBEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE)){
            if (values.getAsString(ASADBContract.ASADBEntry.COLUMN_ASADATA_TITLE) == null){
                throw new IllegalArgumentException("Must have title");
            }
        }
        if (values.containsKey(ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT)){
            if (values.getAsString(ASADBContract.ASADBEntry.COLUMN_ASDADATA_JSONOBJECT) == null) {
                throw new IllegalArgumentException("Data required");
            }
        }
        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(ASADBContract.ASADBEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ASADATA:
                return updatePet(uri, values, selection, selectionArgs);
            case ASADATA_ID:
                selection = ASADBContract.ASADBEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+ uri);

        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ASADATA:
                return ASADBContract.ASADBEntry.CONTENT_LIST_TYPE;
            case ASADATA_ID:
                return ASADBContract.ASADBEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
}
