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

package com.cryart.sabbathschool.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class SSWebView extends WebView {
    public final Handler h = new Handler();
    public final int delay = 1000;
    public boolean actionModeOn = false;

    public boolean _SSOnScrollChangedCallbackEnabled = true;

    protected Context _SSContext;
    private OnScrollChangedCallback _SSOnScrollChangedCallback;
    private OnStartActionModeCallback _SSOnStartActionModeCallback;
    private OnTouchCallback _SSOnTouchCallback;

    public SSWebView(final Context context) {
        super(context);
        _SSContext = context;

    }

    public SSWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        _SSContext = context;

    }

    public SSWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        _SSContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        _SSOnTouchCallback.onTouch(event);
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (_SSOnScrollChangedCallback != null && _SSOnScrollChangedCallbackEnabled){
            _SSOnScrollChangedCallback.onScrollChanged(l - oldl, t - oldt, getScrollY() * -1);
        }
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        _SSOnScrollChangedCallback = onScrollChangedCallback;
    }

    public void setOnStartActionModeCallback(final OnStartActionModeCallback onStartActionModeCallback) {
        _SSOnStartActionModeCallback = onStartActionModeCallback;
    }

    public void setOnTouchCallback(final OnTouchCallback onTouchCallback) {
        _SSOnTouchCallback = onTouchCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScrollChanged(int deltaX, int deltaY, int scrollY);
    }

    public static interface OnStartActionModeCallback {
        public void onStartActionMode();
        public void onStopActionMode();
    }

    public static interface OnTouchCallback {
        public void onTouch(MotionEvent event);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        actionModeOn = true;
        _SSOnStartActionModeCallback.onStartActionMode();

        final WebView v = this;


        h.postDelayed(new Runnable(){
            public void run(){
                v.loadUrl("javascript:ss.getCoord()");

                if (actionModeOn) {
                    h.postDelayed(this, delay);
                }
            }
        }, delay);

        return this.emptyActionMode();
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        actionModeOn = true;
        _SSOnStartActionModeCallback.onStartActionMode();

        final WebView v = this;


        h.postDelayed(new Runnable(){
            public void run(){
                v.loadUrl("javascript:ss.getCoord()");
                if (actionModeOn) {
                    h.postDelayed(this, delay);
                }
            }
        }, delay);


        return this.emptyActionMode();
    }

    public ActionMode emptyActionMode() {
        return new ActionMode() {
            @Override public void setTitle(CharSequence title) {}
            @Override public void setTitle(int resId) {}
            @Override public void setSubtitle(CharSequence subtitle) {}
            @Override public void setSubtitle(int resId) {}
            @Override public void setCustomView(View view) {}
            @Override public void invalidate() {
                _SSOnStartActionModeCallback.onStopActionMode();
            }
            @Override public void finish() {}
            @Override public Menu getMenu() { return null; }
            @Override public CharSequence getTitle() { return null; }
            @Override public CharSequence getSubtitle() { return null; }
            @Override public View getCustomView() { return null; }
            @Override public MenuInflater getMenuInflater() { return null; }

        };
    }

}
