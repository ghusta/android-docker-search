package fr.husta.android.dockersearch.suggestion

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Store suggestions in [SharedPreferences].
 */
class SuggestionManager {
    private lateinit var sharedPreferences: SharedPreferences

    constructor(context: Context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    constructor(activity: Activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    }

    // Backing property
    private val _suggestionSet =
        this.sharedPreferences.getStringSet(KEY_SUGGESTIONS, emptySet())
            ?.toMutableSet()
            ?: mutableSetOf()

    // Public read-only view
    val suggestions: Set<String>
        get() = _suggestionSet

    /**
     * Save a new suggestion
     */
    fun addSuggestion(suggestion: String) {
        _suggestionSet.add(suggestion)
        sharedPreferences.edit { putStringSet(KEY_SUGGESTIONS, _suggestionSet) }
    }

    /**
     * Remove a specific suggestion
     */
    fun removeSuggestion(suggestion: String) {
        if (_suggestionSet.remove(suggestion)) {
            sharedPreferences.edit { putStringSet(KEY_SUGGESTIONS, _suggestionSet) }
        }
    }

    /**
     * Clear all stored suggestions
     */
    fun clearSuggestions() {
        sharedPreferences.edit { remove(KEY_SUGGESTIONS) }
    }

    companion object {
        private const val PREFS_NAME = "SuggestionPrefs"
        private const val KEY_SUGGESTIONS = "suggestions"
    }
}
