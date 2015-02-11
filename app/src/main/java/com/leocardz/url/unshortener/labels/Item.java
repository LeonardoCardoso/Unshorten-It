package com.leocardz.url.unshortener.labels;

public class Item {
	private String hash;
	private boolean checked = false;
	private String title;
	private String url;
	private String urlShortened;
	private int type;

	public Item(String hash, boolean checked, String title, String url,
			String urlShortened, int type) {
		super();
		this.hash = hash;
		this.checked = checked;
		this.title = title;
		this.url = url;
		this.urlShortened = urlShortened;
		this.setType(type);
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlShortened() {
		return urlShortened;
	}

	public void setUrlShortened(String urlShortened) {
		this.urlShortened = urlShortened;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}