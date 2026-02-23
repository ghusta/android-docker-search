package fr.husta.android.dockersearch.utils.format.time

import android.text.format.DateUtils


class TimeFormatterUtils {
    object Android {
        private const val SECONDS_PER_MINUTE = 60
        private val SECONDS_PER_HOUR = 60 * 60
        private val SECONDS_PER_DAY = 24 * 60 * 60
        private val MILLIS_PER_MINUTE = 1000 * 60


        fun getRelativeTimeSpanString(startTime: Long): CharSequence? {
            return DateUtils.getRelativeTimeSpanString(startTime)
        }
    }
}
