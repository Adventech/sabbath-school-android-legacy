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

package com.cryart.sabbathschool.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.ui.fragment.SSWebViewFragment;

import java.util.ArrayList;

public class SSTabsAdapter extends FragmentPagerAdapter {
    private String[] _SSTabTitles;

    public ArrayList<SSDay> _SSDays;
    public SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public SSTabsAdapter(Context context, FragmentManager _FragmentManager, ArrayList<SSDay> _SSDays) {
        super(_FragmentManager);
        this._SSDays = _SSDays;
        this._SSTabTitles = context.getResources().getStringArray(R.array.ss_tabs);
    }

    public void setDays(ArrayList<SSDay> _SSDays){
        this._SSDays = _SSDays;
        for (int i = 0; i < registeredFragments.size(); i++) {
            ((SSWebViewFragment)registeredFragments.get(i)).loadDay(_SSDays.get(i)._day_date);
        }
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
        return SSWebViewFragment.newInstance(_SSDays.get(position)._day_date);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }
}
