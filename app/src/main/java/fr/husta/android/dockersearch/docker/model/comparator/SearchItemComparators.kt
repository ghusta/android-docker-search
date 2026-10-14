package fr.husta.android.dockersearch.docker.model.comparator

import fr.husta.android.dockersearch.docker.model.SearchItem

object SearchItemComparators {

    val BY_OFFICIAL_AND_STARS: Comparator<SearchItem> =
        compareByDescending<SearchItem> { it.isOfficial }
            .thenByDescending { it.starCount }

    val DEFAULT_COMPARATOR: Comparator<SearchItem> =
        BY_OFFICIAL_AND_STARS

}