package fr.husta.android.dockersearch.search

import android.content.SearchRecentSuggestionsProvider

/**
 * Custom SearchRecentSuggestionsProvider for Docker Search.
 * 
 * 
 * To be used with [SearchRecentSuggestions].
 * 
 */
class RecentSearchProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        @JvmField
        val AUTHORITY: String = RecentSearchProvider::class.java.getName()

        @JvmField
        val MODE: Int = DATABASE_MODE_QUERIES
    }
}
