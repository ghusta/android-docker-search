package fr.husta.android.dockersearch.utils.ui

import android.content.res.Configuration

object UiModeUtils {
    /**
     * @see Configuration.isNightModeActive
     */
    fun isNightModeActive(configuration: Configuration): Boolean {
        return (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}
