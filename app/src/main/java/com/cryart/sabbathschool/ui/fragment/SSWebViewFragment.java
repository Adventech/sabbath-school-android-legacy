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
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.ui.activity.SSMainActivity;
import com.cryart.sabbathschool.ui.widget.SSWebView;
import com.cryart.sabbathschool.ui.widget.SSSlidingTabLayout;
import com.cryart.sabbathschool.util.SSHelper;

import org.json.JSONObject;

public class SSWebViewFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final int TOPBAR_PADDING = 215;

    private ViewGroup _view;
    private SSWebView _SSWebView;

    private int position;
    private Toolbar _SSToolbar;
    private SSSlidingTabLayout _SSTabs;
    private ImageView _SSHero;
    private View _SSStatusBar;
    private ProgressBar _SSWebViewLoading;


    public static SSWebViewFragment newInstance(int position) {
        SSWebViewFragment f = new SSWebViewFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        Log.v("onCreate", String.valueOf(position));
        if (savedInstanceState != null){
            Log.v("onCreate from bundle", String.valueOf(position));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.v("onCreate from bundle", String.valueOf(position));
        }
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
        _view = (ViewGroup) inflater.inflate(R.layout.ss_webview_fragment, null);

        _SSWebView = (SSWebView) _view.findViewById(R.id.ss_webview);
        _SSWebViewLoading = (ProgressBar) _view.findViewById(R.id.ss_webview_loading);
        _SSWebView.setBackgroundColor(Color.TRANSPARENT);

        _SSToolbar = (Toolbar)getActivity().findViewById(R.id.ss_toolbar);
        _SSTabs = (SSSlidingTabLayout) getActivity().findViewById(R.id.ss_tabs);
        _SSHero = (ImageView) getActivity().findViewById(R.id.ss_hero);
        _SSStatusBar = getActivity().findViewById(R.id.ss_status_bar);

        _SSWebView.addJavascriptInterface(new SSWebInterface(getActivity()), "SS");
        _SSWebView.getSettings().setJavaScriptEnabled(true);
        _SSWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                _SSWebViewLoading.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        });

        _SSWebView.loadDataWithBaseURL(
                "file:///android_asset/",
                SSHelper.readFileFromAssets(getActivity(), "index.html").replaceAll(
                        "<body>",
                        "<body style='padding-top: "+TOPBAR_PADDING+"px'>"
                ),
                "text/html",
                "utf-8",
                null
        );

        _SSWebView.setOnScrollChangedCallback(new SSWebView.OnScrollChangedCallback() {
            @Override
            public void onScrollChanged(int deltaX, int deltaY) {
                int scrollY = _SSWebView.getScrollY() * -1;
                _SSHero.setTranslationY(scrollY * 0.5f);

                int headerHeight = _SSHero.getHeight() - _SSToolbar.getHeight() - _SSStatusBar.getHeight();
                float ratio = (float) Math.min(Math.max(scrollY * -1, 0), headerHeight) / headerHeight;

                _SSToolbar.getBackground().setAlpha((int) (ratio * 255));
                _SSStatusBar.getBackground().setAlpha((int) (ratio * 255));
                int scrollOtherFragments = scrollY * -1;
                if (_SSTabs.getTop() + scrollY < _SSToolbar.getHeight() + _SSStatusBar.getHeight()) {
                    _SSTabs.setY(_SSToolbar.getHeight() + _SSStatusBar.getHeight());
                    scrollOtherFragments = _SSTabs.getBottom() - (_SSToolbar.getHeight() + _SSStatusBar.getHeight() + _SSTabs.getHeight());
                } else {
                    _SSTabs.setTranslationY(scrollY);
                }

                SparseArray<Fragment> registeredFragments = ((SSMainActivity.MyPagerAdapter) ((SSMainActivity) getActivity()).getPager().getAdapter()).registeredFragments;
                for (int i = 0; i < registeredFragments.size(); i++) {
                    if (i == position) continue;
                    ((SSWebViewFragment) (registeredFragments.get(i)))._SSWebView._SSOnScrollChangedCallbackEnabled = false;
                    ((SSWebViewFragment) (registeredFragments.get(i)))._SSWebView.scrollTo(0, scrollOtherFragments);
                    ((SSWebViewFragment) (registeredFragments.get(i)))._SSWebView._SSOnScrollChangedCallbackEnabled = true;
                }
            }
        });

        return _view;
    }


    private class SSWebInterface {
        Context _SSContext;

        SSWebInterface(Context c) {
            _SSContext = c;
        }

        @JavascriptInterface
        public void highlightStart(final String sRect) {
            Log.v("RECT", String.valueOf(sRect));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject rect = new JSONObject(sRect);
                        RelativeLayout rl = (RelativeLayout) _view.findViewById(R.id.ss_webview_holder);
                        ImageView iv = new ImageView(getActivity());

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(90, 20);
                        params.leftMargin = rect.getInt("left");
                        params.topMargin = rect.getInt("top") + 216;
                        iv.setBackgroundColor(Color.RED);
                        rl.addView(iv, params);
                    } catch (Exception e){
                        Log.v("E", e.getMessage());
                    }
                }
            });
        }

        @JavascriptInterface
        public void highlightStop(){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout rl = (RelativeLayout) _view.findViewById(R.id.ss_webview_holder);
                    int count = rl.getChildCount();
                    for (int i = 0; i <= count; i++) {
                        View v = rl.getChildAt(i);
                        if (v instanceof ImageView) {
                            rl.removeView(v);
                        }
                    }
                }
            });
        }
    }

}
