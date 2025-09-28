package fr.husta.android.dockersearch.search;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.net.Uri;

import java.util.Objects;

/**
 * Custom SearchRecentSuggestionsProvider for Docker Search.
 */
public class RecentSearchProvider
        extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = RecentSearchProvider.class.getName();
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    /**
     * Removes a specific suggestion, using {@link ContentResolver}.
     *
     * @param context    {@link Context}
     * @param suggestion Query to delete
     * @see SearchRecentSuggestionsProvider#delete(Uri, String, String[])
     */
    public static void removeSuggestion(Context context, String suggestion) {
        Objects.requireNonNull(suggestion);
        ContentResolver resolver = context.getContentResolver();
        Uri suggestionsUri = Uri.parse("content://" + AUTHORITY + "/suggestions");
        resolver.delete(suggestionsUri, "display1 = ?", new String[]{suggestion});
    }

}
