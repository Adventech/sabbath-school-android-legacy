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

package com.cryart.sabbathschool.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cryart.sabbathschool.model.SSDay;
import com.cryart.sabbathschool.model.SSLesson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SSCore extends SQLiteOpenHelper {
    private static String ssTodayDate = null;
    private static final String LANGUAGE = Locale.getDefault().getLanguage().equalsIgnoreCase("ru") || Locale.getDefault().getLanguage().equalsIgnoreCase("uk") ? "ru" : "en";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SabbathSchool.db";
    private final Context context;

    public SSCore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void ssSaveHighlights(int ssDaySerial, String highlights){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cu = db.rawQuery("UPDATE ss_days SET day_highlights = ? " +
                "WHERE serial = ?", new String[] { highlights, String.valueOf(ssDaySerial) });
        cu.moveToFirst();
        cu.close();
        db.close();
    }

    public void ssSaveComments(int ssDaySerial, String comments){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cu = db.rawQuery("UPDATE ss_days SET day_comments = ? " +
                "WHERE serial = ?", new String[] { comments, String.valueOf(ssDaySerial) });
        cu.moveToFirst();
        cu.close();
        db.close();
    }

    public List<SSDay> ssGetDaysByLessonSerial(int ssLessonSerial){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ss_days.* " +
                "FROM ss_days WHERE day_lesson_serial = ? " +
                "ORDER BY serial ASC", new String[] { String.valueOf(ssLessonSerial) });

        List<SSDay> ret = new ArrayList<SSDay>();
        if (c.moveToFirst()) {
            do {
                ret.add(new SSDay(c.getInt(0), c.getString(2), c.getString(3), c.getString(7)));
            } while (c.moveToNext());
        }
        return ret;
    }

    public List<SSLesson> ssGetLessons(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ss_quarters.serial " +
                "FROM ss_quarters, ss_lessons, ss_days " +
                "WHERE ss_days.day_date = ? AND ss_days.day_lesson_serial = ss_lessons.serial " +
                "   AND ss_lessons.lesson_quarter_serial = ss_quarters.serial " +
                "   AND ss_quarters.quarter_lang = ?", new String[]{ this.ssTodaysDate(), LANGUAGE });

        c.moveToFirst();
        int ssQuarterSerial = c.getInt(0);

        c = db.rawQuery("SELECT ss_lessons.* " +
                "FROM ss_lessons " +
                "WHERE ss_lessons.lesson_quarter_serial = ?", new String[]{ String.valueOf(ssQuarterSerial) });

        List<SSLesson> ret = new ArrayList<SSLesson>();
        if (c.moveToFirst()) {
            do {
                ret.add(new SSLesson(c.getInt(0), c.getString(2), c.getString(4)));
            } while (c.moveToNext());
        }
        return ret;
    }

    public SSLesson ssGetTodaysLesson(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ss_lessons.* FROM ss_lessons, ss_days, ss_quarters " +
                "WHERE ss_days.day_date = ? AND ss_days.day_lesson_serial = ss_lessons.serial " +
                "   AND ss_lessons.lesson_quarter_serial = ss_quarters.serial" +
                "   AND ss_quarters.quarter_lang = ? ORDER BY ss_lessons.serial " +
                "ASC LIMIT 1", new String[] { this.ssTodaysDate(), LANGUAGE });

        if (cursor != null)
            cursor.moveToFirst();

        return new SSLesson(
                cursor.getInt(0),
                cursor.getString(2),
                cursor.getString(4)
        );
    }

    public String ssTodaysDate(){
        if (SSCore.ssTodayDate == null) {
            String ssTodayTmp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT COUNT(1) FROM ss_days, ss_lessons, ss_quarters " +
                    "WHERE ss_days.day_date = ? AND ss_days.day_lesson_serial = ss_lessons.serial " +
                    "   AND ss_lessons.lesson_quarter_serial = ss_quarters.serial " +
                    "   AND ss_quarters.quarter_lang = ?", new String[]{ ssTodayTmp, LANGUAGE });

            c.moveToFirst();
            int count = c.getInt(0);
            c.close();

            if (count < 1) {
                c = db.rawQuery("SELECT ss_days.day_date " +
                        "FROM ss_days, ss_lessons, ss_quarters " +
                        "WHERE ss_days.day_lesson_serial = ss_lessons.serial " +
                        "   AND ss_lessons.lesson_quarter_serial = ss_quarters.serial " +
                        "   AND ss_quarters.quarter_lang = ? " +
                        "ORDER BY ss_days.day_date DESC LIMIT 1", new String[]{ LANGUAGE });
                c.moveToFirst();
                ssTodayTmp = c.getString(0);
                c.close();
            }

            SSCore.ssTodayDate = ssTodayTmp;
        }

        return SSCore.ssTodayDate;
    }

    public SSDay ssGetDay(String day_date){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ss_days.*, ss_lessons.lesson_image as lesson_image " +
                "FROM ss_days, ss_lessons, ss_quarters " +
                "WHERE ss_days.day_date = ? AND ss_days.day_lesson_serial = ss_lessons.serial " +
                "   AND ss_lessons.lesson_quarter_serial = ss_quarters.serial " +
                "   AND ss_quarters.quarter_lang = ? LIMIT 1", new String[] { day_date, LANGUAGE });

        if (cursor != null)
            cursor.moveToFirst();

        return new SSDay(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)
        );
    }

    public void copyDataBaseIfNeeded() throws IOException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()){
            this.getReadableDatabase();
            this.close();
            InputStream is = context.getAssets().open(DATABASE_NAME);
            OutputStream os = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }

            os.flush();
            os.close();
            is.close();
        }
    }

    public boolean downloadIfNeeded(){
        InputStream is;
        String json;
        JSONObject jObj = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(1) FROM ss_days, ss_lessons, ss_quarters " +
                "WHERE ss_days.day_date = ? AND ss_days.day_lesson_serial = ss_lessons.serial " +
                "   AND ss_lessons.lesson_quarter_serial = ss_quarters.serial " +
                "   AND ss_quarters.quarter_lang = ?", new String[] { new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()), LANGUAGE });

        c.moveToFirst();
        int count = c.getInt(0);
        c.close();

        if (count > 0) { return true; }

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://s3-us-west-2.amazonaws.com/com.cryart.sabbathschool/latest_" + LANGUAGE + ".json");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) { return false;
        } catch (ClientProtocolException e) { return false;
        } catch (IOException e) { return false; }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) { return false; }

        try {
            JSONObject ss_quarterly = new JSONObject(json);
            String ss_quarter_id = ss_quarterly.getString("quarter_id");
            String ss_quarter_name = ss_quarterly.getString("quarter_name");
            String ss_quarter_image = "";
            String ss_quarter_lang = ss_quarterly.getString("quarter_lang");
            JSONArray ss_lessons = ss_quarterly.getJSONArray("quarter_lessons");

            if (ss_quarterly.has("quarter_image")){
                ss_quarter_image = ss_quarterly.getString("quarter_image");
            }

            c = db.rawQuery("SELECT COUNT(1) FROM ss_quarters " +
                    "WHERE quarter_id = ? AND quarter_lang = ?", new String[] {ss_quarter_id, LANGUAGE});
            c.moveToFirst();
            int quarter_count = c.getInt(0);
            c.close();
            if (quarter_count > 0){ return true; }

            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("quarter_id", ss_quarter_id);
            values.put("quarter_name", ss_quarter_name);
            values.put("quarter_image", ss_quarter_image);
            values.put("quarter_lang", ss_quarter_lang);

            long ss_quarter_serial = db.insert("ss_quarters", null, values);

            for (int i = 0; i < ss_lessons.length(); i++){
                JSONObject ss_lesson = ss_lessons.getJSONObject(i);
                String ss_lesson_name = ss_lesson.getString("lesson_name");
                String ss_lesson_image = ss_lesson.getString("lesson_image");
                String ss_lesson_date_text = ss_lesson.getString("lesson_date_text");
                JSONArray ss_days = ss_lesson.getJSONArray("lesson_days");

                values = new ContentValues();
                values.put("lesson_name", ss_lesson_name);
                values.put("lesson_image", ss_lesson_image);
                values.put("lesson_date_text", ss_lesson_date_text);
                values.put("lesson_quarter_serial", ss_quarter_serial);

                long ss_lesson_serial = db.insert("ss_lessons", null, values);

                for (int j = 0; j < ss_days.length(); j++){
                    JSONObject ss_day = ss_days.getJSONObject(j);
                    String ss_day_date = ss_day.getString("day_date");
                    String ss_day_name = ss_day.getString("day_name");
                    String ss_day_text = ss_day.getString("day_text");
                    String ss_day_comments = "";
                    String ss_day_highlights = "";
                    String ss_day_date_text = ss_day.getString("day_date_text");
                    String ss_day_verses = ss_day.getString("day_verses");

                    values = new ContentValues();
                    values.put("day_date", ss_day_date);
                    values.put("day_name", ss_day_name);
                    values.put("day_text", ss_day_text);
                    values.put("day_comments", ss_day_comments);
                    values.put("day_highlights", ss_day_highlights);
                    values.put("day_date_text", ss_day_date_text);
                    values.put("day_verses", ss_day_verses);
                    values.put("day_lesson_serial", ss_lesson_serial);

                    db.insert("ss_days", null, values);
                }
            }

            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
