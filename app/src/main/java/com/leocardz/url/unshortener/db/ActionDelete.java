package com.leocardz.url.unshortener.db;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ActionDelete {

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;

	private Context context;

	public ActionDelete(Context c) {
		context = c;
	}

	public synchronized ActionDelete openToWrite()
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

	public synchronized void rawQuery(String query) {

	}

	public synchronized long deleteInternal(String table,
			Map<String, String> wheres) {

		String where = "";
		for (HashMap.Entry<String, String> entry : wheres.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			where += key + " = '" + value + "' AND ";
		}
		try {
			where = where.substring(0, where.length() - 5);
		} catch (Exception e) {
		}

		return sqLiteDatabase.delete(table, where, null);
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public synchronized void onCreate(SQLiteDatabase db) {
		}

		@Override
		public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
		}

	}

}
