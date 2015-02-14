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
import android.util.AttributeSet;
import android.webkit.WebView;

public class SSWebView extends WebView {
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
            _SSOnScrollChangedCallback.onScrollChanged(l - oldl, t - oldt);
        }
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        _SSOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScrollChanged(int deltaX, int deltaY);
    }

    @Override
    public boolean performLongClick() {

        // TODO: show / hide selection
        return true;
    }

}
