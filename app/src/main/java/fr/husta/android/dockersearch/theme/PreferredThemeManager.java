package fr.husta.android.dockersearch.theme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Store preferred theme like dark mode or light mode in {@link SharedPreferences}.
 */
public class PreferredThemeManager
{
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String KEY_PREF_SAVED_THEME = "saved_theme";
    private static final int DEFAULT_THEME = 2; // = system

    private final SharedPreferences sharedPreferences;

    public PreferredThemeManager(Context context)
    {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public PreferredThemeManager(Activity activity)
    {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public int getPreferredTheme()
    {
        return sharedPreferences.getInt(KEY_PREF_SAVED_THEME, DEFAULT_THEME);
    }

    public void setPreferredTheme(int theme)
    {
        sharedPreferences.edit().putInt(KEY_PREF_SAVED_THEME, theme).apply();
    }

}
