package io.github.xtls.xray4magisk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import io.github.xtls.xray4magisk.util.theme.ThemeUtil;
import rikka.material.app.DayNightDelegate;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    public static final String TAG = "Xray4MagiskManager";

    private static App instance = null;
    private SharedPreferences pref;

    public static App getInstance() {
        return instance;
    }

    public static SharedPreferences getPreferences() {
        return instance.pref;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        DayNightDelegate.setApplicationContext(this);
        DayNightDelegate.setDefaultNightMode(ThemeUtil.getDarkTheme());
    }
}
