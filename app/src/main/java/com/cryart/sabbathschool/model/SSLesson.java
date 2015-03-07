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

public class SSLesson extends Object {
    public int _serial;
    public String _lesson_name;
    public String _lesson_date_text;
    public String _lesson_image;


    public SSLesson(int serial,
                    String lesson_name,
                    String lesson_date_text){
        this._serial = serial;
        this._lesson_name = lesson_name;
        this._lesson_date_text = lesson_date_text;
    }

    public SSLesson(int serial,
                    int lesson_quarter_serial,
                    String lesson_name,
                    String lesson_date_text,
                    String lesson_image) {
        this._serial = serial;
        this._lesson_name = lesson_name;
        this._lesson_date_text = lesson_date_text;
        this._lesson_image = lesson_image;
    }
}
