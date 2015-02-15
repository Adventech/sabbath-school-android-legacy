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

package com.cryart.sabbathschool.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.adapters.SSMenuAdapter;
import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.model.SSLesson;
import com.cryart.sabbathschool.ui.fragment.SSWebViewFragment;
import com.cryart.sabbathschool.ui.widget.SSSlidingTabLayout;
import com.cryart.sabbathschool.util.SSHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SSMainActivity extends ActionBarActivity {
    private final int SS_PAGER_OFFSCREEN_LIMIT = 7;


    private ActionBarDrawerToggle _SSActionBarToggle;
    private DrawerLayout _SSDrawerLayout;
    private SSSlidingTabLayout _SSTabs;
    private Toolbar _SSToolbar;
    private ViewPager _SSPager;
    private View _SSStatusBar;
    private ExpandableListView _SSMenu;


    private void setupWidgets(){
        _SSDrawerLayout = (DrawerLayout) findViewById(R.id.ss_main_layout);
        _SSToolbar = (Toolbar) findViewById(R.id.ss_toolbar);
        _SSActionBarToggle = new ActionBarDrawerToggle(this, _SSDrawerLayout, _SSToolbar, R.string.ss_navigation_menu_open, R.string.ss_navigation_menu_close);
        _SSPager = (ViewPager) findViewById(R.id.ss_pager);
        _SSStatusBar = findViewById(R.id.ss_status_bar);
        _SSTabs = (SSSlidingTabLayout) findViewById(R.id.ss_tabs);
        _SSMenu = (ExpandableListView) findViewById(R.id.ss_menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ss_main_activity);
        setupWidgets();

        setSupportActionBar(_SSToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        _SSActionBarToggle.setDrawerIndicatorEnabled(true);

        _SSDrawerLayout.setDrawerListener(_SSActionBarToggle);
        _SSDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        _SSPager.setAdapter(new SSTabsAdapter(getSupportFragmentManager()));

        _SSPager.setOffscreenPageLimit(SS_PAGER_OFFSCREEN_LIMIT);

        ViewGroup.LayoutParams lp = _SSStatusBar.getLayoutParams();
        lp.height = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? SSHelper.getStatusBarHeight(this) : 0;
        _SSStatusBar.setLayoutParams(lp);

        _SSTabs.setCustomTabView(R.layout.ss_tab_indicator, android.R.id.text1);
        _SSTabs.setViewPager(_SSPager);

        _SSToolbar.getBackground().setAlpha(0);
        _SSStatusBar.getBackground().setAlpha(0);
        _SSTabs.setBackgroundColor(getResources().getColor(R.color.ss_primary));

        _SSMenu.setAdapter(new SSMenuAdapter(this, new LinkedHashMap<Object, ArrayList<?>>(){{
            put(new SSLesson(1, "Lesson 1. Judging", "1"), new ArrayList<Object>(){{
                add(new SSLesson(1, "1", "1"));
            }});
            put(new SSDay(1, "2015-01-01", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-02", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-03", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-04", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-05", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-06", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-07", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-08", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-09", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-10", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
            put(new SSDay(1, "2015-01-11", "A Matter of Life and Death", "Saturday. January 1"), new ArrayList<>());
        }}));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _SSActionBarToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (_SSActionBarToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ViewPager getPager(){
        return _SSPager;
    }

    public class SSTabsAdapter extends FragmentPagerAdapter {
        public SparseArray<Fragment> registeredFragments = new SparseArray<>();
        private final String[] _SSTabTitles = getResources().getStringArray(R.array.ss_tabs);

        public SSTabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _SSTabTitles[position];
        }

        @Override
        public int getCount() {
            return _SSTabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SSWebViewFragment.newInstance(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }
    }
}
