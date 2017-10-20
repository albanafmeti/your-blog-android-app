package com.techalin.yourblog.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Settings {

    private static Context context;

    public static void setContext(Context context) {
        Settings.context = context;
    }

    public static void setSetting(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSetting(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

}
