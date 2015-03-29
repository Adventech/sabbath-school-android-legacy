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

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.model.SSAboutMenuItem;

import java.util.ArrayList;

public class SSAboutMenu {
    public static final SSAboutMenuItem SS_ABOUT_MENU_INSTAGRAM = new SSAboutMenuItem(R.string.ss_about_menu_instagram_icon, R.string.ss_about_menu_instagram_title);
    public static final SSAboutMenuItem SS_ABOUT_MENU_TWITTER = new SSAboutMenuItem(R.string.ss_about_menu_twitter_icon, R.string.ss_about_menu_twitter_title);
    public static final SSAboutMenuItem SS_ABOUT_MENU_PHOTO_CREDITS = new SSAboutMenuItem(R.string.ss_about_menu_photo_credits_icon, R.string.ss_about_menu_photo_credits_title);
    public static final SSAboutMenuItem SS_ABOUT_MENU_FACEBOOK_PAGE = new SSAboutMenuItem(R.string.ss_about_menu_facebook_icon, R.string.ss_about_menu_facebook_title);

    public static ArrayList<SSAboutMenuItem> getAboutMenuAsArrayList(){
        return new ArrayList<SSAboutMenuItem>(){{
            add(SS_ABOUT_MENU_INSTAGRAM);
            add(SS_ABOUT_MENU_TWITTER);
            add(SS_ABOUT_MENU_PHOTO_CREDITS);
            add(SS_ABOUT_MENU_FACEBOOK_PAGE);
        }};
    }
}
