package fr.husta.android.dockersearch.utils.format.time;

import org.joda.time.base.AbstractInstant;
import org.joda.time.base.AbstractPartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * See Joda-Time DateTime formatting based on locale.
 * https://stackoverflow.com/questions/26102295/joda-time-datetime-formatting-based-on-locale
 */
public class JodaTimeFormatterUtils
{

    /**
     * @param instant
     * @return
     */
    public static String formatToShortDateTime(final AbstractInstant instant)
    {
        Locale locale = Locale.getDefault();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.shortDateTime().withLocale(locale);
        return dateTimeFormatter.print(instant);
    }

    /**
     * @param partial
     * @return
     */
    public static String formatToShortDateTime(final AbstractPartial partial)
    {
        Locale locale = Locale.getDefault();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.shortDateTime().withLocale(locale);
        return dateTimeFormatter.print(partial);
    }

}
