package fr.husta.android.dockersearch.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Custom SearchRecentSuggestionsProvider for Docker Search.
 */
public class RecentSearchProvider
        extends SearchRecentSuggestionsProvider
{

    public static final String AUTHORITY = RecentSearchProvider.class.getName();
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchProvider()
    {
        setupSuggestions(AUTHORITY, MODE);
    }

}
