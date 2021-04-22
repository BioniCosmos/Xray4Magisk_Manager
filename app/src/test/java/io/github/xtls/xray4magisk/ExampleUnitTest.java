package io.github.xtls.xray4magisk;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testDate() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        try {
            Date test = date.parse("20210414");
            System.out.println(date.format(test));
        } catch (java.text.ParseException e) {

        }

    }
}