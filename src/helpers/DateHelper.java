/*
 * Class that handles date operations throughout the program
 */

package helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateHelper {

    // Easy to change values
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("u-MM-d HH:mm:ss");

    // Returns the Date Object equivalent of String
    public static LocalDateTime stringToDate(String date) {
        try {
            return LocalDateTime.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted string equivalent of date
    public static String dateToString(LocalDateTime date) {
        try {
            return date.format(DATE_FORMATTER);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, "models.helpers.DateHelper");
            return null;
        }
    }

    // Returns formatted current date time
    public static String getCurrentDateTimeString() {
        return dateToString(LocalDateTime.now());
    }

    // Returns true if date supplied has already passed
    public static Boolean isDateBeforeNow(LocalDateTime date1) {
        return date1.isBefore(LocalDateTime.now());
    }

    // Returns current date time
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // Adds minutes to supplied date
    public static LocalDateTime addMinutes(LocalDateTime date, int minutes) {
        return date.plusMinutes(minutes);
    }
}
