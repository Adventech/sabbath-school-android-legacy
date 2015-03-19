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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SSHelper {
    /**
     * Reads file located in the assets directory and returns contents of it as String
     *
     * @param context Context that will be used to retrieve asset path.
     * @param assetPath Relative to the asset folder, path of particular asset to read from
     * @return Contents of the assetPath
     */
    public static String readFileFromAssets(Context context, String assetPath){

        StringBuilder buf = new StringBuilder();
        try {
            InputStream json = context.getAssets().open(assetPath);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            return buf.toString();

        } catch (IOException e){ return "";  }
    }

    /**
     * Returns the height of the status bar
     *
     * @param context Context that will be used to identify the height
     * @return Integer of the status bar height in dp
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return result;
    }

    /**
     * Returns Bitmap representation of the decoded byte sequence from base64 input string
     * @param input Base64 encoded image
     * @return Bitmap image
     */
    public static Bitmap getBitmapFromBase64(String input){
        byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    /**
     * Converts DP value to pixels value according to the DisplayMetric's current density
     * @param context Context that will be used to retrieve DisplayMetrics
     * @param dp Value to be converted
     * @return Pixel-value of the input dp
     */
    public static int convertDpToPixels(Context context, int dp){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Converts Pixel value to DP value according to the DisplayMetric's current density
     * @param context Context that will be used to retrieve DisplayMetrics
     * @param px Value to be converted
     * @return DP value of the input px
     */
    public static int convertPixelsToDp(Context context, int px){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Parses time string and returns parsed hour of day
     * @param time Time string
     * @param parseFormat Format of the time string
     * @return Hour of the day or 0 if error
     */
    public static int parseHourFromString(String time, String parseFormat){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(parseFormat);
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            return calendar.get(Calendar.HOUR_OF_DAY);
        } catch (Exception exception){
            return 0;
        }
    }

    /**
     * Parses time string and returns parsed minute of hour
     * @param time Time string
     * @param parseFormat Format of the time string
     * @return Minute of the hour or 0 if error
     */
    public static int parseMinuteFromString(String time, String parseFormat){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(parseFormat);
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            return calendar.get(Calendar.MINUTE);
        } catch (Exception exception){
            return 0;
        }
    }

    /**
     * Parses time string and returns it formatted with <code>returnFormat</code>
     * @param time Time string
     * @param parseFormat Format of the time string
     * @param returnFormat Desired format
     * @return Reformatted string
     */
    public static String parseTimeAndReturnInFormat(String time, String parseFormat, java.text.DateFormat returnFormat){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(parseFormat);
            Date date = sdf.parse(time);
            return returnFormat.format(date);
        } catch (Exception exception){
            return time;
        }
    }
}
