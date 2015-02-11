package com.leocardz.url.unshortener.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.leocardz.url.unshortener.PMain;
import com.leocardz.url.unshortener.labels.Item;

public class Persistence {

	private static Persistence instance = null;

	public static Persistence getInstance() {
		synchronized (Persistence.class) {
			if (instance == null)
				instance = new Persistence();
			return instance;
		}
	}

	public synchronized boolean verifyDatabase(Context context) {
		try {
			ActionRetrieve myActionRetrieve = new ActionRetrieve(context);
			myActionRetrieve.openToRead();
			String query = "SELECT " + DatabaseConstants._ID + " FROM "
					+ DatabaseConstants._URLS;
			myActionRetrieve.retrieveInternal(query);
			myActionRetrieve.close();
			return true;
		} catch (SQLiteException e) {
			return false;
		}
	}

	public synchronized boolean createLabel(Context context, Item item) {
		try {
			ActionCreate myActionCreate = new ActionCreate(context);

			myActionCreate.openToWrite();

			Map<String, String> urls = new HashMap<String, String>();
			urls.put(DatabaseConstants._URLS_HASH, item.getHash());
			urls.put(DatabaseConstants._URLS_TITLE, item.getTitle());
			urls.put(DatabaseConstants._URLS_URL, item.getUrl());
			urls.put(DatabaseConstants._URLS_SHORTENED_URL,
					item.getUrlShortened());

			myActionCreate.createInternal(DatabaseConstants._URLS, urls);

			myActionCreate.close();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized int retrieveRowsCount(Context context, String query) {
		int rows = 0;
		try {
			ActionRetrieve myActionRetrieve = new ActionRetrieve(context);
			myActionRetrieve.openToRead();
			rows = myActionRetrieve.numRows(query);
			myActionRetrieve.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public synchronized HashMap<String, String> retrieveLabel(Context context,
			String query) {
		HashMap<String, String> label = new HashMap<String, String>();
		try {
			ActionRetrieve myActionRetrieve = new ActionRetrieve(context);
			myActionRetrieve.openToRead();

			List<HashMap<String, String>> labelList = myActionRetrieve
					.retrieveInternal(query);

			label = labelList.get(0);

			myActionRetrieve.close();
		} catch (Exception e) {
		}
		return label;
	}

	public synchronized ArrayList<Item> retrieveLabels(Context context,
			String query) {
		String hash = "";
		String title = "";
		String url = "";
		String shortenedUrl = "";

		ArrayList<Item> labelItems = new ArrayList<Item>();

		try {
			ActionRetrieve myActionRetrieve = new ActionRetrieve(context);
			myActionRetrieve.openToRead();

			List<HashMap<String, String>> labelList = myActionRetrieve
					.retrieveInternal(query);

			int labelListCount = labelList.size();

			for (int i = 0; i < labelListCount; i++) {
				HashMap<String, String> labels = labelList.get(i);
				hash = labels.get(DatabaseConstants._URLS_HASH);
				title = labels.get(DatabaseConstants._URLS_TITLE);
				url = labels.get(DatabaseConstants._URLS_URL);
				shortenedUrl = labels
						.get(DatabaseConstants._URLS_SHORTENED_URL);

				labelItems.add(new Item(hash, false, title, url, shortenedUrl,
						PMain.TYPE_POST));

				hash = "";
				title = "";
				url = "";
				shortenedUrl = "";
			}

			myActionRetrieve.close();
		} catch (Exception e) {
		}

		return labelItems;
	}

	public synchronized boolean deleteLabel(Context context, String data) {
		try {
			ActionDelete myActionDelete = new ActionDelete(context);
			myActionDelete.openToWrite();

			Map<String, String> wheres = new HashMap<String, String>();
			wheres.put(DatabaseConstants._URLS_HASH, data);

			myActionDelete.deleteInternal(DatabaseConstants._URLS, wheres);

			myActionDelete.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
