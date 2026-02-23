package fr.husta.android.dockersearch.search

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.SearchRecentSuggestions
import androidx.core.net.toUri
import java.util.Objects

/**
 * Custom extension of [SearchRecentSuggestions].
 * Initialized with data from [RecentSearchProvider].
 */
class CustomSearchRecentSuggestions(private val context: Context) : SearchRecentSuggestions(
    context, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE
) {
    private val suggestionsUri =
        ("content://" + RecentSearchProvider.AUTHORITY + "/suggestions").toUri()

    /**
     * Removes a specific suggestion, using [ContentResolver].
     * 
     * @param suggestion Query to delete
     * @see ContentResolver.delete
     */
    fun removeSuggestion(suggestion: String) {
        Objects.requireNonNull(suggestion)
        val resolver = context.contentResolver
        resolver.delete(suggestionsUri, "display1 = ?", arrayOf(suggestion))
    }

    val recentSearches: MutableList<String>
        /**
         * List of recent searches.
         */
        get() {
            val list: MutableList<String> = mutableListOf()
            val resolver = context.contentResolver
            val cursor = resolver.query(
                suggestionsUri,
                arrayOf("query"),  // projection
                null,  // selection
                null,  // selectionArgs
                "date DESC" // sort order (latest first)
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(0)) // column "query"
                }
            }

            return list
        }
}
