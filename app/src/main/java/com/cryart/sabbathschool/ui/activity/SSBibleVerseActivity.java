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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.util.SSConstants;
import com.cryart.sabbathschool.util.SSHelper;

public class SSBibleVerseActivity extends Activity {
    private ProgressBar _SSLoading;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.ss_bible_verse_activity);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageButton _SSCloseButton = (ImageButton) findViewById(R.id.ss_bible_verse_activity_close);
        _SSLoading = (ProgressBar) findViewById(R.id.ss_bible_verse_activity_loading);
        _SSLoading.setVisibility(View.VISIBLE);

        _SSCloseButton.setOnClickListener(this.onCloseClick(this));

        String _SSBibleDayVerses;
        WebView _SSBibleVerseContent;
        SharedPreferences _SSPreferences;

        _SSPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        _SSBibleDayVerses = getIntent().getExtras().getString(SSConstants.SS_BIBLE_VERSE_ACTIVITY_ARGUMENT);
        _SSBibleVerseContent = (WebView) findViewById(R.id.ss_bible_verse_activity_content);

        _SSBibleVerseContent.loadDataWithBaseURL(SSConstants.SS_WEBAPP_PATH_PREFIX,
                SSHelper.readFileFromAssets(this, SSConstants.SS_WEBAPP_BIBLE)
                    .replaceAll("\\{\\{platform\\}\\}", SSConstants.SS_WEBAPP_PLATFORM)
                    .replaceAll("small.css", _SSPreferences.getString(SSConstants.SS_SETTINGS_TEXT_SIZE_KEY, SSConstants.SS_SETTINGS_TEXT_SIZE_DEFAULT_VALUE))
                    .replaceAll("<div class=\"wrapper\">", "<div class=\"wrapper\">" + _SSBibleDayVerses),
                "text/html", "utf-8", null);

        _SSBibleVerseContent.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                _SSLoading.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        });
    }


    public View.OnClickListener onCloseClick(final Activity context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        };
    }
}
