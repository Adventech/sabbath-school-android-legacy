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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.cryart.sabbathschool.adapters.SSTabsAdapter;
import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.model.SSLesson;
import com.cryart.sabbathschool.model.SSMenuMiscItem;
import com.cryart.sabbathschool.ui.fragment.SSWebViewFragment;
import com.cryart.sabbathschool.ui.widget.SSSlidingTabLayout;
import com.cryart.sabbathschool.ui.widget.SSWebView;
import com.cryart.sabbathschool.util.SSConstants;
import com.cryart.sabbathschool.util.SSCore;
import com.cryart.sabbathschool.util.SSHelper;
import com.cryart.sabbathschool.util.SSMenuMisc;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SSMainActivity extends ActionBarActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, SharedPreferences.OnSharedPreferenceChangeListener, SSWebView.OnScrollChangedCallback {
    private int SS_TOOLBAR_STATUS_BAR_ALPHA = 0;

    private SharedPreferences _SSPreferences;
    private Handler _SSHandler = new Handler();

    private ActionBarDrawerToggle _SSActionBarToggle;
    private DrawerLayout _SSDrawerLayout;
    private SSSlidingTabLayout _SSTabs;
    private Toolbar _SSToolbar;
    private ViewPager _SSPager;
    private View _SSStatusBar;
    private ExpandableListView _SSMenu;
    private KenBurnsView _SSHero;

    private SSLesson _SSCurrentLesson;
    private ArrayList<SSDay> _SSDays;
    private SSCore _SSCore;

    private void setupWidgets(){
        _SSDrawerLayout = (DrawerLayout) findViewById(R.id.ss_main_layout);
        _SSToolbar = (Toolbar) findViewById(R.id.ss_toolbar);
        _SSActionBarToggle = new ActionBarDrawerToggle(this, _SSDrawerLayout, _SSToolbar, R.string.ss_navigation_menu_open, R.string.ss_navigation_menu_close);
        _SSPager = (ViewPager) findViewById(R.id.ss_pager);
        _SSStatusBar = findViewById(R.id.ss_status_bar);
        _SSTabs = (SSSlidingTabLayout) findViewById(R.id.ss_tabs);
        _SSMenu = (ExpandableListView) findViewById(R.id.ss_menu);
        _SSHero = (KenBurnsView) findViewById(R.id.ss_hero);

        _SSCore = SSCore.getInstance(this);
        _SSPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setHeroView(){
        _SSHero.resume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        _SSHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(_SSPreferences.getBoolean(SSConstants.SS_SETTINGS_KEN_BURNS_EFFECT_KEY, SSConstants.SS_SETTINGS_KEN_BURNS_EFFECT_DEFAULT_VALUE)) _SSHero.resume(); else _SSHero.pause();
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    private void setMainView(){
        _SSCurrentLesson = _SSCore.ssGetTodaysLesson();
        _SSDays = _SSCore.ssGetDaysByLessonSerial(_SSCurrentLesson._serial);
        ((SSTabsAdapter)_SSPager.getAdapter()).setDays(_SSDays);
        this.setMenuItems();
    }

    private void setMenuItems(){
        ((SSMenuAdapter)_SSMenu.getExpandableListAdapter()).setMenu(
            new LinkedHashMap<Object, ArrayList<?>>(){{
                put(_SSCurrentLesson, _SSCore.ssGetLessons());
                for (SSDay ssDay : _SSDays){
                    put(ssDay, new ArrayList<>());
                }
                put(SSMenuMisc.SS_MENU_MISC_ITEM_DIVIDER, new ArrayList<>());
                put(SSMenuMisc.SS_MENU_MISC_ITEM_SETTINGS, new ArrayList<>());
                put(SSMenuMisc.SS_MENU_MISC_ITEM_ABOUT, new ArrayList<>());
            }}
        );
    }

    public void setToolbarStatusBarAlpha(int alpha){
        SS_TOOLBAR_STATUS_BAR_ALPHA = alpha;
        _SSToolbar.getBackground().setAlpha(alpha);
        _SSStatusBar.getBackground().setAlpha(alpha);
    }

    @Override
    public void onBackPressed() {
        if (_SSDrawerLayout.isDrawerOpen(Gravity.START)){
            _SSDrawerLayout.closeDrawer(Gravity.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        setToolbarStatusBarAlpha(SS_TOOLBAR_STATUS_BAR_ALPHA);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ss_main_activity);
        setupWidgets();

        setSupportActionBar(_SSToolbar);

        _SSActionBarToggle.setDrawerIndicatorEnabled(true);

        _SSDrawerLayout.setDrawerListener(_SSActionBarToggle);
        _SSDrawerLayout.setDrawerShadow(R.drawable.ss_menu_shadow, Gravity.START);

        _SSCurrentLesson = _SSCore.ssGetTodaysLesson();
        _SSDays = _SSCore.ssGetDaysByLessonSerial(_SSCurrentLesson._serial);

        _SSPager.setAdapter(new SSTabsAdapter(this, getSupportFragmentManager(), new ArrayList<SSDay>()));

        _SSPager.setOffscreenPageLimit(SSConstants.SS_PAGER_OFFSCREEN_PAGE_LIMIT);

        ViewGroup.LayoutParams lp = _SSStatusBar.getLayoutParams();
        lp.height = SSHelper.getStatusBarHeight(this);
        _SSStatusBar.setLayoutParams(lp);

        _SSTabs.setCustomTabView(R.layout.ss_tab_indicator, android.R.id.text1);
        _SSTabs.setViewPager(_SSPager);

        setToolbarStatusBarAlpha(SS_TOOLBAR_STATUS_BAR_ALPHA);
        _SSTabs.setBackgroundColor(getResources().getColor(R.color.ss_primary));

        _SSMenu.setAdapter(new SSMenuAdapter(this, new LinkedHashMap<Object, ArrayList<?>>()));

        this.setMainView();
        this.setTab(_SSCore.ssGetDay(_SSCore.ssTodaysDate()));

        _SSMenu.setOnGroupClickListener(this);
        _SSMenu.setOnChildClickListener(this);
        _SSPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _SSActionBarToggle.syncState();
    }

    @Override
    protected void onDestroy(){
        _SSPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeroView();
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

    private void closeSSMenu(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _SSDrawerLayout.closeDrawers();
            }
        }, 150);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        _SSCurrentLesson = (SSLesson) _SSMenu.getExpandableListAdapter().getChild(groupPosition, childPosition);
        _SSDays = _SSCore.ssGetDaysByLessonSerial(_SSCurrentLesson._serial);
        this.setMenuItems();
        _SSMenu.collapseGroup(groupPosition);
        return true;
    }

    private boolean setTab(SSDay selected){
        int _page = 0;
        for (SSDay day : ((SSTabsAdapter) _SSPager.getAdapter())._SSDays) {
            if (day._day_date.equalsIgnoreCase(selected._day_date)){
                _SSPager.setCurrentItem(_page, true);
                return true;
            }
            _page++;
        }
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        Object selected = _SSMenu.getExpandableListAdapter().getGroup(groupPosition);

        if (selected instanceof SSDay) {
            if (!setTab((SSDay)selected)){
                ((SSTabsAdapter) _SSPager.getAdapter()).setDays(_SSDays);
                setTab((SSDay)selected);
            }

            this.closeSSMenu();
            return true;
        } else if (selected instanceof SSMenuMiscItem){
            if (selected == SSMenuMisc.SS_MENU_MISC_ITEM_SETTINGS){
                Intent intent = new Intent(SSMainActivity.this, SSSettingsActivity.class);
                SSMainActivity.this.startActivity(intent);
            }

            if (selected == SSMenuMisc.SS_MENU_MISC_ITEM_ABOUT){
                Intent intent = new Intent(SSMainActivity.this, SSAboutActivity.class);
                SSMainActivity.this.startActivity(intent);
            }

            this.closeSSMenu();

            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch(s){
            case SSConstants.SS_SETTINGS_LANGUAGE_KEY: {
                SSCore.refreshLanguage();

                if (!_SSCore.quarterlyForLanguageExists()){
                    Intent i = new Intent(getApplicationContext(), SSLoadingActivity.class);
                    startActivity(i);
                } else {
                    this.setMainView();
                }
                break;
            }

            case SSConstants.SS_SETTINGS_KEN_BURNS_EFFECT_KEY: {
                this.setHeroView();
                break;
            }

            case SSConstants.SS_SETTINGS_TEXT_SIZE_KEY:
            case SSConstants.SS_SETTINGS_READING_MODE_KEY: {
                ((SSTabsAdapter)this._SSPager.getAdapter()).reloadTabs();
                break;
            }
        }
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY, int scrollY) {
        _SSHero.setTranslationY(scrollY * 0.5f);

        int headerHeight = _SSHero.getHeight() - _SSToolbar.getHeight() - _SSStatusBar.getHeight();
        float ratio = (float) Math.min(Math.max(scrollY * -1, 0), headerHeight) / headerHeight;

        setToolbarStatusBarAlpha((int) (ratio * 255));

        int scrollOtherFragments = scrollY * -1;
        if (_SSTabs.getTop() + scrollY < _SSToolbar.getHeight() + _SSStatusBar.getHeight()) {
            _SSTabs.setY(_SSToolbar.getHeight() + _SSStatusBar.getHeight());
            scrollOtherFragments = _SSTabs.getBottom() - (_SSToolbar.getHeight() + _SSStatusBar.getHeight() + _SSTabs.getHeight());
        } else {
            _SSTabs.setTranslationY(scrollY);
        }

        SparseArray<Fragment> registeredFragments = ((SSTabsAdapter) _SSPager.getAdapter()).registeredFragments;
        for (int i = 0; i < registeredFragments.size(); i++) {
            if (i == _SSPager.getCurrentItem()) continue;
            ((SSWebViewFragment) (registeredFragments.get(i)))._SSWebView._SSOnScrollChangedCallbackEnabled = false;
            ((SSWebViewFragment) (registeredFragments.get(i)))._SSWebView.scrollTo(0, scrollOtherFragments);
            ((SSWebViewFragment) (registeredFragments.get(i)))._SSWebView._SSOnScrollChangedCallbackEnabled = true;
        }
    }
}
