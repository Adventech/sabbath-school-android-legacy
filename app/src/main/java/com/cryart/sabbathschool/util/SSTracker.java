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

package com.cryart.sabbathschool.util;

import android.app.Activity;

import com.cryart.sabbathschool.SSApplication;
import com.google.android.gms.analytics.HitBuilders;

public class SSTracker {
    private static final String GA_BIBLE_VERSE_CATEGORY = "Bible Verse";
    private static final String GA_BIBLE_VERSE_OPEN = "Open";

    private static final String GA_COMMENT_CATEGORY = "Comment";
    private static final String GA_COMMENT_SAVE = "Save";

    private static final String GA_SELECTION_CATEGORY = "Selection";
    private static final String GA_SELECTION_HIGHLIGHT = "Highlight";
    private static final String GA_SELECTION_SEARCH = "Search";
    private static final String GA_SELECTION_SHARE = "Share";
    private static final String GA_SELECTION_COPY = "Copy";

    public static void sendOpenBibleEvent(Activity context){
        ((SSApplication) context.getApplication()).getTracker().send(
            new HitBuilders.EventBuilder()
                .setCategory(GA_BIBLE_VERSE_CATEGORY)
                .setAction(GA_BIBLE_VERSE_OPEN)
                .build()
        );
    }

    public static void sendCommentSaveEvent(Activity context){
        ((SSApplication) context.getApplication()).getTracker().send(
            new HitBuilders.EventBuilder()
                .setCategory(GA_COMMENT_CATEGORY)
                .setAction(GA_COMMENT_SAVE)
                .build()
        );
    }

    public static void sendSelectionHighlightEvent(Activity context){
        ((SSApplication) context.getApplication()).getTracker().send(
            new HitBuilders.EventBuilder()
                .setCategory(GA_SELECTION_CATEGORY)
                .setAction(GA_SELECTION_HIGHLIGHT)
                .build()
        );
    }

    public static void sendSelectionSearchEvent(Activity context){
        ((SSApplication) context.getApplication()).getTracker().send(
            new HitBuilders.EventBuilder()
                .setCategory(GA_SELECTION_CATEGORY)
                .setAction(GA_SELECTION_SEARCH)
                .build()
        );
    }

    public static void sendSelectionShareEvent(Activity context){
        ((SSApplication) context.getApplication()).getTracker().send(
            new HitBuilders.EventBuilder()
                .setCategory(GA_SELECTION_CATEGORY)
                .setAction(GA_SELECTION_SHARE)
                .build()
        );
    }

    public static void sendSelectionCopyEvent(Activity context){
        ((SSApplication) context.getApplication()).getTracker().send(
            new HitBuilders.EventBuilder()
                .setCategory(GA_SELECTION_CATEGORY)
                .setAction(GA_SELECTION_COPY)
                .build()
        );
    }
}
