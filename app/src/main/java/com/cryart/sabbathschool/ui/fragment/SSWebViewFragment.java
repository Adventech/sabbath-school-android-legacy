/*
 * Copyright (c) 2014 Vitaliy Lim <lim.vitaliy@gmail.com>
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

package com.cryart.sabbathschool.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.ui.activity.SSBibleVerseActivity;
import com.cryart.sabbathschool.ui.activity.SSMainActivity;
import com.cryart.sabbathschool.ui.widget.SSWebView;
import com.cryart.sabbathschool.util.SSConstants;
import com.cryart.sabbathschool.util.SSCore;
import com.cryart.sabbathschool.util.SSHelper;
import com.cryart.sabbathschool.util.SSTracker;
import com.flaviofaria.kenburnsview.KenBurnsView;

public class SSWebViewFragment extends Fragment {
    private SharedPreferences _SSPreferences;

    private String _SSDayDate;
    private SSDay _SSDay;
    private SSCore _SSCore;

    private KenBurnsView _SSHero;
    private ProgressBar _SSWebViewLoading;

    public SSWebView _SSWebView;

    public static SSWebViewFragment newInstance(String _SSDayDate) {
        SSWebViewFragment f = new SSWebViewFragment();
        Bundle b = new Bundle();
        b.putString(SSConstants.SS_DAY_DATE_ARG, _SSDayDate);
        f.setArguments(b);
        return f;
    }

    public void loadDay(String _SSDayDate){
        _SSWebView.setVisibility(View.INVISIBLE);
        _SSWebViewLoading.setVisibility(View.VISIBLE);
        _SSDay = _SSCore.ssGetDay(_SSDayDate);
        _SSHero.setImageBitmap(SSHelper.getBitmapFromBase64(_SSDay._lesson_image));
        ((SSMainActivity)getActivity()).setHeroView();

        _SSWebView.loadDataWithBaseURL(SSConstants.SS_WEBAPP_PATH_PREFIX,
                SSHelper.readFileFromAssets(getActivity(), SSConstants.SS_WEBAPP_MAIN)
                    .replaceAll("\\{\\{platform\\}\\}", SSConstants.SS_WEBAPP_PLATFORM)
                    .replaceAll(SSConstants.SS_SETTINGS_TEXT_SIZE_DEFAULT_VALUE, _SSPreferences.getString(SSConstants.SS_SETTINGS_TEXT_SIZE_KEY, SSConstants.SS_SETTINGS_TEXT_SIZE_DEFAULT_VALUE))
                    .replaceAll(SSConstants.SS_SETTINGS_READING_MODE_DEFAULT_VALUE, _SSPreferences.getString(SSConstants.SS_SETTINGS_READING_MODE_KEY, SSConstants.SS_SETTINGS_READING_MODE_DEFAULT_VALUE))
                    .replaceAll("<div class=\"wrapper\">", "<div class=\"wrapper\" style=\"margin-top: " + (SSConstants.TOPBAR_PADDING - SSHelper.convertPixelsToDp(getActivity(), SSHelper.getStatusBarHeight(getActivity()))) + "px\">" + _SSDay._day_text),
                "text/html", "utf-8", null);
    }

    protected void setHighlights(String highlights){
        _SSWebView.loadUrl(String.format("javascript:ss.setHighlight('%s');", highlights));
    }

    protected void setComments(String comments){
        _SSWebView.loadUrl(String.format("javascript:ss.setComments('%s');", comments));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _SSDayDate = getArguments().getString(SSConstants.SS_DAY_DATE_ARG);
        _SSCore = SSCore.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.ss_webview_fragment, null);

        _SSPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        _SSWebView = (SSWebView) _view.findViewById(R.id.ss_webview);
        _SSWebViewLoading = (ProgressBar) _view.findViewById(R.id.ss_webview_loading);
        _SSWebView.setBackgroundColor(Color.TRANSPARENT);

        _SSHero = (KenBurnsView) getActivity().findViewById(R.id.ss_hero);

        _SSWebView.addJavascriptInterface(new SSWebInterface(getActivity()), "SSBridge");
        _SSWebView.getSettings().setJavaScriptEnabled(true);
        _SSWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                _SSWebViewLoading.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);

                setHighlights(_SSDay._day_highlights);
                setComments(_SSDay._day_comments);
            }
        });

        this.loadDay(_SSDayDate);

        _SSWebView.setOnScrollChangedCallback((SSMainActivity)getActivity());
        ((AppCompatActivity)getActivity()).getDelegate().setHandleNativeActionModesEnabled(false);

        return _view;
    }

    private class SSWebInterface {
        Context _SSContext;
        SSWebInterface(Context c) {
            _SSContext = c;
        }

        @JavascriptInterface
        public void openBible(final String verse){
            try {
                Intent _SSBibleActivityIntent = new Intent(getActivity().getApplicationContext(), SSBibleVerseActivity.class);
                _SSBibleActivityIntent.putExtra(SSConstants.SS_BIBLE_VERSE_ACTIVITY_ARGUMENT, _SSDay._day_verses_parsed.getString(verse));
                startActivity(_SSBibleActivityIntent);

                SSTracker.sendOpenBibleEvent(getActivity());
            } catch (Exception e){}
        }

        @JavascriptInterface
        public void saveHighlights(String highlights){
            try {
                SSTracker.sendSelectionHighlightEvent(getActivity());
                _SSCore.ssSaveHighlights(_SSDay._serial, highlights);
            } catch (Exception e){}
        }

        @JavascriptInterface
        public void saveComments(String comments){
            try {
                SSTracker.sendCommentSaveEvent(getActivity());
                _SSCore.ssSaveComments(_SSDay._serial, comments);
            } catch (Exception e){}
        }

        @JavascriptInterface
        public void copy(String selection){
            try {
                SSTracker.sendSelectionCopyEvent(getActivity());
                ClipboardManager _clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(SSConstants.SS_WEBVIEW_CLIPBOARD_LABEL, selection);
                _clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.ss_webview_menu_copied), Toast.LENGTH_LONG).show();
            } catch (Exception e){}
        }

        @JavascriptInterface
        public void search(String selection){
            try {
                SSTracker.sendSelectionSearchEvent(getActivity());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(SSConstants.SS_WEBVIEW_SEARCH_PROVIDER, selection)));
                startActivity(intent);
            } catch (Exception e){}
        }

        @JavascriptInterface
        public void share(String selection){
            try {
                SSTracker.sendSelectionShareEvent(getActivity());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, selection);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.ss_webview_menu_share_text_to)));
            } catch (Exception e){}
        }
    }

}
