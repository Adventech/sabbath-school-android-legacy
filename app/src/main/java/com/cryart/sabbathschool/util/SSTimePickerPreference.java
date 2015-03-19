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

import android.app.TimePickerDialog;
import android.content.Context;
import android.preference.Preference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class SSTimePickerPreference extends Preference implements TimePickerDialog.OnTimeSetListener {
    public SSTimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected String getValue(){
        return getSharedPreferences().getString(getKey(), SSConstants.SS_SETTINGS_NOTIFICATION_TIME_DEFAULT_VALUE);
    }

    @Override
    protected void onClick() {
        super.onClick();

        String ss_notification_time = getValue();

        new TimePickerDialog(
            getContext(),
            this,
            SSHelper.parseHourFromString(ss_notification_time, SSConstants.SS_NOTIFICATION_TIME_SETTINGS_FORMAT),
            SSHelper.parseMinuteFromString(ss_notification_time, SSConstants.SS_NOTIFICATION_TIME_SETTINGS_FORMAT),
            DateFormat.is24HourFormat(getContext())
        ).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        if (timePicker.isShown()) {
            getSharedPreferences().edit().putString(
                    SSConstants.SS_SETTINGS_NOTIFICATION_TIME_KEY,
                    String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute())
            ).commit();
            setSummary(getSummary());
        }
    }

    @Override
    public String getSummary(){
        return SSHelper.parseTimeAndReturnInFormat(getValue(), SSConstants.SS_NOTIFICATION_TIME_SETTINGS_FORMAT, android.text.format.DateFormat.getTimeFormat(getContext()));
    }
}
