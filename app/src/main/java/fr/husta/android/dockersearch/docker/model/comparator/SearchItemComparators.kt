package fr.husta.android.dockersearch.docker.model.comparator

import fr.husta.android.dockersearch.docker.model.ImageItem

object SearchItemComparators {

    val BY_OFFICIAL_AND_STARS: Comparator<ImageItem> =
        compareByDescending<ImageItem> { it.isOfficial }
            .thenByDescending { it.starCount }

    val DEFAULT_COMPARATOR: Comparator<ImageItem> =
        BY_OFFICIAL_AND_STARS

}