package fr.husta.android.dockersearch.suggestion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Store suggestions in {@link SharedPreferences}.
 */
public class SuggestionManager
{

    private static final String PREFS_NAME = "SuggestionPrefs";
    private static final String KEY_SUGGESTIONS = "suggestions";

    private final SharedPreferences sharedPreferences;

    public SuggestionManager(Context context)
    {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public SuggestionManager(Activity activity)
    {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    /**
     * Save a new suggestion
     */
    public void addSuggestion(String suggestion)
    {
        Set<String> suggestionSet = getSuggestionSet();
        suggestionSet.add(suggestion);
        sharedPreferences.edit().putStringSet(KEY_SUGGESTIONS, suggestionSet).apply();
    }

    /**
     * Remove a specific suggestion
     */
    public void removeSuggestion(String suggestion)
    {
        Set<String> suggestionSet = getSuggestionSet();
        if (suggestionSet.remove(suggestion))
        {
            sharedPreferences.edit().putStringSet(KEY_SUGGESTIONS, suggestionSet).apply();
        }
    }

    /**
     * Get all saved suggestions as a List
     */
    public List<String> getSuggestions()
    {
        return new ArrayList<>(getSuggestionSet());
    }

    /**
     * Clear all stored suggestions
     */
    public void clearSuggestions()
    {
        sharedPreferences.edit().remove(KEY_SUGGESTIONS).apply();
    }

    /**
     * Internal helper to get suggestion set
     */
    private Set<String> getSuggestionSet()
    {
        return new HashSet<>(sharedPreferences.getStringSet(KEY_SUGGESTIONS, new HashSet<>()));
    }
}
