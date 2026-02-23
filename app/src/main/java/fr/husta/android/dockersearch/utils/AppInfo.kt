package fr.husta.android.dockersearch.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import fr.husta.android.dockersearch.R;

/**
 * @see android.content.pm.ApplicationInfo
 */
public class AppInfo
{

    /**
     * Returns application name ('app_name' in res/values/strings.xml).
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Context context)
    {
        return context.getString(R.string.app_name);
    }

    /**
     * Returns application version (app/build.gradle: versionName).
     *
     * @param context
     * @return
     */
    public static String getApplicationVersion(Context context)
    {
        PackageInfo pInfo = null;
        try
        {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.e(AppInfo.class.getSimpleName(), e.getMessage(), e);
        }
        return pInfo.versionName;
    }

}
