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

package com.cryart.sabbathschool.model;

import org.json.JSONObject;

public class SSDay {
    public int _serial;
    public int _day_lesson_serial;
    public String _day_date;
    public String _day_name;
    public String _day_text;
    public String _day_comments;
    public String _day_highlights;
    public String _day_date_text;
    public String _day_verses;
    public String _lesson_image;
    public JSONObject _day_verses_parsed;

    public SSDay(int serial, String day_date, String day_name, String day_date_text){
        this._serial = serial;
        this._day_date = day_date;
        this._day_name = day_name;
        this._day_date_text = day_date_text;
    }

    public SSDay(int serial, int day_lesson_serial, String day_date,
                 String day_name, String day_text, String day_comments,
                 String day_highlights, String day_date_text, String day_verses,
                 String lesson_image){
        this._serial = serial;
        this._day_lesson_serial = day_lesson_serial;
        this._day_date = day_date;
        this._day_name = day_name;
        this._day_text = day_text;
        this._day_comments = day_comments;
        this._day_highlights = day_highlights;
        this._day_date_text = day_date_text;
        this._day_verses = day_verses;
        try {
            this._day_verses_parsed = new JSONObject(day_verses);
        } catch (Exception e){
            this._day_verses_parsed = null;
        }
        this._lesson_image = lesson_image;
    }
}