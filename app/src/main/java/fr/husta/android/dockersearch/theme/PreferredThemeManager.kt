package fr.husta.android.dockersearch.theme

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Store preferred theme like dark mode or light mode in [SharedPreferences].
 */
class PreferredThemeManager {
    private val sharedPreferences: SharedPreferences

    constructor(context: Context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    constructor(activity: Activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    }

    var preferredTheme: Int
        get() = sharedPreferences.getInt(
            KEY_PREF_SAVED_THEME,
            DEFAULT_THEME
        )
        set(theme) {
            sharedPreferences.edit {
                putInt(KEY_PREF_SAVED_THEME, theme)
            }
        }

    companion object {
        private const val PREFS_NAME = "ThemePrefs"
        private const val KEY_PREF_SAVED_THEME = "saved_theme"
        private const val DEFAULT_THEME = 2 // = system
    }
}
