package com.leocardz.url.unshortener.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ActionRetrieve {

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;

	public ActionRetrieve(Context c) {
		context = c;
	}

	public synchronized ActionRetrieve openToRead()
			throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, DatabaseConstants._DATABASE,
				null, DatabaseConstants._VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public synchronized void close() {
		sqLiteDatabase.close();
		sqLiteHelper.close();
	}

	public synchronized int numRows(String string) {
		Cursor cursor = sqLiteDatabase.rawQuery(string, null);
		int rows = cursor.getCount();
		cursor.close();
		return rows;
	}

	public synchronized List<HashMap<String, String>> retrieveInternal(
			String string) {

		Cursor cursor = sqLiteDatabase.rawQuery(string, null);

		List<HashMap<String, String>> insideDataList = new ArrayList<HashMap<String, String>>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			HashMap<String, String> insideData = new HashMap<String, String>();

			for (int i = 0; i < cursor.getColumnCount(); i++)
				insideData.put(cursor.getColumnName(i), cursor.getString(i));

			insideDataList.add(insideData);
		}

		cursor.close();
		return insideDataList;
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
