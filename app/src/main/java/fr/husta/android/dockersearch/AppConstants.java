package fr.husta.android.dockersearch;


public interface AppConstants
{

    /**
     * https://developer.chrome.com/multidevice/android/customtabs
     */
    boolean USE_CHROME_CUSTOM_TABS = true;

    /**
     * Default page size for {@link fr.husta.android.dockersearch.docker.DockerSearchRestService#searchImages(java.lang.String, int)}.
     * <br/>
     * MAX = 100.
     */
    int IMAGE_SEARCH_PAGE_SIZE = 30;

    int TAG_LIST_PAGE_SIZE = 50;

}
