package fr.husta.android.dockersearch.utils.ui;

import android.content.res.Configuration;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

public class UiModeUtils
{

    /**
     * @see Configuration#isNightModeActive()
     */
    public static boolean isNightModeActive(Configuration configuration)
    {
        return (configuration.uiMode & UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES;
    }

}
