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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cryart.sabbathschool.R;
import com.cryart.sabbathschool.util.SSConstants;
import com.cryart.sabbathschool.util.SSCore;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class SSLoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ss_loading_activity);

        SharedPreferences ssPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String[] _SSSupportedLanguages = getResources().getStringArray(R.array.ss_settings_lesson_language_values);

        if (ssPreferences.getString(SSConstants.SS_SETTINGS_LANGUAGE_KEY, null) == null){
            String _SSLanguage = Locale.getDefault().getLanguage();
            if (Arrays.asList(_SSSupportedLanguages).contains(_SSLanguage)) {
                _SSLanguage = SSConstants.SS_SETTINGS_FALLBACK_LANGUAGE;
            }
            ssPreferences.edit().putString(SSConstants.SS_SETTINGS_LANGUAGE_KEY, _SSLanguage).commit();
        }

        PreferenceManager.setDefaultValues(this, R.xml.ss_settings, false);

        new QuarterlyDownloader(this).execute();
    }

    private class QuarterlyDownloader extends AsyncTask<Void, Void, Boolean> {
        private Context context;

        public QuarterlyDownloader(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            SSCore ss_core = SSCore.getInstance(this.context);

            try {
                ss_core.copyDataBaseIfNeeded();
            } catch (IOException e){}

            return ss_core.downloadIfNeeded();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Log.v("Error downloading", "Error downloading");
            } else {
                Intent i = new Intent(getApplicationContext(), SSMainActivity.class);
                startActivity(i);
            }
        }

        @Override
        protected void onPreExecute() {}
    }
}
