package com.leocardz.url.unshortener.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ActionCreate {

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;

	public ActionCreate(Context c) {
		context = c;
	}

	public synchronized ActionCreate openToWrite()
			throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, DatabaseConstants._DATABASE,
				null, DatabaseConstants._VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public synchronized void close() {
		sqLiteDatabase.close();
		sqLiteHelper.close();
	}

	public synchronized long createInternal(String table,
			Map<String, String> content) {
		ContentValues contentValues = new ContentValues();

		for (HashMap.Entry<String, String> entry : content.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			contentValues.put(key, value);
		}

		return sqLiteDatabase.insert(table, null, contentValues);
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public synchronized void onCreate(SQLiteDatabase db) {
			db.execSQL(DatabaseConstants._CREATE_URLS);
		}

		@Override
		public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
		}

	}

}
