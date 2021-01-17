import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {
    public static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static String dateToString(Date date) {
        return format.format(date);
    }
    public static String dateToString(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

    public static Date stringToDate(String dateString) {
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Date stringToDate(String dateString, SimpleDateFormat format) {
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getYearGap(Date startDate, Date endDate) {
        try {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("date should not be null");
            }
            Calendar endTime = getCalendarFromDate(endDate);
            if (endTime.before(startDate)) {
                throw new IllegalArgumentException("start day is later than end day");
            }
            Calendar startTime = getCalendarFromDate(startDate);
            int endYear = endTime.get(Calendar.YEAR);
            int startYear = startTime.get(Calendar.YEAR);
            int endMonth = endTime.get(Calendar.MONTH);
            int startMonth = startTime.get(Calendar.MONTH);
            int endDay = endTime.get(Calendar.DAY_OF_MONTH);
            int startDay = startTime.get(Calendar.DAY_OF_MONTH);
            int yearGap = endYear - startYear;
            if (endMonth <= startMonth) {
                if (endMonth == startMonth) {
                    if (endDay < startDay) {
                        yearGap--;
                    }
                } else {
                    yearGap--;
                }
            }
            return yearGap;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Calendar getCalendarFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
