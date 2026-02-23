package fr.husta.android.dockersearch.search;

import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

/**
 * Custom SearchRecentSuggestionsProvider for Docker Search.
 * <p>
 * To be used with {@link SearchRecentSuggestions}.
 * </p>
 */
public final class RecentSearchProvider
        extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = RecentSearchProvider.class.getName();
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
