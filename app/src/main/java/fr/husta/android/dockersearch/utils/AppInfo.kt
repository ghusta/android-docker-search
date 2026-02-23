package fr.husta.android.dockersearch.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import fr.husta.android.dockersearch.R

/**
 * @see android.content.pm.ApplicationInfo
 */
object AppInfo {
    /**
     * Returns application name ('app_name' in res/values/strings.xml).
     * 
     * @param context
     * @return
     */
    fun getApplicationName(context: Context): String {
        return context.getString(R.string.app_name)
    }

    /**
     * Returns application version (app/build.gradle: versionName).
     * 
     * @param context
     * @return
     */
    fun getApplicationVersion(context: Context): String? {
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(AppInfo::class.java.getSimpleName(), e.message, e)
        }
        return pInfo?.versionName
    }
}
