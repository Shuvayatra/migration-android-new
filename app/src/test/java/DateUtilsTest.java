import com.taf.data.utils.DateUtils;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DateUtilsTest {

    // 1990-06-1
    public static final Calendar DATE_1;
    // 1996-01-2
    public static final Calendar DATE_2;
    // 1989-06-1
    public static final Calendar DATE_3;

    // comparison date - 2000-01-1
    public static final Calendar COMPARISON_DATE;

    static {

        DATE_1 = Calendar.getInstance();
        DATE_1.set(1990, Calendar.JUNE, 1);

        DATE_2 = Calendar.getInstance();
        DATE_2.set(1996, Calendar.JANUARY, 2);

        DATE_3 = Calendar.getInstance();
        DATE_3.set(1989, Calendar.JUNE, 1);

        COMPARISON_DATE = Calendar.getInstance();
        COMPARISON_DATE.set(2000, Calendar.JANUARY, 1);

    }


    @Test
    public void testAgeCalculation() {

        assertEquals("Age calculation not correct", 9, DateUtils.getAge(COMPARISON_DATE.getTime(),
                DATE_1.getTime()));
        assertEquals("Age calculation not correct", 3, DateUtils.getAge(COMPARISON_DATE.getTime(),
                DATE_2.getTime()));
        assertEquals("Age calculation not correct", 10, DateUtils.getAge(COMPARISON_DATE.getTime(),
                DATE_3.getTime()));
    }
}
