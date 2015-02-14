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

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.model.SSLesson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SSMenuAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private LinkedHashMap<?, ArrayList<?>> menu;
    private List<List<?>> menuList;


    public SSMenuAdapter(Activity context, LinkedHashMap<?, ArrayList<?>> menu){
        this.context = context;
        this.menu = menu;
        this.menuList = new ArrayList<List<?>>(menu.values());
    }

    @Override
    public int getGroupCount() {
        return menu.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menuList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        int i = 0;
        for (Map.Entry<?, ArrayList<?>> group : menu.entrySet()) {
            if (i++ == groupPosition){
                return group.getKey();
            }
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menuList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        Object menuItemGroup = getGroup(groupPosition);

        if (getChildrenCount(groupPosition) > 0){
            view = inflater.inflate(R.layout.ss_menu_header, null);
            TextView SSMenuLessonName = (TextView) view.findViewById(R.id.ss_menu_lesson_name);
            SSMenuLessonName.setText(((SSLesson)menuItemGroup)._lesson_name);
        } else {
            view = inflater.inflate(R.layout.ss_menu_day, null);
            TextView SSMenuDayName = (TextView) view.findViewById(R.id.ss_menu_day_name);
            TextView SSMenuDayDate = (TextView) view.findViewById(R.id.ss_menu_day_date);
            TextView SSMenuDayDateNumber = (TextView) view.findViewById(R.id.ss_menu_day_date_number);
            TextView SSMenuDayDateMonth = (TextView) view.findViewById(R.id.ss_menu_day_date_month);

            SimpleDateFormat SSDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date SSDate = SSDateFormat.parse(((SSDay)menuItemGroup)._day_date);
                SSMenuDayDateNumber.setText(new SimpleDateFormat("d").format(SSDate));
                SSMenuDayDateMonth.setText(new SimpleDateFormat("LLL").format(SSDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SSMenuDayName.setText(((SSDay)menuItemGroup)._day_name);
            SSMenuDayDate.setText(((SSDay)menuItemGroup)._day_date_text);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        SSLesson lesson = (SSLesson) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        view = inflater.inflate(R.layout.ss_menu_lesson, null);

        TextView SSMenuLessonName = (TextView) view.findViewById(R.id.ss_menu_lesson_name);
        SSMenuLessonName.setText(lesson._lesson_name);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
