package fr.husta.android.dockersearch.utils.format.unit;

import android.content.Context;

import java.util.Locale;

public class ByteSizeFormatterUtils
{

    /**
     * How to convert byte size into human readable format in java?
     * https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     */
    public static String humanReadableByteCount(long sizeBytes, boolean si)
    {
        int unit = si ? 1000 : 1024;
        if (sizeBytes < unit)
        {
            return sizeBytes + " B";
        }
        int exp = (int) (Math.log(sizeBytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.getDefault(), "%.1f %sB", sizeBytes / Math.pow(unit, exp), pre);
    }

    public static class Android
    {

        /**
         * android.text.format.Formatter.formatShortFileSize(activityContext, bytes);
         * <p></p>
         * android.text.format.Formatter.formatFileSize(activityContext, bytes);
         */
        public static String formatSize(Context context, long sizeBytes)
        {
            return android.text.format.Formatter.formatFileSize(context, sizeBytes);
        }

        public static String formatShortSize(Context context, long sizeBytes)
        {
            return android.text.format.Formatter.formatShortFileSize(context, sizeBytes);
        }

    }

}
