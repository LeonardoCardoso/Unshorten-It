package com.leocardz.url.unshortener.labels;

import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewHolder {
	private CheckBox checkbox;
	private TextView title;
	private TextView url;
	private TextView urlShortened;
	private ImageButton share;
	private ImageButton open;

	public ViewHolder(CheckBox checkbox, TextView title, TextView url,
			TextView urlShortened, ImageButton share, ImageButton open) {
		super();
		this.checkbox = checkbox;
		this.title = title;
		this.url = url;
		this.urlShortened = urlShortened;
		this.share = share;
		this.open = open;
	}

	public CheckBox getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(CheckBox checkbox) {
		this.checkbox = checkbox;
	}

	public TextView getTitle() {
		return title;
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public TextView getUrl() {
		return url;
	}

	public void setUrl(TextView url) {
		this.url = url;
	}

	public TextView getUrlShortened() {
		return urlShortened;
	}

	public void setUrlShortened(TextView urlShortened) {
		this.urlShortened = urlShortened;
	}

	/**
	 * @return the share
	 */
	public ImageButton getShare() {
		return share;
	}

	/**
	 * @param share
	 *            the share to set
	 */
	public void setShare(ImageButton share) {
		this.share = share;
	}

	/**
	 * @return the open
	 */
	public ImageButton getOpen() {
		return open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(ImageButton open) {
		this.open = open;
	}

}
