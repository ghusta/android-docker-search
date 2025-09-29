package fr.husta.android.dockersearch.search;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;

import java.util.Objects;

/**
 * Custom extension of {@link SearchRecentSuggestions}.
 * Initialized with data from {@link RecentSearchProvider}.
 */
public class CustomSearchRecentSuggestions extends SearchRecentSuggestions
{

    private final Context context;
    private final Uri suggestionsUri;

    public CustomSearchRecentSuggestions(Context context)
    {
        super(context, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE);
        this.context = context;
        this.suggestionsUri = Uri.parse("content://" + RecentSearchProvider.AUTHORITY + "/suggestions");
    }

    /**
     * Removes a specific suggestion, using {@link ContentResolver}.
     *
     * @param suggestion Query to delete
     * @see ContentResolver#delete(Uri, String, String[])
     */
    public void removeSuggestion(String suggestion)
    {
        Objects.requireNonNull(suggestion);
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(suggestionsUri, "display1 = ?", new String[]{suggestion});
    }

}
