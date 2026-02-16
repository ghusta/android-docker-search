package fr.husta.android.dockersearch

object AppConstants {

    /**
     * https://developer.chrome.com/multidevice/android/customtabs
     */
    const val USE_CHROME_CUSTOM_TABS: Boolean = true

    /**
     * Default page size for [fr.husta.android.dockersearch.docker.DockerSearchRestService.searchImages].
     * <br></br>
     * MAX = 100.
     */
    const val IMAGE_SEARCH_PAGE_SIZE: Int = 30

    const val TAG_LIST_PAGE_SIZE: Int = 50

}
