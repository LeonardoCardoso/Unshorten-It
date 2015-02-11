package com.leocardz.url.unshortener.db;

public class DatabaseConstants {

	public static final String _ID = "_id";
	public static final String _DATABASE = "us";
	public static final String _URLS = "u";
	public static final int _VERSION = 1;

	public static final String _URLS_ID = "_id";
	public static final String _URLS_HASH = "h";
	public static final String _URLS_TITLE = "title";
	public static final String _URLS_URL = "url";
	public static final String _URLS_SHORTENED_URL = "s_url";

	public static final String _CREATE_URLS = "CREATE TABLE IF NOT EXISTS " + _URLS + "  ("
			+ _URLS_ID + " INTEGER PRIMARY KEY, " + _URLS_HASH
			+ " TEXT UNIQUE, " + _URLS_TITLE + " TEXT, " + _URLS_URL
			+ " TEXT, " + _URLS_SHORTENED_URL + " TEXT " + ")";

}