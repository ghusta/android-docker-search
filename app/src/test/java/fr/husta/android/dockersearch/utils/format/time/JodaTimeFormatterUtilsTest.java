package fr.husta.android.dockersearch.utils.format.time;

import android.support.test.filters.SmallTest;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

@SmallTest
public class JodaTimeFormatterUtilsTest
{

    private Locale systemLocale;

    @Before
    public void setUp() throws Exception
    {
        systemLocale = Locale.getDefault();
    }

    @After
    public void tearDown() throws Exception
    {
        Locale.setDefault(systemLocale);
    }

    @Test
    public void formatToShortDateTime() throws Exception
    {
        String res;
        DateTime dateTime = DateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(dateTime);
        System.out.println(res);

        LocalDateTime localDateTime = LocalDateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(localDateTime);
        System.out.println(res);

        Instant instant = Instant.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(instant);
        System.out.println(res);
    }

    @Test
    public void formatToShortDateTime_LocaleDE() throws Exception
    {
        Locale.setDefault(Locale.GERMANY);

        String res;
        DateTime dateTime = DateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(dateTime);
        System.out.println(res);

        LocalDateTime localDateTime = LocalDateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(localDateTime);
        System.out.println(res);

        Instant instant = Instant.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(instant);
        System.out.println(res);
    }

    @Test
    public void formatToShortDateTime_LocaleUS() throws Exception
    {
        Locale.setDefault(Locale.US);

        String res;
        DateTime dateTime = DateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(dateTime);
        System.out.println(res);

        LocalDateTime localDateTime = LocalDateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(localDateTime);
        System.out.println(res);

        Instant instant = Instant.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(instant);
        System.out.println(res);
    }

    @Test
    public void formatToShortDateTime_LocaleJP() throws Exception
    {
        Locale.setDefault(Locale.JAPAN);

        String res;
        DateTime dateTime = DateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(dateTime);
        System.out.println(res);

        LocalDateTime localDateTime = LocalDateTime.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(localDateTime);
        System.out.println(res);

        Instant instant = Instant.now();
        res = JodaTimeFormatterUtils.formatToShortDateTime(instant);
        System.out.println(res);
    }

}