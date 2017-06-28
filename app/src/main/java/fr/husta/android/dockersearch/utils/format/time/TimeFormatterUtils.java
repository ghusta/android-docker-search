package fr.husta.android.dockersearch.utils.format.time;


import android.text.format.DateUtils;

public class TimeFormatterUtils
{

    public static class Android
    {

        private static final int SECONDS_PER_MINUTE = 60;
        private static final int SECONDS_PER_HOUR = 60 * 60;
        private static final int SECONDS_PER_DAY = 24 * 60 * 60;
        private static final int MILLIS_PER_MINUTE = 1000 * 60;


        public static CharSequence getRelativeTimeSpanString(long startTime)
        {
            return DateUtils.getRelativeTimeSpanString(startTime);
        }

    }

}
