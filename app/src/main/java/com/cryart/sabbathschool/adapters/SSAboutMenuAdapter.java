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
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.model.SSAboutMenuItem;
import com.cryart.sabbathschool.util.SSConstants;

import java.util.ArrayList;

public class SSAboutMenuAdapter extends BaseAdapter {
    private ArrayList<SSAboutMenuItem> _SSAboutMenuItems;
    private Context context;
    private LayoutInflater inflater;

    public SSAboutMenuAdapter(Context context, ArrayList<SSAboutMenuItem> _SSAboutMenuItems){
        this.context = context;
        this._SSAboutMenuItems = _SSAboutMenuItems;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount(){
        return _SSAboutMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return _SSAboutMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SSAboutMenuViewHolder _SSAboutMenuViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.ss_about_menu_item, null);
            _SSAboutMenuViewHolder = new SSAboutMenuViewHolder();
            view.setTag(_SSAboutMenuViewHolder);
        } else {
            _SSAboutMenuViewHolder = (SSAboutMenuViewHolder) view.getTag();
        }

        SSAboutMenuItem _SSAboutMenuItem = _SSAboutMenuItems.get(position);

        TextView _SSAboutMenuIcon = (TextView) view.findViewById(R.id.ss_about_menu_icon);
        TextView _SSAboutMenuTitle = (TextView) view.findViewById(R.id.ss_about_menu_title);

        _SSAboutMenuIcon.setTypeface(Typeface.createFromAsset(context.getAssets(), SSConstants.SS_FONTAWESOME_FONT_PATH));

        _SSAboutMenuIcon.setText(_SSAboutMenuItem._SSAboutMenuIcon);
        _SSAboutMenuTitle.setText(_SSAboutMenuItem._SSAboutMenuTitle);

        _SSAboutMenuViewHolder._SSAboutMenuIcon = _SSAboutMenuIcon;
        _SSAboutMenuViewHolder._SSAboutMenuTitle = _SSAboutMenuTitle;

        return view;
    }

    private class SSAboutMenuViewHolder {
        TextView _SSAboutMenuIcon, _SSAboutMenuTitle;
    }

}
