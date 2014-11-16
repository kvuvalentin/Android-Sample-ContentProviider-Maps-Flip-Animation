package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

public class IrcContentProvider extends ContentProvider {
    public static final String NAME;
    public static final UriMatcher URI_MATCHER;
    public static final int TABLE_NAME_INDEX = 0;
    public static final int ID_INDEX = 1;
    public static final Uri CONTENT_URI;
    public static final String TEXT;
    public static final String GRAVITY;
    public static final String COLOR;
    public static final String USER_COLOR;
    public static final String USER_NAME;
    public static final String ID;
    public static final Cursor NULL_OBJECT_CURSOR;
    private static final String VND_ANDROID_CURSOR_ITEM;
    private static final String VND_ANDROID_CURSOR_DIR;
    private ChatDBHelper chatDbHelper;
    private SQLiteDatabase database;


    static {
        NAME = IrcContentProvider.class.getName();
        VND_ANDROID_CURSOR_ITEM = "vnd.android.cursor.item/vnd.";
        VND_ANDROID_CURSOR_DIR = "vnd.android.cursor.dir/vnd.";
        TEXT = ChatDBHelper.TEXT;
        GRAVITY = ChatDBHelper.GRAVITY;
        COLOR = ChatDBHelper.COLOR;
        USER_COLOR = ChatDBHelper.USER_COLOR;
        USER_NAME = ChatDBHelper.USER_NAME;
        ID = ChatDBHelper.ID;
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(NAME, ChatDBHelper.TABLE_NAME, 1);
        URI_MATCHER.addURI(NAME, ChatDBHelper.TABLE_NAME + "/#", 2);
        CONTENT_URI = Uri.parse("content://" + NAME + "/" + ChatDBHelper.TABLE_NAME);
        NULL_OBJECT_CURSOR = new Cursor() {
            @Override
            public int getCount() {
                return -1;
            }

            @Override
            public int getPosition() {
                return -1;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                throw new IllegalArgumentException("This is null-object cursor");
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {
                throw new IllegalStateException("This is null-object cursor");
            }

            @Override
            public Uri getNotificationUri() {
                return Uri.EMPTY;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public Bundle getExtras() {
                Bundle bundle = new Bundle();
                bundle.putCharSequence("NULL-OBJECT CURSOR", "NULL-OBJECT CURSOR");
                return bundle;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
    }

    //Required default constructor
    public IrcContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == -1) return 0;
        return database.delete(ChatDBHelper.TABLE_NAME, selection, selectionArgs);
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
        if (URI_MATCHER.match(uri) == -1) return Uri.EMPTY;
        long id = database.insertOrThrow(ChatDBHelper.TABLE_NAME, null, values);
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(itemUri, null);
        return id != -1 ? itemUri : null;
    }

    @Override
    public boolean onCreate() {
        chatDbHelper = new ChatDBHelper(getContext());
        database = chatDbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (uri == null || URI_MATCHER.match(uri) == -1) {
            return NULL_OBJECT_CURSOR;
        }
        Args args = getArgs(uri);
        if (args == null) return null;
        Cursor c = database.query(args.tableName, null, args.where, null, null, null, null, null);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uri == null || URI_MATCHER.match(uri) == -1) return 0;
        Args args = getArgs(uri);
        if (args == null) return 0;
        return database.update(args.tableName, values, args.where, null);
    }

    private Args getArgs(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        Args args = new Args();
        switch (pathSegments.size()) {
            case 1:
                args.tableName = pathSegments.get(TABLE_NAME_INDEX);
                break;
            case 2:
                args.tableName = pathSegments.get(TABLE_NAME_INDEX);
                args.where = ChatDBHelper.ID + "=" + pathSegments.get(ID_INDEX);
                break;
            default:
                return null;
        }
        return args;
    }

    @Override
    public void shutdown() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    private static class ChatDBHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "chat_messages";
        private static final String TABLE_NAME = "messages";
        public static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private static final String TEXT = "text";
        private static final String GRAVITY = "gravity";
        private static final String COLOR = "color";
        private static final String ID = "_id";
        public static final String USER_COLOR = "user_color";
        public static final String USER_NAME = "user_name";
        private static final String CREATE_TABLE = "create table " + TABLE_NAME + " " +
                "(" + ID + " integer primary key autoincrement," + TEXT + " text,"
                + GRAVITY + " integer," + COLOR + " integer," + USER_COLOR + " integer,"
                + USER_NAME + " text)";

        public ChatDBHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_IF_EXISTS);
            onCreate(db);
        }
    }

    private static class Args {
        String tableName;
        String where;
    }
}
