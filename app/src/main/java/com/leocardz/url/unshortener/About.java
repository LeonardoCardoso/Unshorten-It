package com.leocardz.url.unshortener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class About extends ActionBarActivity {

    Activity context;
    Button button;
    Button rateIt, shareIt;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        context = this;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shareIt = (Button) findViewById(R.id.share_us_button);
        shareIt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                shareUs();
            }
        });
        rateIt = (Button) findViewById(R.id.rate_us_button);
        rateIt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                rateUs();
            }
        });

    }

    private void shareUs() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.spread_subject));
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.spread_text));
        startActivity(Intent.createChooser(intent,
                getString(R.string.share_via)));
    }

    private void rateUs() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri
                .parse("market://details?id=com.leocardz.url.unshortener"));
        startActivity(intent);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

}
