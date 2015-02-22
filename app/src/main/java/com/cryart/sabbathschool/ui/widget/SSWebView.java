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
import android.view.ActionMode;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewParent;
import android.webkit.WebView;

import com.cryart.sabbathschool.R;

public class SSWebView extends WebView {
    private ActionMode _SSActionMode;
    private ActionMode.Callback _SSActionModeCallback;
    public boolean _SSOnScrollChangedCallbackEnabled = true;

    protected Context _SSContext;
    private OnScrollChangedCallback _SSOnScrollChangedCallback;

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
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (_SSOnScrollChangedCallback != null && _SSOnScrollChangedCallbackEnabled){
            _SSOnScrollChangedCallback.onScrollChanged(l - oldl, t - oldt, getScrollY() * -1);
        }
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        _SSOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScrollChanged(int deltaX, int deltaY, int scrollY);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ViewParent parent = getParent();
        if (parent == null) {
            return null;
        }
        _SSActionModeCallback = new SSActionModeCallback();
        return parent.startActionModeForChild(this, _SSActionModeCallback);
    }

    private class SSActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.ss_webview_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ss_webview_menu_highlight:
                    loadUrl("javascript:ss.highlight();");
                    break;
                case R.id.ss_webview_menu_highlight_remove:
                    loadUrl("javascript:ss.unHighlight();");
                    break;
                case R.id.ss_webview_menu_copy:
                    loadUrl("javascript:ss.copy();");
                    break;
                case R.id.ss_webview_menu_share:
                    loadUrl("javascript:ss.share();");
                    break;
                case R.id.ss_webview_menu_search:
                    loadUrl("javascript:ss.search();");
                    break;
                default:
                    return false;
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            clearFocus();
            _SSActionMode = null;
        }
    }
}
