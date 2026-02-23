package fr.husta.android.dockersearch.utils.format.unit

import android.content.Context
import android.text.format.Formatter
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

object ByteSizeFormatterUtils {
    /**
     * How to convert byte size into human-readable format in java?
     * https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     */
    fun humanReadableByteCount(sizeBytes: Long, si: Boolean): String {
        val unit = if (si) 1000 else 1024
        if (sizeBytes < unit) {
            return "$sizeBytes B"
        }
        val exp = (ln(sizeBytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + (if (si) "" else "i")
        return String.format(
            Locale.getDefault(),
            "%.1f %sB",
            sizeBytes / unit.toDouble().pow(exp.toDouble()),
            pre
        )
    }

    object Android {
        /**
         * android.text.format.Formatter.formatShortFileSize(activityContext, bytes);
         * 
         * 
         * android.text.format.Formatter.formatFileSize(activityContext, bytes);
         */
        fun formatSize(context: Context?, sizeBytes: Long): String? {
            return Formatter.formatFileSize(context, sizeBytes)
        }

        @JvmStatic
        fun formatShortSize(context: Context?, sizeBytes: Long): String? {
            return Formatter.formatShortFileSize(context, sizeBytes)
        }
    }
}
