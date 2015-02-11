package com.leocardz.url.unshortener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SearchUrls;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.leocardz.url.unshortener.db.ActionCreate;
import com.leocardz.url.unshortener.db.DatabaseConstants;
import com.leocardz.url.unshortener.db.Persistence;
import com.leocardz.url.unshortener.labels.Item;
import com.leocardz.url.unshortener.labels.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PMain extends ActionBarActivity {

	private EditText editText, editTextTitlePost, editTextDescriptionPost;
	private Button submitButton;
	private Persistence persistence = Persistence.getInstance();
	private boolean allMarked = false;

	public static final int TYPE_TITLE_PREVIEW = 0;
	public static final int TYPE_PREVIEW = 1;
	public static final int TYPE_TITLE_POST = 2;
	public static final int TYPE_POST = 3;

	private Context context;

	private int currentHistoryPosition = 0, currentRecentPosition = 0;

	private TextCrawler textCrawler;

	private String currentUrl = "";

	private ListView listView;

	private ArrayList<Item> labelItems;
	public ArrayAdapter<Item> labelListAdapter;
	private ArrayList<String> checkBoxHashes;
	private ActionMode actionMode;
	private boolean actionModeIsShowing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		context = this;

		setContentView(R.layout.main);

		setProgressBarIndeterminateVisibility(false);
		
		try { // Init DB
			ActionCreate myActionCreate = new ActionCreate(context);
			myActionCreate.openToWrite();
			myActionCreate.close();
		} catch (Exception e) {
		}

		editText = (EditText) findViewById(R.id.input);
		editTextTitlePost = null;
		editTextDescriptionPost = null;

		/** --- From ShareVia Intent */
		if (getIntent().getExtras() != null) {
			String shareVia = (String) getIntent().getExtras().get(
					Intent.EXTRA_TEXT);
			if (shareVia != null) {
				editText.setText(shareVia);
			}
		}
		if (getIntent().getAction() == Intent.ACTION_VIEW) {
			Uri data = getIntent().getData();
			String scheme = data.getScheme();
			String host = data.getHost();
			List<String> params = data.getPathSegments();
			String builded = scheme + "://" + host + "/";

			for (String string : params) {
				builded += string + "/";
			}

			if (data.getQuery() != null && !data.getQuery().equals("")) {
				builded = builded.substring(0, builded.length() - 1);
				builded += "?" + data.getQuery();
			}

			editText.setText(builded);

		}
		/** --- */

		submitButton = (Button) findViewById(R.id.action_go);

		textCrawler = new TextCrawler();

		listView = (ListView) findViewById(R.id.list_view);

		initSubmitButton();

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (editText.getText().length() > 0)
					submitButton.setEnabled(true);
				else
					submitButton.setEnabled(false);
			}
		});

		if (editText.getText().length() > 0)
			submitButton.setEnabled(true);

		buildList(false);
	}

	private void buildList(boolean rebuild) {
		if (rebuild) {
			labelListAdapter.clear();
			checkBoxHashes.clear();
			labelItems.clear();
		}

		checkBoxHashes = new ArrayList<String>();

		String query = "SELECT * FROM " + DatabaseConstants._URLS
				+ "  ORDER BY " + DatabaseConstants._ID + " DESC";

		labelItems = new ArrayList<Item>(persistence.retrieveLabels(context,
				query));

		if (labelItems.size() > 0)
			labelItems.add(0, new Item(null, false,
					getString(R.string.history), "", "", TYPE_TITLE_POST));

		labelListAdapter = new LabelArrayAdapter(context, labelItems);
		listView.setAdapter(labelListAdapter);

	}

	public void initSubmitButton() {
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				currentUrl = editText.getText().toString();
				currentUrl = findFirstUrl(TextCrawler.extendedTrim(currentUrl));
				textCrawler.makePreview(callback, currentUrl, TextCrawler.NONE);
			}
		});
	}

	private LinkPreviewCallback callback = new LinkPreviewCallback() {

		@Override
		public void onPre() {
			submitButton.setEnabled(false);
			setProgressBarIndeterminateVisibility(true);
			hideSoftKeyboard();
		}

		@Override
		public void onPos(final SourceContent sourceContent, boolean isNull) {

			if (!sourceContent.getFinalUrl().equals("")) {
				String hash = String.valueOf(Calendar.getInstance()
						.getTimeInMillis());

				if (sourceContent.getTitle().equals(""))
					sourceContent.setTitle(currentUrl);

				Item item = new Item(hash, false, sourceContent.getTitle(),
						sourceContent.getFinalUrl(), currentUrl, TYPE_PREVIEW);

				if (!hashRecent()) {
					labelItems.add(0, new Item(null, false,
							getString(R.string.recent), "", "",
							TYPE_TITLE_PREVIEW));
					currentHistoryPosition++;
				}

				persistence.createLabel(context, item);

				labelItems.add(1, item);
				currentHistoryPosition++;
			} else {
				Toast.makeText(context, getString(R.string.error_occurred),
						Toast.LENGTH_LONG).show();
			}

			labelListAdapter.notifyDataSetChanged();

			submitButton.setEnabled(true);
			setProgressBarIndeterminateVisibility(false);
		}
	};

	private boolean hashRecent() {
		for (Item item : labelItems) {
			if (item.getType() == TYPE_TITLE_PREVIEW)
				return true;
		}
		return false;
	}

	/** Hide keyboard */
	private void hideSoftKeyboard() {
		hideSoftKeyboard(editText);
		if (editTextTitlePost != null)
			hideSoftKeyboard(editTextTitlePost);
		if (editTextDescriptionPost != null)
			hideSoftKeyboard(editTextDescriptionPost);
	}

	private void hideSoftKeyboard(EditText editText) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager
				.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.about: {
			startActivity(new Intent(this, About.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			return true;
		}

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class LabelArrayAdapter extends ArrayAdapter<Item> {

		private LayoutInflater inflater;
		private ArrayList<Item> labelList;

		public LabelArrayAdapter(Context context, ArrayList<Item> labelList) {
			super(context, R.layout.post_content, R.id.title, labelList);
			inflater = LayoutInflater.from(context);
			this.labelList = labelList;
		}

		@Override
		public int getViewTypeCount() {
			return 4;
		}

		@Override
		public int getItemViewType(int position) {
			return labelList.get(position).getType();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Item labelItem = labelList.get(position);

			int type = getItemViewType(position);

			CheckBox checkbox;
			TextView title, url, urlShortened;
			ImageButton share, open;

			if (convertView == null) {
				if (type == TYPE_TITLE_POST || type == TYPE_TITLE_PREVIEW)
					convertView = inflater.inflate(R.layout.title, null);
				else
					convertView = inflater.inflate(R.layout.post_content, null);

				checkbox = (CheckBox) convertView
						.findViewById(R.id.list_checkbox);
				title = (TextView) convertView.findViewById(R.id.title);
				url = (TextView) convertView.findViewById(R.id.url);
				urlShortened = (TextView) convertView
						.findViewById(R.id.shortened_url);
				share = (ImageButton) convertView.findViewById(R.id.share);
				open = (ImageButton) convertView.findViewById(R.id.open);

				convertView.setTag(new ViewHolder(checkbox, title, url,
						urlShortened, share, open));
			}

			else {
				ViewHolder viewHolderLabel = (ViewHolder) convertView.getTag();
				checkbox = viewHolderLabel.getCheckbox();
				title = viewHolderLabel.getTitle();
				url = viewHolderLabel.getUrl();
				urlShortened = viewHolderLabel.getUrlShortened();
				share = viewHolderLabel.getShare();
				open = viewHolderLabel.getOpen();
			}

			checkbox.setTag(labelItem);

			checkbox.setChecked(labelItem.isChecked());
			title.setText(labelItem.getTitle());
			url.setText(labelItem.getUrl());
			urlShortened.setText(labelItem.getUrlShortened());

			checkbox.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Item itemLabel = (Item) cb.getTag();
					itemLabel.setChecked(cb.isChecked());

					if (cb.isChecked() == true)
						checkBoxHashes.add(itemLabel.getHash());
					else
						checkBoxHashes.remove(itemLabel.getHash());

					try {

						if (!actionModeIsShowing && checkBoxHashes.size() > 0) {
							actionModeIsShowing = true;
                            ((ActionBarActivity) getContext()).startSupportActionMode(mActionModeCallback);
						}

						if (checkBoxHashes.size() == 0) {
							actionModeIsShowing = false;
							if (actionMode != null)
								actionMode.finish();
						} else
							updateSelected(actionMode);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			share.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent sharingIntent = new Intent(
							Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					sharingIntent.putExtra(Intent.EXTRA_TITLE,
							labelItem.getTitle());
					sharingIntent.putExtra(
							Intent.EXTRA_SUBJECT,
							labelItem.getTitle());
					sharingIntent.putExtra(Intent.EXTRA_TEXT,
							labelItem.getUrl());
					startActivity(Intent.createChooser(sharingIntent,
							getResources().getString(R.string.share_via)));
				}
			});

			open.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(labelItem.getUrl()));
					startActivity(intent);
				}
			});

			return convertView;
		}

		@Override
		public boolean isEnabled(int position) {
			return position != currentRecentPosition
					&& position != currentHistoryPosition;
		}

	}

	private String findFirstUrl(String content) {
		String url = "";

		ArrayList<String> urls = SearchUrls.matches(content, SearchUrls.FIRST);

		if (urls.size() > 0)
			url = urls.get(0);

		return url;
	}

	public void updateSelected(ActionMode actionMode) {
		if (checkBoxHashes.size() == 1)
			actionMode.setTitle(checkBoxHashes.size() + " "
					+ getString(R.string.selected));
		else
			actionMode.setTitle(checkBoxHashes.size() + " "
					+ getString(R.string.selected_));
	}

	public ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			actionMode = mode;
			updateSelected(actionMode);

			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.p_label_context_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.item_select_all:
				allMarked = !allMarked;
				for (Item labelItem : labelItems) {
					if (allMarked) {
						if (!checkBoxHashes.contains(labelItem.getHash())
								&& labelItem.getType() != TYPE_TITLE_POST
								&& labelItem.getType() != TYPE_TITLE_PREVIEW) {
							checkBoxHashes.add(labelItem.getHash());
							labelItem.setChecked(true);
						}
					} else {
						labelItem.setChecked(false);
					}
				}
				if (!allMarked)
					checkBoxHashes.clear();

				labelListAdapter.notifyDataSetChanged();
				updateSelected(mode);
				return true;
			case R.id.item_delete:
				for (String string : checkBoxHashes) {
					persistence.deleteLabel(context, string);
				}
				buildList(true);
				mode.finish();
				return true;
			default:
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionModeIsShowing = false;
			mode = null;
		}
	};
}