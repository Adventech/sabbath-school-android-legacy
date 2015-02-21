/*
 * Copyright (c) 2015 Vitaliy Lim <lim.vitaliy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cryart.sabbathschool.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.adapters.SSAboutMenuAdapter;
import com.cryart.sabbathschool.model.SSAboutMenuItem;
import com.cryart.sabbathschool.util.SSAboutMenu;
import com.cryart.sabbathschool.util.SSConstants;
import com.cryart.sabbathschool.util.SSHelper;

public class SSAboutActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Toolbar _SSToolbar;
    private View _SSStatusBar;
    private ListView _SSAboutMenuList;
    private TextView _SSLoveYourNeighbor;

    private void aboutMenuInstagramClick(){
        try {
            // TODO: tracking
            Intent instagram_intent = new Intent(Intent.ACTION_VIEW);
            instagram_intent.setComponent(new ComponentName("com.instagram.android", "com.instagram.android.activity.UrlHandlerActivity"));
            instagram_intent.setData(Uri.parse(SSConstants.SS_ABOUT_INSTAGRAM_URL));
            startActivity(instagram_intent);
        } catch (Exception e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SSConstants.SS_ABOUT_INSTAGRAM_FALLBACK_URL)));
        }
    }

    private void aboutMenuTwitterClick(){
        try {
            // TODO: trackings
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
            intent.putExtra("user_id", SSConstants.SS_ABOUT_TWITTER_ID);
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SSConstants.SS_ABOUT_TWITTER_FALLBACK_URL)));
        }
    }

    private void aboutMenuPhotoCreditsClick(){
        try {
            // TODO: tracking
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SSConstants.SS_ABOUT_PHOTO_CREDITS_URL)));
        } catch (Exception e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ss_about_activity);

        _SSToolbar = (Toolbar)findViewById(R.id.ss_toolbar);
        _SSStatusBar = findViewById(R.id.ss_status_bar);
        _SSAboutMenuList = (ListView) findViewById(R.id.ss_about_menu_list);
        _SSLoveYourNeighbor = (TextView) findViewById(R.id.ss_about_love_your_neighbor);

        ViewGroup.LayoutParams lp = _SSStatusBar.getLayoutParams();
        lp.height = SSHelper.getStatusBarHeight(this);
        _SSStatusBar.setLayoutParams(lp);

        setSupportActionBar(_SSToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        _SSToolbar.getBackground().setAlpha(255);
        _SSStatusBar.getBackground().setAlpha(255);

        _SSAboutMenuList.setAdapter(new SSAboutMenuAdapter(this, SSAboutMenu.getAboutMenuAsArrayList()));
        _SSAboutMenuList.setOnItemClickListener(this);

        _SSLoveYourNeighbor.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        SSAboutMenuItem _SSAboutMenuItem = (SSAboutMenuItem) adapterView.getAdapter().getItem(position);
        if (_SSAboutMenuItem == SSAboutMenu.SS_ABOUT_MENU_INSTAGRAM){
            this.aboutMenuInstagramClick();
        }

        if (_SSAboutMenuItem == SSAboutMenu.SS_ABOUT_MENU_TWITTER) {
            this.aboutMenuTwitterClick();
        }

        if (_SSAboutMenuItem == SSAboutMenu.SS_ABOUT_MENU_PHOTO_CREDITS) {
            this.aboutMenuPhotoCreditsClick();
        }
    }

    @Override
    public void onClick(View v){
        try {
            // TODO: tracking
            Uri uri = Uri.parse(String.format(SSConstants.SS_WEBVIEW_SEARCH_PROVIDER, getString(R.string.ss_about_love_your_neighbor)));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e){}
    }
}
