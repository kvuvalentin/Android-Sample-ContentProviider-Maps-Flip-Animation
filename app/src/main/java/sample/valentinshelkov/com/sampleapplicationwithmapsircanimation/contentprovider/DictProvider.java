package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class DictProvider extends ContentProvider {
    public static final Uri CONTENT_URI;
    public static final String ID = "_id";
    public static final String WORD = "word";
    private static final String TAG = DictProvider.class.getSimpleName();
    private static final String NAME = DictProvider.class.getName();
    private static final long MAX_ID = 127142;
    public static final String RANDOM = "RANDOM()";
    public static final int MIN_WORDS = 10;
    public static final int MAX_WORDS = 30;
    private SQLiteDatabase dict;

    private static final UriMatcher URI_MATCHER;

    private static final String DB_NAME = "DICT";
    private static final String TABLE_NAME = "words";
    private static final String VND_ANDROID_CURSOR_ITEM = "vnd.android.cursor.item/vnd.";
    private static final String VND_ANDROID_CURSOR_DIR = "vnd.android.cursor.dir/vnd.";

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(NAME, TABLE_NAME, 1);
        URI_MATCHER.addURI(NAME, TABLE_NAME + "/#", 2);
        CONTENT_URI = Uri.parse("content://" + NAME + "/" + TABLE_NAME);
    }

    public DictProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Dictionary is readonly");
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case 1:
                return VND_ANDROID_CURSOR_DIR;
            case 2:
                return VND_ANDROID_CURSOR_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Dictionary is readonly");
    }

    @Override
    public boolean onCreate() {
        String dbPath = getContext().getFilesDir().getAbsolutePath() + File.separator;
        File dictFile = new File(dbPath + DB_NAME);
        if (!dictFile.exists()) {
            try {
                FileUtils.copyInputStreamToFile(getContext().getAssets().open(DB_NAME), dictFile);
            } catch (IOException e) {
                e.printStackTrace();
                Log.w(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
                return false;
            }
        }
        dict = SQLiteDatabase.openDatabase(dictFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Random random = new Random();
        return dict.query(TABLE_NAME, new String[]{WORD}, null, null, null, null, RANDOM, Integer.toString(MIN_WORDS + random.nextInt(MAX_WORDS - MIN_WORDS)));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Dictionary is readonly");
    }
}
