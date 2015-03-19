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

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.receivers.SSBroadcastReceiver;
import com.cryart.sabbathschool.ui.activity.SSLoadingActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SSNotification {
    public static void setRepeatingNotification(Context context){
        AlarmManager _SSAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent _SSNotificationIntent = new Intent(context, SSBroadcastReceiver.class);
        PendingIntent _SSAlarmIntent = PendingIntent.getBroadcast(context, 0, _SSNotificationIntent, 0);

        Calendar _SSNotificationTime = Calendar.getInstance();

        SharedPreferences ssPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ss_settings_notification_time = ssPreferences.getString(SSConstants.SS_SETTINGS_NOTIFICATION_TIME_KEY, SSConstants.SS_SETTINGS_NOTIFICATION_TIME_DEFAULT_VALUE);

        _SSNotificationTime.set(Calendar.HOUR_OF_DAY, SSHelper.parseHourFromString(ss_settings_notification_time, SSConstants.SS_NOTIFICATION_TIME_SETTINGS_FORMAT));
        _SSNotificationTime.set(Calendar.MINUTE, SSHelper.parseMinuteFromString(ss_settings_notification_time, SSConstants.SS_NOTIFICATION_TIME_SETTINGS_FORMAT));
        _SSNotificationTime.set(Calendar.SECOND, 0);

        _SSAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, _SSNotificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, _SSAlarmIntent);
    }

    public static void cancelRepeatingNotification(Context context) {
        AlarmManager _SSAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent _SSUpdateServiceIntent = new Intent(context, SSBroadcastReceiver.class);
        PendingIntent _SSPendingUpdateServiceIntent = PendingIntent.getBroadcast(context, 0, _SSUpdateServiceIntent, 0);

        try {
            _SSAlarmManager.cancel(_SSPendingUpdateServiceIntent);
        } catch (Exception e) {}
    }


    public static void showLessonNotification(Context context){
        SSCore _SSCore = SSCore.getInstance(context);
        if (!SSCore.databaseExists()) return;
        try {
            SSDay _SSDay = _SSCore.ssGetDay(_SSCore.ssTodaysDate());
            Bitmap _SSLessonBitmap = SSHelper.getBitmapFromBase64(_SSDay._lesson_image);

            NotificationManager _SSNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent _SSContentIntent = new Intent(context, SSLoadingActivity.class);
            Intent _SSShareIntent = new Intent();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            String _SSBitmapLocation = Environment.getExternalStorageDirectory().toString() + "/" + SSConstants.SS_NOTIFICATION_LESSON_HERO_TMP_FILENAME;

            _SSContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _SSShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _SSShareIntent.setAction(Intent.ACTION_SEND);
            _SSShareIntent.putExtra(Intent.EXTRA_TEXT, _SSDay._day_name);

            _SSLessonBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);


            File f = new File(_SSBitmapLocation);
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            _SSShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + _SSBitmapLocation));
            _SSShareIntent.setType("*/*");

            PendingIntent _SSPendingContentIntent = PendingIntent.getActivity(context, 0, _SSContentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent _SSPendingShareIntent = PendingIntent.getActivity(context, 0, Intent.createChooser(_SSShareIntent, "Share lesson to:"), PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder _SSNotificationBuilder =
                new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ss_app_icon_notification)
                    .setColor(context.getResources().getColor(R.color.ss_primary))
                    .setLargeIcon(_SSLessonBitmap)
                    .setContentTitle(_SSDay._day_name)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(_SSDay._day_name)
                        .setSummaryText(_SSDay._day_name)
                        .bigPicture(_SSLessonBitmap)
                    )
                    .addAction(R.drawable.ss_ic_book_grey600_24dp, context.getString(R.string.ss_notification_read_lesson), _SSPendingContentIntent)
                    .addAction(R.drawable.ss_ic_share_white_24dp, context.getString(R.string.ss_notification_share_lesson), _SSPendingShareIntent)
                    .setAutoCancel(true)
                    .setContentIntent(_SSPendingContentIntent)
                    .setContentText(_SSDay._day_text);

            _SSNotificationManager.notify(1, _SSNotificationBuilder.build());
        } catch (Exception e) {}
    }
}
