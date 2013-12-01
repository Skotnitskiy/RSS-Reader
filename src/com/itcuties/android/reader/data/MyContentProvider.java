package com.itcuties.android.reader.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log; 

public class MyContentProvider extends ContentProvider {
  final String LOG_TAG = "myLogs";

  // // Константы для БД
  // БД
  static final String DB_NAME = "rssdb";
  static final int DB_VERSION = 1;

  // Таблица
  static final String LIKES_TABLE = "likes";

  // Поля
  static final String LIKE_ID = "_id";
  static final String LIKE_ARTICLE = "article";

  // Скрипт создания таблицы
  static final String DB_CREATE = "create table " + LIKES_TABLE + "("
      + LIKE_ID + " integer primary key autoincrement, "
      + LIKE_ARTICLE + " text, "+ ");";

  // // Uri
  // authority
  static final String AUTHORITY = "com.itcuties.android.reader.data";

  // path
  static final String LIKES_PATH = "likes";

  // Общий Uri
  public static final Uri LIKES_CONTENT_URI = Uri.parse("content://"
      + AUTHORITY + "/" + LIKES_PATH);

  // Типы данных
  // набор строк
  static final String LIKES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
      + AUTHORITY + "." + LIKES_PATH;

  // одна строка
  static final String LIKES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
      + AUTHORITY + "." + LIKES_PATH;

  //// UriMatcher
  // общий Uri
  static final int URI_LIKES = 1;

  // Uri с указанным ID
  static final int URI_LIKES_ID = 2;

  // описание и создание UriMatcher
  private static final UriMatcher uriMatcher;
  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(AUTHORITY, LIKES_PATH, URI_LIKES);
    uriMatcher.addURI(AUTHORITY, LIKES_PATH + "/#", URI_LIKES_ID);
  }

  DBHelper dbHelper;
  SQLiteDatabase db;

  public boolean onCreate() {
    Log.d(LOG_TAG, "onCreate");
    dbHelper = new DBHelper(getContext());
    return true;
  }

  // чтение
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    Log.d(LOG_TAG, "query, " + uri.toString());
    // проверяем Uri
    switch (uriMatcher.match(uri)) {
    case URI_LIKES: // общий Uri
      Log.d(LOG_TAG, "URI_LIKES");
      // если сортировка не указана, ставим свою - по id
      if (TextUtils.isEmpty(sortOrder)) {
        sortOrder = LIKE_ID + " ASC";
      }
      break;
    case URI_LIKES_ID: // Uri с ID
      String id = uri.getLastPathSegment();
      Log.d(LOG_TAG, "URI_LIKES_ID, " + id);
      // добавляем ID к условию выборки
      if (TextUtils.isEmpty(selection)) {
        selection = LIKE_ID + " = " + id;
      } else {
        selection = selection + " AND " + LIKE_ID + " = " + id;
      }
      break;
    default:
      throw new IllegalArgumentException("Wrong URI: " + uri);
    }
    db = dbHelper.getWritableDatabase();
    Cursor cursor = db.query(LIKES_TABLE, projection, selection,
        selectionArgs, null, null, sortOrder);
    // просим ContentResolver уведомлять этот курсор 
    // об изменениях данных в LIKES_CONTENT_URI
    cursor.setNotificationUri(getContext().getContentResolver(),
        LIKES_CONTENT_URI);
    return cursor;
  }

  public Uri insert(Uri uri, ContentValues values) {
    Log.d(LOG_TAG, "insert, " + uri.toString());
    if (uriMatcher.match(uri) != URI_LIKES)
      throw new IllegalArgumentException("Wrong URI: " + uri);

    db = dbHelper.getWritableDatabase();
    long rowID = db.insert(LIKES_TABLE, null, values);
    Uri resultUri = ContentUris.withAppendedId(LIKES_CONTENT_URI, rowID);
    // уведомляем ContentResolver, что данные по адресу resultUri изменились
    getContext().getContentResolver().notifyChange(resultUri, null);
    return resultUri;
  }

  public int delete(Uri uri, String selection, String[] selectionArgs) {
    Log.d(LOG_TAG, "delete, " + uri.toString());
    switch (uriMatcher.match(uri)) {
    case URI_LIKES:
      Log.d(LOG_TAG, "URI_CONTACTS");
      break;
    case URI_LIKES_ID:
      String id = uri.getLastPathSegment();
      Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
      if (TextUtils.isEmpty(selection)) {
        selection = LIKE_ID + " = " + id;
      } else {
        selection = selection + " AND " + LIKE_ID + " = " + id;
      }
      break;
    default:
      throw new IllegalArgumentException("Wrong URI: " + uri);
    }
    db = dbHelper.getWritableDatabase();
    int cnt = db.delete(LIKES_TABLE, selection, selectionArgs);
    getContext().getContentResolver().notifyChange(uri, null);
    return cnt;
  }

  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {
    Log.d(LOG_TAG, "update, " + uri.toString());
    switch (uriMatcher.match(uri)) {
    case URI_LIKES:
      Log.d(LOG_TAG, "URI_CONTACTS");

      break;
    case URI_LIKES_ID:
      String id = uri.getLastPathSegment();
      Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
      if (TextUtils.isEmpty(selection)) {
        selection = LIKE_ID + " = " + id;
      } else {
        selection = selection + " AND " + LIKE_ID + " = " + id;
      }
      break;
    default:
      throw new IllegalArgumentException("Wrong URI: " + uri);
    }
    db = dbHelper.getWritableDatabase();
    int cnt = db.update(LIKES_TABLE, values, selection, selectionArgs);
    getContext().getContentResolver().notifyChange(uri, null);
    return cnt;
  }

  public String getType(Uri uri) {
    Log.d(LOG_TAG, "getType, " + uri.toString());
    switch (uriMatcher.match(uri)) {
    case URI_LIKES:
      return LIKES_CONTENT_TYPE;
    case URI_LIKES_ID:
      return LIKES_CONTENT_ITEM_TYPE;
    }
    return null;
  }

  private class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DB_CREATE);
      //ContentValues cv = new ContentValues();
      
       // cv.put(LIKE_ARTICLE, "article " + i);
       // db.insert(LIKES_TABLE, null, cv);
    
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
  }
}